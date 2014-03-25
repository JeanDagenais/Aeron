/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.aeron.benchmark.filelocks;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Condition;

import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

public class FileLockBenchmark
{

    //public static final String NON_TMPFS_FILE = "/tmp/filelock_benchmark";
    public static final String TMPFS_FILE = "/dev/shm/filelock_benchmark";

    private static final long MUTEX_OFFSET = 0;
    private static final long PING_LOCK_OFFSET = 2;
    private static final long PONG_LOCK_OFFSET = 4;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        boolean isPinger = args.length == 1 && Boolean.parseBoolean(args[0]);

        final File file = new File(TMPFS_FILE);
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer data = channel.map(READ_WRITE, 0, 10);

        final FileLockBasedLock lock = new FileLockBasedLock(channel, MUTEX_OFFSET);
        final Condition pinged = new FileLockCondition(lock, channel, PING_LOCK_OFFSET);
        final Condition ponged = new FileLockCondition(lock, channel, PONG_LOCK_OFFSET);

        final PingPongBenchmark benchmark = new PingPongBenchmark(lock, pinged, ponged, data);
        final Runnable toRun = isPinger ? benchmark.pinger() : benchmark.ponger();
        toRun.run();
    }

}
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
package uk.co.real_logic.aeron.mediadriver;

import java.nio.ByteBuffer;

/**
 * Interface for encapsulating the strategy of allocating ByteBuffers for Session, Channel, and Term
 */
public interface BufferManagementStrategy
{

    ByteBuffer addSenderChannel(final long sessionId, final long channelId, final long termId);

    ByteBuffer incrementSenderTerm(final long sessionId, final long channelId);

    void removeSenderChannel(final long sessionId, final long channelId);

    void removeSenderTerm(final long sessionId, final long channelId, final long termId);

    ByteBuffer addReceiverTerm(final UdpDestination destination, final long sessionId, final long channelId, final long termId);

    ByteBuffer incrementReceiverTerm(final UdpDestination destination, final long sessionId, final long channelId);
}
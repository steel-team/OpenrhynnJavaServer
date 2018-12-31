/*
MIT License
-----------

Copyright (c) 2019 Ivan Yurkov (MB "Stylo tymas" http://steel-team.net)
Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/
package com.steelteam.openrhynn.network.handlers.server;

import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GamePlayfieldLoadRequest;
import com.steelteam.openrhynn.network.messages.server.GamePlayfieldLoadChunk;
import io.netty.channel.ChannelHandlerContext;

public class HandleGamePlayfieldLoadRequest {
    public HandleGamePlayfieldLoadRequest(ORClient client, ChannelHandlerContext ctx, GamePlayfieldLoadRequest message) {

        if(World.registeredWorlds.containsKey(client.currentChar.connectedModel.worldId)) {
            World wld = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            int totalSize = wld.data.length;
            int chunkSize = 512;

            int curChunkNum = 0;
            int curChunkSize = chunkSize;
            int curOffset = 0;

            while (curOffset < totalSize) {
                ++curChunkNum;
                if (totalSize - curOffset < chunkSize)
                    curChunkSize = totalSize - curOffset;

                byte[] buf = new byte[curChunkSize];
                System.arraycopy(wld.data, curOffset, buf, 0, curChunkSize);

                client.writeMessage(new GamePlayfieldLoadChunk(totalSize, curChunkNum, curChunkSize, buf).getData());

                curOffset += curChunkSize;
            }
        }
    }
}

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

import com.steelteam.openrhynn.data.Graphics;
import com.steelteam.openrhynn.enums.GraphicsType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GamePlayfieldEnterWorldRequest;
import com.steelteam.openrhynn.network.messages.server.GamePlayfieldGraphicsInfo;
import com.steelteam.openrhynn.network.messages.server.GamePlayfieldInfo;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

public class HandleGamePlayfieldEnterWorldRequest {
    public HandleGamePlayfieldEnterWorldRequest(ORClient client, ChannelHandlerContext ctx, GamePlayfieldEnterWorldRequest message) {
        if(World.registeredWorlds.containsKey(client.currentChar.connectedModel.worldId)) {
            World world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            //send map info(width, height, name)

            client.writeMessage(new GamePlayfieldInfo(world.id, world.width, world.height, world.name).getData());
            //send graphics info
            ArrayList<Integer> graphics = new ArrayList<>();
            graphics.addAll(world.graphicsBackground);
            graphics.addAll(world.graphicsCharacter);

            ArrayList<Integer> al = new ArrayList<>();
            al.add(100005);

            client.writeMessage(new GamePlayfieldGraphicsInfo(GraphicsType.BACKGROUND, graphics).getData());
            client.writeMessage(new GamePlayfieldGraphicsInfo(GraphicsType.CHARACTER, al).getData());

            //client.writeMessage(new GamePlayfieldGraphicsInfo(GraphicsType.CHARACTER, world.graphicsCharacter).getData());
            //client.writeMessage(new GamePlayfieldGraphicsInfo(GraphicsType.BACKGROUND, world.graphicsBackground).getData());
        }
    }
}

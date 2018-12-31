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

import com.steelteam.openrhynn.enums.ResponseCode;
import com.steelteam.openrhynn.enums.Variable;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GamePlayfieldEnterRequest;
import com.steelteam.openrhynn.network.messages.server.GameHelpEntry;
import com.steelteam.openrhynn.network.messages.server.GameItemAdd;
import com.steelteam.openrhynn.network.messages.server.GamePlayfieldEnterResult;
import com.steelteam.openrhynn.scripting.ScriptManager;
import io.netty.channel.ChannelHandlerContext;

public class HandleGamePlayfieldEnterRequest {
    public HandleGamePlayfieldEnterRequest(ORClient client, ChannelHandlerContext ctx, GamePlayfieldEnterRequest message) {
        //after all sent unlock flag
        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");
        World world = null;
        if(!client.worldFlagLocked || !World.registeredWorlds.containsKey(client.currentChar.connectedModel.worldId)) {
            responseStr = client.getGL("wrong_message");
        } else {
            responseStr = "ok";
            responseCode = ResponseCode.OK;

            world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            world.addEntity(client.currentChar);
            client.worldFlagLocked = false;
        }
        client.writeMessage(new GamePlayfieldEnterResult(responseCode, responseStr).getData());
        if(world != null) {
            for (Pickupable obj : world.getPickupables())
                client.writeMessage(new GameItemAdd(obj).getData());
        }

        if(client.currentChar.getVariable(Variable.VAR_INITIAL_HELP) == -1) {
            //send initial help
            client.currentChar.setVariable(Variable.VAR_INITIAL_HELP, 1);

            ScriptManager.executeHelp(client.currentChar, 1);
        }
    }
}

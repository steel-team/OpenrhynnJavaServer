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

import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameItemPickupRequest;
import com.steelteam.openrhynn.network.messages.server.GameInfo;
import com.steelteam.openrhynn.network.messages.server.GameUpdateGold;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameItemPickupRequest {
    public HandleGameItemPickupRequest(ORClient client, ChannelHandlerContext ctx, GameItemPickupRequest message) {
        if(World.registeredWorlds.containsKey(client.currentChar.connectedModel.worldId)) {
            World world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            Pickupable pickupable = world.getPickupable(message.getObjectId());
            if(pickupable != null) {
                world.removePickupable(pickupable.objectId);
                if(pickupable.usageType == UsageType.GOLD) {
                    client.currentChar.connectedModel.gold += pickupable.units;
                    client.writeMessage(new GameUpdateGold(client.currentChar.connectedModel.gold).getData());
                    client.writeMessage(new GameInfo(client.getGL("added_gold") + "(" + pickupable.units + ")").getData());
                } else {
                    /* add to inventory */
                    client.currentChar.addItem(pickupable.itemTemplate, pickupable.units);
                }
            }
        }
    }
}

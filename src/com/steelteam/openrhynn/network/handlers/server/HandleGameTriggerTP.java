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

import com.steelteam.openrhynn.enums.Direction;
import com.steelteam.openrhynn.enums.TriggerType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Cell;
import com.steelteam.openrhynn.models.Portal;
import com.steelteam.openrhynn.models.Trigger;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.server.*;
import com.steelteam.openrhynn.scripting.ScriptManager;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameTriggerTP {
    public HandleGameTriggerTP(ORClient client, ChannelHandlerContext ctx, GameTriggerTP message) {

        synchronized (client.currentChar.entityLock) {
            World world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);

            int cellX = message.getCellX();
            int cellY = message.getCellY();

            String cellKey = cellX + "_" + cellY;
            //check world PortalEndPoints
            if (world.portals.containsKey(cellKey)) {
                Portal portal = world.portals.get(cellKey);
                if (portal.required_level > client.currentChar.level) {
                    client.writeMessage(new GameInfoOverlay(client.getGL("level_enter")).getData());
                    client.writeMessage(new GameTriggerUnlock().getData());
                } else {
                    //check required_quest
                    boolean condQuestChecked = true;

                    if (!condQuestChecked) {
                        client.writeMessage(new GameInfoOverlay(client.getGL("level_enter")).getData());
                        client.writeMessage(new GameTriggerUnlock().getData());
                    } else {
                        client.currentChar.teleport(portal.destinationWorld.id, portal.destinationX, portal.destinationY);
                    }
                }
                return;
            }
            //check trigger

            boolean triggerFound = false;
            if (world.triggers.containsKey(cellKey)) {
                triggerFound = true;
                client.writeMessage(new GameTriggerUnlock().getData());

                Trigger trigger = world.triggers.get(cellKey);
                ScriptManager.executeTrigger(trigger.script, client.currentChar);
            }

            //teleport on map
            if(!triggerFound) {
                int y_up = cellY - 1;
                int y_down = cellY + 1;
                int x_right = cellX + 1;
                int x_left = cellX - 1;


                int x = client.currentChar.x;
                int y = client.currentChar.y;

                client.writeMessage(new GameTriggerUnlock().getData());

                try {
                    if (world.cells[x_left][cellY].blocked && world.cells[x_right][cellY].blocked) {
                        //up/down
                        if (y < cellY * Cell.cellSize + Cell.cellSize / 2) {
                            //down
                            client.currentChar.x = cellX * Cell.cellSize;
                            client.currentChar.y = y_down * Cell.cellSize /*+ Cell.cellSize*/ + 2;
                        } else {
                            //up
                            client.currentChar.x = cellX * Cell.cellSize;
                            client.currentChar.y = y_up * Cell.cellSize - 2;
                        }
                    } else {
                        //left/right
                        if (x < cellX * Cell.cellSize + Cell.cellSize / 2) {
                            //right
                            client.currentChar.y = cellY * Cell.cellSize;
                            client.currentChar.x = x_right * Cell.cellSize /*+ Cell.cellSize*/ + 2;
                        } else {
                            //left
                            client.currentChar.y = cellY * Cell.cellSize;
                            client.currentChar.x = x_left * Cell.cellSize - 2;
                        }
                    }

                    client.writeMessage(new GameUpdateCords(client.currentChar.x, client.currentChar.y, client.currentChar.direction).getData());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}

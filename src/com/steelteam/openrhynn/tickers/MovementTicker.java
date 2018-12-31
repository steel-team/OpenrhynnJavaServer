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
package com.steelteam.openrhynn.tickers;

import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.network.messages.server.GameCharacterMoveInfo;
import com.steelteam.openrhynn.tickers.core.Ticker;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.ArrayList;
import java.util.Collection;

public class MovementTicker extends Ticker {
    private World world = null;

    public MovementTicker(World _world) {
        world = _world;
    }
    private ArrayList<GameCharacterMoveInfo> moves = new ArrayList<>();

    @Override
    public void tick(int threadIndex) {
        Collection<Entity> entities = world.getEntities();

        for(Entity entity : entities) {
            if(entity.movementRequired) {
                moves.add(new GameCharacterMoveInfo(entity.x, entity.y, entity.direction, entity.objectId));
                entity.movementRequired = false;
            }
        }

        for(Entity entity : entities) {
            if(entity.entityType == EntityType.CHARACTER) {
                Character chr = (Character)entity;
                if(chr.movementSentCount == 0) {
                    //System.out.println("Sending for " + chr.objectId + ", msc: " + chr.movementSentCount);
                    for (GameCharacterMoveInfo msg : moves) {
                        if (msg.getObjectId() != chr.objectId) {

                        /*int wattempts = 0;
                        while (!chr.connectedClient.context.channel().isWritable() && wattempts < 5) {
                            chr.connectedClient.context.flush();
                            wattempts++;
                        }*/

                            if (chr.connectedClient.context.channel().isWritable() && !chr.connectedClient.busy) {
                                chr.connectedClient.context.writeAndFlush(msg.getData()).addListener(new ChannelFutureListener() {
                                    @Override
                                    public void operationComplete(ChannelFuture future) {
                                        chr.movementSentCount--;
                                    }
                                });
                                chr.movementSentCount++;
                            }

                        }
                    }
                    //chr.connectedClient.context.flush();
                }
            }
        }

        moves.clear();
    }
}

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

import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.entities.Npc;
import com.steelteam.openrhynn.enums.AIState;
import com.steelteam.openrhynn.enums.AIType;
import com.steelteam.openrhynn.enums.Direction;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.scripting.ScriptManager;
import com.steelteam.openrhynn.tickers.core.Ticker;
import com.steelteam.openrhynn.utilits.AdvRandom;
import com.steelteam.openrhynn.utilits.Time;

import java.util.ArrayList;

public class AITickerNpc extends Ticker {
    private World world = null;
    private static AdvRandom sharedRandom = new AdvRandom();

    public AITickerNpc(World _world) {
        world = _world;
    }

    @Override
    public void tick(int threadIndex) {
        long currentTime = Time.getUnixTimeMillis();
        ArrayList<Entity> entities = world.getNpcs();
        for(Entity entity : entities) {
            Npc npc = (Npc) entity;
            Entity target = null;
            if(!npc.dead) {

                /* state randomize for stay/free_move */
                if((npc.aiState == AIState.STAY || npc.aiState == AIState.FREE_MOVE) && npc.aiTime + 1500 < currentTime && npc.connectedModel.ai != AIType.STAY) {
                    int mode = sharedRandom.nextIntImproved(0, 2);
                    switch (mode) {
                        case 0:
                            npc.aiState = AIState.STAY;
                            break;
                        case 1:
                            npc.aiState = AIState.FREE_MOVE;
                            break;
                    }
                    npc.aiTime = currentTime;
                }

                /* talk if it's time */
                if(npc.connectedModel.talk_script != null && npc.talkTime + npc.connectedModel.talkDelay < currentTime) {
                    npc.talkTime = currentTime;
                    ScriptManager.executeRequestTalkNpc(npc.connectedModel.talk_script, world, npc);
                }

                switch (npc.aiState) {
                    case FREE_MOVE:
                    {
                        int[] futureCords = seekCoordinatesForMove(npc, npc.choosenDirection);
                        int new_x = futureCords[0];
                        int new_y = futureCords[1];

                        if(npc.choosenTime + 3000 < currentTime || !coordWithinRange(npc, new_x, new_y)) {
                            int direction = sharedRandom.nextIntImproved(0, 4);
                            if(direction == 3)
                                direction = 4;
                            npc.choosenDirection = direction;
                            npc.choosenTime = currentTime;
                            //move only in next tick
                            return;
                        }

                        move(npc, new_x, new_y);

                    }
                    break;
                }

            }
        }
    }


    /* within range and not blocked */

    public void move(Npc npc, int new_x, int new_y) {
        if(npc.moveTickerCounter > 1) {
            npc.moveTickerCounter = 0;
            if (npc.updatePosition(world, new_x, new_y, npc.choosenDirection, true, false))
                world.movement(npc);
        } else {
            npc.moveTickerCounter++;
        }
    }

    public boolean coordWithinRange(Npc npc, int x, int y) {
        boolean ret = false;

        int range = npc.connectedModel.move_range;

        int x_lower = npc.spawnX - range;
        int x_upper = npc.spawnX + range;

        int y_lower = npc.spawnY - range;
        int y_upper = npc.spawnY + range;

        if(x > x_lower && x < x_upper && y > y_lower && y < y_upper)
            ret = true;

        return ret;
    }

    public int[] seekCoordinatesForMove(Npc npc, int direction) {
        int[] nint = new int[2];
        nint[0] = npc.x;
        nint[1] = npc.y;
        switch (direction) {
            case Direction.UP:
                nint[1] -= npc.connectedModel.move_speed;
                break;
            case Direction.RIGHT:
                nint[0] += npc.connectedModel.move_speed;
                break;
            case Direction.DOWN:
                nint[1] += npc.connectedModel.move_speed;
                break;
            case Direction.LEFT:
                nint[0] -= npc.connectedModel.move_speed;
                break;
        }
        return nint;
    }
}
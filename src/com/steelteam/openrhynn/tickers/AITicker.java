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
import com.steelteam.openrhynn.entities.Mob;
import com.steelteam.openrhynn.enums.AIState;
import com.steelteam.openrhynn.enums.AIType;
import com.steelteam.openrhynn.enums.Direction;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.logic.WorldTools;
import com.steelteam.openrhynn.scripting.ScriptManager;
import com.steelteam.openrhynn.tickers.core.Ticker;
import com.steelteam.openrhynn.utilits.AdvRandom;
import com.steelteam.openrhynn.utilits.Time;

import java.util.ArrayList;

public class AITicker extends Ticker {
    private World world = null;
    private static AdvRandom sharedRandom = new AdvRandom();

    public AITicker(World _world) {
        world = _world;
    }

    @Override
    public void tick(int threadIndex) {
        long currentTime = Time.getUnixTimeMillis();
        ArrayList<Entity> entities = world.getMobs();
        ArrayList<Entity> characters = world.getCharacters();
        for(Entity entity : entities) {
            Mob mob = (Mob) entity;
            Entity target = null;
            if(!mob.dead) {
                /* check aggressive */
                if(mob.connectedModel.aggresive && !hasTarget(mob) && mob.connectedModel.ai != AIType.STAY) {
                    //seek for target
                    mob.aiFollow = findTarget(mob, characters);
                }

                if(hasTarget(mob) && mob.connectedModel.ai != AIType.STAY)
                    mob.aiState = AIState.FOLLOW_TARGET;

                int target_x = 0;
                int target_y = 0;
                target = world.getEntity(mob.aiFollow);
                if(target != null) {
                    target_x = target.x;
                    target_y = target.y;
                }

                if((!hasTarget(mob) || !coordWithinRange(mob, target_x, target_y)) && mob.aiState == AIState.FOLLOW_TARGET) {
                    mob.aiFollow = 0;
                    if(mob.connectedModel.ai == AIType.STAY)
                        mob.aiState = AIState.STAY;
                    else
                        mob.aiState = AIState.FREE_MOVE;
                    mob.aiTime = currentTime;
                }

                /* state randomize for stay/free_move */
                if((mob.aiState == AIState.STAY || mob.aiState == AIState.FREE_MOVE) && mob.aiTime + 1500 < currentTime && mob.connectedModel.ai != AIType.STAY) {
                    int mode = sharedRandom.nextIntImproved(0, 2);
                    switch (mode) {
                        case 0:
                            mob.aiState = AIState.STAY;
                            break;
                        case 1:
                            mob.aiState = AIState.FREE_MOVE;
                            break;
                    }
                    mob.aiTime = currentTime;
                }

                /* attack if we have target */
                if(hasTarget(mob) && !mob.connectedModel.peaceful) {
                    mob.attack(world.getEntity(mob.aiFollow));
                }

                /* talk if it's time */
                if(mob.connectedModel.talk_script != null && mob.talkTime + mob.connectedModel.talkDelay < currentTime) {
                    mob.talkTime = currentTime;
                    ScriptManager.executeRequestTalk(mob.connectedModel.talk_script, world, mob);
                }

                switch (mob.aiState) {
                    case FOLLOW_TARGET:
                    {
                        //System.out.println("following target: " + target.objectId + " at x = " + target_x + " & y = " + target_y + " within_range = "+coordWithinRange(mob, target_x, target_y));
                        /* check possible directions */
                        if(target_x > mob.x && movePossible(mob, Direction.RIGHT)) {
                            mob.choosenDirection = Direction.RIGHT;
                        } else if(target_x < mob.x && movePossible(mob, Direction.LEFT)) {
                            mob.choosenDirection = Direction.LEFT;
                        } else if(target_y < mob.y && movePossible(mob, Direction.UP)) {
                            mob.choosenDirection = Direction.UP;
                        } else if(target_y > mob.y && movePossible(mob, Direction.DOWN)) {
                            mob.choosenDirection = Direction.DOWN;
                        }

                        int[] futureCords = seekCoordinatesForMove(mob, mob.choosenDirection);
                        int new_x = futureCords[0];
                        int new_y = futureCords[1];

                        move(mob, new_x, new_y);

                    }
                    break;
                    case FREE_MOVE:
                    {
                        int[] futureCords = seekCoordinatesForMove(mob, mob.choosenDirection);
                        int new_x = futureCords[0];
                        int new_y = futureCords[1];

                        if(mob.choosenTime + 3000 < currentTime || !coordWithinRange(mob, new_x, new_y)) {
                            int direction = sharedRandom.nextIntImproved(0, 4);
                            if(direction == 3)
                                direction = 4;
                            mob.choosenDirection = direction;
                            mob.choosenTime = currentTime;
                            //move only in next tick
                            return;
                        }

                        move(mob, new_x, new_y);

                    }
                    break;
                }

            }
        }
    }


    /* within range and not blocked */
    public boolean movePossible(Mob mob, int direction) {
        int[] futureCords = seekCoordinatesForMove(mob, direction);
        if(!WorldTools.isBlocked(world, futureCords[0], futureCords[1], mob.graphicsDim) &&
           //!WorldTools.isPeaceful(world, futureCords[0], futureCords[1], mob.graphicsDim) &&
           coordWithinRange(mob, futureCords[0], futureCords[1])
          )
            return true;
        return false;
    }

    public void move(Mob mob, int new_x, int new_y) {
        if(mob.moveTickerCounter > 1) {
            mob.moveTickerCounter = 0;
            if (mob.updatePosition(world, new_x, new_y, mob.choosenDirection, mob.connectedModel.peaceful, false))
                world.movement(mob);
        } else {
            mob.moveTickerCounter++;
        }
    }

    public boolean coordWithinRange(Mob mob, int x, int y) {
        boolean ret = false;

        int range = mob.connectedModel.move_range;

        int x_lower = mob.spawnX - range;
        int x_upper = mob.spawnX + range;

        int y_lower = mob.spawnY - range;
        int y_upper = mob.spawnY + range;

        if(x > x_lower && x < x_upper && y > y_lower && y < y_upper)
            ret = true;

        return ret;
    }

    public int[] seekCoordinatesForMove(Mob mob, int direction) {
        int[] nint = new int[2];
        nint[0] = mob.x;
        nint[1] = mob.y;
        switch (direction) {
            case Direction.UP:
                nint[1] -= mob.connectedModel.move_speed;
                break;
            case Direction.RIGHT:
                nint[0] += mob.connectedModel.move_speed;
                break;
            case Direction.DOWN:
                nint[1] += mob.connectedModel.move_speed;
                break;
            case Direction.LEFT:
                nint[0] -= mob.connectedModel.move_speed;
                break;
        }
        return nint;
    }

    public int findTarget(Mob mob, ArrayList<Entity> characters) {
        //find target within range
        for(Entity ent : characters) {
            if(ent != null && coordWithinRange(mob, ent.x, ent.y) && world.hasEntity(ent.objectId))
                return ent.objectId;
        }
        return 0;
    }

    public boolean hasTarget(Mob mob) {
        boolean hTarget = false;
        if(world.hasEntity(mob.aiFollow))
            hTarget = true;
        return hTarget;
    }
}
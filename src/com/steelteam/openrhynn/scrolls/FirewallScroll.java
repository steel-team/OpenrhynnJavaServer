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
package com.steelteam.openrhynn.scrolls;

import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.logic.Formulas;
import com.steelteam.openrhynn.models.Cell;
import com.steelteam.openrhynn.models.Firewall;
import com.steelteam.openrhynn.network.messages.server.GameFirewall;
import com.steelteam.openrhynn.network.messages.server.GameFirewallRemove;
import com.steelteam.openrhynn.scrolls.core.BaseScroll;
import com.steelteam.openrhynn.utilits.Time;

import java.util.ArrayList;

public class FirewallScroll extends BaseScroll {

    private long startTime = 0;

    public int cleanDamage = 0;
    public int cellX = 0;
    public int cellY = 0;
    public int time = 0;

    private int damageTimePerTick = 200;
    private int durability = 0;
    private long lastDamageTime = 0;

    @Override
    public void tick() {
        long currentTime = Time.getUnixTimeMillis();
        try {
            Entity attacker = world.getEntity(attackerId);

            if(!world.scrolls.contains(this)) {
                startTime = currentTime;


                cleanDamage += Formulas.calculateMagicPower(attacker.getMagicMax(), cleanDamage);

                //timing
                int millitime = time * 1000;
                damageTimePerTick = 200;
                //int howmany = millitime / t;
                durability = millitime;


                //add firewall
                Firewall fire = new Firewall();
                fire.attackerId = attackerId;
                fire.cellX = cellX;
                fire.cellY = cellY;
                world.firewalls.put(cellX + "_" + cellY, fire);
                world.broadcastMessage(new GameFirewall(attackerId, cellX, cellY).getData(), 0);

                cast();

                world.scrolls.add(this);
            } else {
                if(startTime + durability < currentTime) {
                    //remove firewall
                    world.firewalls.remove(cellX + "_" + cellY);
                    world.broadcastMessage(new GameFirewallRemove(cellX, cellY).getData(), 0);
                    world.scrolls.remove(this);
                } else {
                    if(lastDamageTime + damageTimePerTick < currentTime) {
                        lastDamageTime = currentTime;

                        int x1 = 0;
                        int x2 = 0;
                        int y1 = 0;
                        int y2 = 0;

                        int centerX = cellX * Cell.cellSize;
                        int centerY = cellY * Cell.cellSize;

                        int cof = Cell.cellSize;//3 cells range ---*---

                        x1 = centerX;
                        x2 = centerX + cof;// + cof;

                        y1 = centerY - cof;
                        y2 = centerY;// + cof;


                        ArrayList<Entity> entities = world.getMobs();
                        entities.addAll(world.getCharacters());

                        for(Entity ent : entities) {
                            if(ent.x > x1 && ent.x < x2 && ent.y > y1 && ent.y < y2) {
                                world.clearDamage(attacker, ent, cleanDamage, true);
                            }
                        }





                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

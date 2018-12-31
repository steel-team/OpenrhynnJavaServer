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

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.logic.Formulas;
import com.steelteam.openrhynn.network.messages.server.GameSpeed;
import com.steelteam.openrhynn.scrolls.core.BaseScroll;
import com.steelteam.openrhynn.utilits.Time;

public class HasteScroll extends BaseScroll {

    private long startTime = 0;

    public int durability = 0;
    public int moveUpPercent = 0;

    @Override
    public void tick() {
        long currentTime = Time.getUnixTimeMillis();
        try {
            Entity attacker = world.getEntity(attackerId);
            Entity target = world.getEntity(targetId);

            if(!world.scrolls.contains(this)) {
                if(target.hasted)
                    return;

                durability = durability * 1000;
                startTime = currentTime;

                if(target.entityType == EntityType.CHARACTER) {
                    Character chr = (Character)target;
                    chr.connectedClient.writeMessage(new GameSpeed(moveUpPercent).getData());
                } else {
                    return;
                }
                target.hasted = true;

                world.scrolls.add(this);

            } else if(startTime + durability < currentTime) {
                target.hasted = false;

                Character chr = (Character)target;
                chr.connectedClient.writeMessage(new GameSpeed(ServerConfig.defaultPlayerSpeed).getData());

                world.scrolls.remove(this);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

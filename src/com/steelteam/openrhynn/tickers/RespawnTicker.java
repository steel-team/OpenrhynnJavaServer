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
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.tickers.core.Ticker;
import com.steelteam.openrhynn.utilits.Time;

import java.util.ArrayList;
import java.util.Collection;

public class RespawnTicker extends Ticker {
    @Override
    public void tick(int threadIndex) {
        long currentTime = Time.getUnixTime();
        long currentTimeMillis = Time.getUnixTimeMillis();
        Collection<World> worlds = World.registeredWorlds.values();
        for (World world : worlds) {

            /* mobs */
            ArrayList<Entity> entities = world.getMobs();
            for(Entity entity : entities) {
                Mob mob = (Mob)entity;
                if(entity.dead && entity.deathTime + (mob.respawnDelay / 1000) < currentTime) {
                    mob.x = mob.spawnX;
                    mob.y = mob.spawnY;
                    mob.healthCurrent = mob.healthBase + mob.healthEffectsExtra;
                    world.respawnEntity(entity);
                }
            }

            /* items */
            for(Pickupable pickupable : world.registeredPickupables.values()) {
                if(!pickupable.present && pickupable.pickupTime + pickupable.respawnDelay < currentTimeMillis && pickupable.respawnDelay > 0)
                    world.spawnPickupable(pickupable.objectId);
            }
        }
    }
}

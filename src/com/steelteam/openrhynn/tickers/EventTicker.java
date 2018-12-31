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

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.scrolls.core.BaseScroll;
import com.steelteam.openrhynn.tickers.core.Ticker;
import com.steelteam.openrhynn.utilits.Time;
import com.sun.corba.se.spi.activation.Server;

public class EventTicker extends Ticker {

    private World world = null;

    public EventTicker(World _world) {
        world = _world;
    }

    @Override
    public void tick(int threadIndex) {

        long currentTime = Time.getUnixTimeMillis();

        if(ServerConfig.pickupableAutoRemove) {
            for(Pickupable pick : world.getPickupables()) {
                if(pick.autoRemoveSpawnTime > 0 && pick.autoRemoveSpawnTime + ServerConfig.pickupableRemoveTime < currentTime) {
                    world.removePickupable(pick.objectId);
                }
            }
        }

        for(BaseScroll scroll : world.scrolls) {
            scroll.tick();
        }
    }
}

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
package com.steelteam.openrhynn.scrolls.core;

import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.network.messages.server.GameSpellVisual;

public class BaseScroll {
    public World world = null;
    public int targetId = -1;
    public int attackerId = 0;

    public int flashTarget = 0;     //0 - false,1 - true
    public int useSpellVisuals = 0; //0 - false,1 - true
    public int useAttackerIcon = 0;    //0 - false,1 - true
    public int usevisual1 = 0; //0 - false,1 - true
    public int attackerHealth = 0;
    public int targetHealth = 1;

    public int spellVisualTime  = 0; //seconds
    public int spellVisualcolor = 0; //can be 0 or 1

    public int spellVisualTime1 = 0;
    public int spellVisualType1 = 0;

    public int icon = 0; //3-heal, 4-attack

    private boolean validated = false;

    public void validate() {
        if(world != null) {
            if(world.getEntity(targetId) != null && world.getEntity(attackerId) != null) {
                attackerHealth = world.getEntity(attackerId).healthCurrent;
                targetHealth = world.getEntity(targetId).healthCurrent;
                validated = true;
            }
        }
    }


    public void tick() {

    }

    public void cast() {
        world.broadcastMessage(new GameSpellVisual(this).getData(), 0);
    }
}

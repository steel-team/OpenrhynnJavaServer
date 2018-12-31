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
package com.steelteam.openrhynn.entities;

import com.steelteam.openrhynn.data.Graphics;
import com.steelteam.openrhynn.enums.AIState;
import com.steelteam.openrhynn.enums.Direction;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.NpcTemplate;

public class Npc extends Entity {

    public NpcTemplate connectedModel = null;
    public int worldId = 0;
    public int spawnX = 0;
    public int spawnY = 0;

    /* AI variables */
    public AIState aiState = AIState.STAY;
    public long aiTime = 0;//can not applied for all states(used for STAY, FREE_MOVE)

    /* free move */
    public int choosenDirection = Direction.UP;
    public long choosenTime = 0;
    public int moveTickerCounter = 0;



    public long talkTime = 0;


    public Npc() {
        entityType = EntityType.NPC;
    }

    /* data functions */
    public void fillFromModel() {

        this.clanId = connectedModel.clan_id;
        this.graphicsId = connectedModel.graphics_id;
        this.graphicsX = connectedModel.graphics_x;
        this.graphicsY = connectedModel.graphics_y;
        this.graphicsDim = connectedModel.graphics_dim;
        this.level = connectedModel.level;

        this.direction = Direction.DOWN;

        this.healthBase = connectedModel.health/* + connectedModel.healthEffectsExtra*/;
        this.healthEffectsExtra = 0;
        this.healthCurrent = connectedModel.health;

        this.name = connectedModel.name;



        this.manaBase = 0;
        this.manaEffectsExtra = 0;
        this.manaCurrent = 0;

        this.damageBase = 0;
        this.damageEffectsExtra = 0;

        this.defenseBase = 0;
        this.defenseEffectsExtra = 0;

        this.attackBase = 0;
        this.attackEffectsExtra = 0;

        this.skillBase = 0;
        this.skillEffectsExtra = 0;

        this.magicBase = 0;
        this.magicEffectsExtra = 0;

        this.attackRange = 0;


        World world = World.registeredWorlds.get(worldId);
        if(Graphics.registeredGraphics.containsKey(graphicsId) && !world.graphicsCharacter.contains(graphicsId))
            world.graphicsCharacter.add(graphicsId);

    }
}

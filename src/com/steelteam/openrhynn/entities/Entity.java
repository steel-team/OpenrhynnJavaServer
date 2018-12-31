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

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.enums.Direction;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.logic.WorldTools;
import com.steelteam.openrhynn.utilits.Time;

public class Entity {

    public final Object entityLock = new Object();
    public boolean entityDisposed = false;//set to true when entity should be completely removed(used to unlink POJO to be removed by GC)

    public int objectId = 0;//for character = id, for mobs see mob_spawning table(on_map_id)
    public EntityType entityType = EntityType.CHARACTER;
    public int clanId = 0;


    public boolean movementRequired = false;
    public int movementSentCount = 0;


    public int graphicsId = 0;
    public int graphicsX = 0;
    public int graphicsY = 0;
    public int graphicsDim = 0;

    public int graphicsIdBak = 0;
    public int graphicsXBak = 0;
    public int graphicsYBak = 0;
    public int graphicsDimBak = 0;



    public int level = 0;

    public int x = 0;
    public int y = 0;

    /* server side only */
    public int cellX = 0;
    public int cellY = 0;
    /* end */


    public int direction = 0;

    public int healthCurrent = 0;
    public int healthBase = 0;
    public int healthEffectsExtra = 0;
    public int getHealthMax() {
        return healthBase + healthEffectsExtra;
    }

    public String name = "";



    public int manaCurrent = 0;
    public int manaBase = 0;
    public int manaEffectsExtra = 0;
    public int getManaMax() {
        return manaBase + manaEffectsExtra;
    }


    public int damageBase = 0;
    public int damageEffectsExtra = 0;
    public int getDamageMax() {
        return damageBase + damageEffectsExtra;
    }

    public int defenseBase = 0;
    public int defenseEffectsExtra = 0;
    public int defenseEffectsTemp = 0;
    public int getDefenseMax() {
        return defenseBase + defenseEffectsExtra + defenseEffectsTemp;
    }

    public int attackBase = 0;
    public int attackEffectsExtra = 0;
    public int attackEffectsTemp = 0;
    public int getAttackMax() {
        return attackBase + attackEffectsExtra + attackEffectsTemp;
    }

    public int skillBase = 0;
    public int skillEffectsExtra = 0;
    public int getSkillMax() {
        return skillBase + skillEffectsExtra;
    }

    public int magicBase = 0;
    public int magicEffectsExtra = 0;
    public int getMagicMax() {
        return magicBase + magicEffectsExtra;
    }



    public int attackRange = ServerConfig.defaultWeaponRange;//should be updated on equip/unequip! TO-DO
    public int attackCharge = ServerConfig.defaultWeaponCharge;//should be updated on equip/unequip TO-DO

    public long lastAttackTime = 0;
    public long lastReceivedDamageTime = 0;
    public long lastVitalityIncrease = 0;

    /* skill variables */
    public boolean morphed = false;
    public boolean blessed = false;
    public boolean cursed = false;
    public boolean hasted = false;

    /* */
    public boolean invincible = false;


    public void restoreHP(int amount) {
        healthCurrent += amount;
        if(healthCurrent > getHealthMax())
            healthCurrent = getHealthMax();
    }

    public void restoreMP(int amount) {
        manaCurrent += amount;
        if(manaCurrent > getManaMax())
            manaCurrent = getManaMax();
    }


    public boolean updatePosition(World world, int x, int y, int direction, boolean allowPeaceful, boolean skipBlocked) {
        if(direction != Direction.UP && direction != Direction.DOWN && direction != Direction.LEFT && direction != Direction.RIGHT)
            return false;

        if(WorldTools.isBlocked(world, x, y, graphicsDim) && !skipBlocked)
            return false;

        if(!allowPeaceful && WorldTools.isPeaceful(world, x, y, graphicsDim))
            return false;


        this.x = x;
        this.y = y;
        this.direction = direction;

        return true;
    }

    public boolean dead = false;
    public long deathTime = 0;

    public void setDead() {
        deathTime = Time.getUnixTime();
        dead = true;
    }

}

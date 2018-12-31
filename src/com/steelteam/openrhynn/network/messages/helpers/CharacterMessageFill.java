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
package com.steelteam.openrhynn.network.messages.helpers;

import com.steelteam.openrhynn.models.CharacterModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterMessageFill {
    public static CharacterModel fillFromDb(ResultSet resultSet) throws SQLException {
        CharacterModel cm = new CharacterModel();
        cm.id = resultSet.getInt("id");
        cm.classId = resultSet.getInt("class_id");
        cm.clanId = resultSet.getInt("clan_id");
        cm.worldId = resultSet.getInt("world_id");
        cm.graphicsId = resultSet.getInt("graphics_id");
        cm.displayName = resultSet.getString("display_name");
        cm.graphicsX = resultSet.getInt("graphics_x");
        cm.graphicsY = resultSet.getInt("graphics_y");
        cm.graphicsDim = resultSet.getInt("graphics_dim");
        cm.x = resultSet.getInt("x");
        cm.y = resultSet.getInt("y");
        cm.level = resultSet.getInt("level");
        cm.levelPoints = resultSet.getInt("points");
        cm.experience = resultSet.getInt("exp");
        cm.gold = resultSet.getInt("gold");
        cm.healthBase = resultSet.getInt("health_base");
        cm.healthEffectsExtra = resultSet.getInt("health_effect_extra");
        cm.healthCurrent = resultSet.getInt("health_current");
        cm.manaBase = resultSet.getInt("mana_base");
        cm.manaEffectsExtra = resultSet.getInt("mana_effect_extra");
        cm.manaCurrent = resultSet.getInt("mana_current");
        cm.attackBase = resultSet.getInt("attack_base");
        cm.attackEffectsExtra = resultSet.getInt("attack_effect_extra");
        cm.defenseBase = resultSet.getInt("defense_base");
        cm.defenseEffectsExtra = resultSet.getInt("defense_effect_extra");
        cm.damageBase = resultSet.getInt("damage_base");
        cm.damageEffectsExtra = resultSet.getInt("damage_effect_extra");
        cm.skillBase = resultSet.getInt("skill_base");
        cm.skillEffectsExtra = resultSet.getInt("skill_effect_extra");
        cm.magicBase = resultSet.getInt("magic_base");
        cm.magicEffectsExtra = resultSet.getInt("magic_effect_extra");
        cm.healthregenerateBase = resultSet.getInt("healthregenerate_base");
        cm.healthregenerateEffectsExtra = resultSet.getInt("healthregenerate_effect_extra");
        cm.manaregenerateBase = resultSet.getInt("manaregenerate_base");
        cm.manaregenerateEffectsExtra = resultSet.getInt("manaregenerate_effect_extra");
        cm.ownStatus = resultSet.getString("custom_status_msg");
        return cm;
    }
}

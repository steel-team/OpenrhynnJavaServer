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
package com.steelteam.openrhynn.network.handlers.server;

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.enums.ItemType;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameItemEquipRequest;
import com.steelteam.openrhynn.network.messages.server.GameItemSetunits;
import com.steelteam.openrhynn.network.messages.server.GameRemoveInv;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.Statement;

public class HandleGameItemEquipRequest {
    public HandleGameItemEquipRequest(ORClient client, ChannelHandlerContext ctx, GameItemEquipRequest message) {
        int objId = message.getObjectId();
        if(client.currentChar.inventory.containsKey(objId)) {
            Item item = client.currentChar.inventory.get(objId);
            if(item.equiped == 0 && item.usage_type == UsageType.EQUIP) {
                if(item.required_skill <= client.currentChar.getSkillMax() && item.required_magic <= client.currentChar.getMagicMax()) {

                    /* unequip previous */
                    client.currentChar.unequipItemByType(item.type, false);

                    item.equiped = 1;
                    item.units_sell = 0;

                    /* update DB item */
                    Connection conn = null;
                    try {
                        conn = DataSource.getInstance().getConnection();
                        Statement state = conn.createStatement();
                        state.executeUpdate("UPDATE inventory SET units_sell='" + item.units_sell + "', equiped='1' WHERE id='" + item.id + "'");
                        conn.commit();
                        state.close();

                    } catch (Exception ex) { ex.printStackTrace();} finally { try { conn.close(); } catch (Exception e) {} }



                    /* update character */
                    //System.out.println(client.currentChar.attackEffectsExtra);
                    client.currentChar.attackEffectsExtra += item.attack_effect;
                    //System.out.println(client.currentChar.attackEffectsExtra);
                    client.currentChar.damageEffectsExtra += item.damage_effect;
                    client.currentChar.defenseEffectsExtra += item.defense_effect;
                    client.currentChar.healthEffectsExtra += item.health_effect;
                    client.currentChar.connectedModel.healthregenerateEffectsExtra += item.healthregenerate_effect;
                    client.currentChar.magicEffectsExtra += item.magic_effect;
                    client.currentChar.manaEffectsExtra += item.mana_effect;
                    client.currentChar.connectedModel.manaregenerateEffectsExtra += item.manaregenerate_effect;
                    client.currentChar.skillEffectsExtra += item.skill_effect;

                    /* frequency + attack_range */
                    if(item.type == ItemType.WEAPON_1) {
                        //client.currentChar.attackRange += item.range;
                        int plainRange = item.range / 10; // actual value is times 10
                        client.currentChar.attackRange = ServerConfig.defaultWeaponRange + (plainRange * plainRange) - plainRange + 2;
                        client.currentChar.attackCharge = 5000 - ((item.frequency / 10) * 300);
                    }

                    client.currentChar.saveModelToDb();
                    client.currentChar.sendSilentUpdate();
                }
            }
        }
    }
}

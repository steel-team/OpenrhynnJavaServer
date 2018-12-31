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
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.enums.ItemType;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.models.CharacterModel;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameUserCharacterSelectRequest;
import com.steelteam.openrhynn.network.messages.helpers.CharacterMessageFill;
import com.steelteam.openrhynn.network.messages.server.GameBeltEntry;
import com.steelteam.openrhynn.network.messages.server.GameItemInventoryAdd;
import com.steelteam.openrhynn.network.messages.server.GameItemInventoryEnd;
import com.steelteam.openrhynn.network.messages.server.GameUserCharacterForList;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HandleGameUserCharacterSelectRequest {
    public HandleGameUserCharacterSelectRequest(ORClient client, ChannelHandlerContext ctx, GameUserCharacterSelectRequest message) {
        int charId = message.getCharId();
        if(charId == 0) {
            charId = client.characters.entrySet().iterator().next().getKey();
        }
        if(client.characters.containsKey(charId)) {
            //character login process
            client.currentChar = new Character();
            client.characterSelected = true;
            client.currentChar.connectedModel = client.characters.get(charId);
            client.currentChar.connectedClient = client;
            client.currentChar.fillFromModel();
            Character.characters.put(client.currentChar.objectId, client.currentChar);

            //load & send inventory
            Connection conn = null;
            try {
                conn = DataSource.getInstance().getConnection();

                Statement state = conn.createStatement();
                ResultSet resultSet = state.executeQuery("SELECT * FROM inventory WHERE char_id='" + charId + "'");

                while (resultSet.next()) {
                    Item item = new Item();
                    item.id = resultSet.getInt("id");
                    item.range = resultSet.getInt("range");
                    item.frequency = resultSet.getInt("frequency");
                    item.required_magic = resultSet.getInt("required_magic");
                    item.required_skill = resultSet.getInt("required_skill");
                    item.effect_duration = resultSet.getInt("effect_duration");
                    item.action_effect_1_data = resultSet.getString("action_effect_1_data");
                    item.manaregenerate_effect = resultSet.getInt("manaregenerate_effect");
                    item.healthregenerate_effect = resultSet.getInt("healthregenerate_effect");
                    item.magic_effect = resultSet.getInt("magic_effect");
                    item.skill_effect = resultSet.getInt("skill_effect");
                    item.damage_effect = resultSet.getInt("damage_effect");
                    item.defense_effect = resultSet.getInt("defense_effect");
                    item.attack_effect = resultSet.getInt("attack_effect");
                    item.mana_effect = resultSet.getInt("mana_effect");
                    item.health_effect = resultSet.getInt("health_effect");
                    item.price = resultSet.getInt("price");
                    item.max_units = resultSet.getInt("max_units");
                    item.units_sell = resultSet.getInt("units_sell");
                    item.units = resultSet.getInt("units");
                    item.can_drop = resultSet.getInt("can_drop");
                    item.can_sell = resultSet.getInt("can_sell");
                    item.description = resultSet.getString("description");
                    item.name = resultSet.getString("name");
                    item.graphics_y = resultSet.getInt("graphics_y");
                    item.graphics_x = resultSet.getInt("graphics_x");
                    item.set_id = resultSet.getInt("set_id");
                    item.tpl_id = resultSet.getInt("tpl_id");
                    item.usage_type = UsageType.fromInt(resultSet.getInt("usage_type"));
                    item.type = ItemType.fromInt(resultSet.getInt("type"));
                    item.premium = resultSet.getInt("premium");
                    item.graphics_id = resultSet.getInt("graphics_id");
                    item.available_status = resultSet.getString("available_status");
                    item.char_id = resultSet.getInt("char_id");
                    item.equiped = resultSet.getInt("equiped");
                    item.action_effect_1 = resultSet.getInt("action_effect_1");
                    item.belt = resultSet.getInt("belt");

                    if(item.equiped == 1 && item.type == ItemType.WEAPON_1) {
                        int plainRange = item.range / 10; // actual value is times 10
                        client.currentChar.attackRange = ServerConfig.defaultWeaponRange + (plainRange * plainRange) - plainRange + 2;
                        client.currentChar.attackCharge = 5000 - ((item.frequency / 10) * 300);
                    }

                    client.currentChar.inventory.put(item.id, item);

                    client.writeMessage(new GameItemInventoryAdd(item).getData());
                }

                resultSet.close();
                state.close();

                client.currentChar.sendSilentUpdate();

            } catch (Exception ex) { ex.printStackTrace(); } finally { try { conn.close(); } catch (Exception e) {} }

            client.writeMessage(new GameItemInventoryEnd().getData());
            //load & send belt
            for(Item it : client.currentChar.inventory.values()) {
                if(it.belt > -1) {
                    client.writeMessage(new GameBeltEntry(it.id, it.belt).getData());
                }
            }
        }
    }
}

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
import com.steelteam.openrhynn.enums.ResponseCode;
import com.steelteam.openrhynn.models.CharacterClassModel;
import com.steelteam.openrhynn.models.CharacterModel;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameUserCharacterCreateRequest;
import com.steelteam.openrhynn.network.messages.helpers.CharacterMessageFill;
import com.steelteam.openrhynn.network.messages.server.GameUserCharacterCreateResult;
import com.steelteam.openrhynn.network.messages.server.GameUserCharacterForList;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HandleGameUserCharacterCreateRequest {
    public HandleGameUserCharacterCreateRequest(ORClient client, ChannelHandlerContext ctx, GameUserCharacterCreateRequest message) {
        int classId = message.getClassId();
        String name = message.getDisplayName().toLowerCase();

        Connection conn = null;
        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");

        int charId = 0;

        boolean classFound = false;
        CharacterClassModel classModel = null;
        for(CharacterClassModel cl : ServerConfig.classes) {
            if(cl.classId == classId) {
                classModel = cl;
                classFound = true;
                break;
            }
        }

        if(!name.matches("^(?=[A-Za-z0-9])(?!.*._()-{2})[A-Za-z0-9._()\\\\-]{3,15}$")) {
            responseStr = client.getGL("error_char_name");
        } else if(!classFound) {
            responseStr = client.getGL("error_class_not_found");
        } else {
            try {
                conn = DataSource.getInstance().getConnection();

                Statement state = conn.createStatement();
                ResultSet resultSet = state.executeQuery("SELECT COUNT(id) FROM characters WHERE user_id='" + client.userId + "' AND state=0");
                if (resultSet.next()) {
                    //seems that credentials is right
                    //check if user is already online and kick if so
                    int count = resultSet.getInt(1);
                    if (count > ServerConfig.maxCharacters - 1) {
                        responseStr = client.getGL("too_many_characters");
                    } else {



                        PreparedStatement st = conn.prepareStatement("SELECT id FROM characters WHERE display_name=?");
                        st.setString(1, name);
                        ResultSet rs = st.executeQuery();
                        if(!rs.next()) {

                            PreparedStatement st2 = conn.prepareStatement("INSERT INTO characters (user_id, class_id, graphics_id, display_name, graphics_x, graphics_y, graphics_dim, health_base, health_effect_extra, health_current, mana_base, mana_effect_extra, mana_current, attack_base, attack_effect_extra, defense_base, defense_effect_extra, damage_base, damage_effect_extra, skill_base, skill_effect_extra, magic_base, magic_effect_extra, healthregenerate_base, healthregenerate_effect_extra, manaregenerate_base, manaregenerate_effect_extra) VALUES (" +
                                    "'" + client.userId + "'," +
                                    "'" + classModel.classId + "'," +
                                    "'" + classModel.graphicsId + "'," +
                                    "?," +
                                    "'" + classModel.graphicsX + "'," +
                                    "'" + classModel.graphicsY + "'," +
                                    "'" + classModel.graphicsDim + "'," +
                                    "'" + classModel.healthBase + "'," +
                                    "'" + classModel.healthModifier + "'," +
                                    "'" + classModel.healthBase + "'," +
                                    "'" + classModel.manaBase + "'," +
                                    "'" + classModel.manaModifier + "'," +
                                    "'" + classModel.manaBase + "'," +
                                    "'" + classModel.attackBase + "'," +
                                    "'" + classModel.attackModifier + "'," +
                                    "'" + classModel.defenseBase + "'," +
                                    "'" + classModel.defenseModifier + "'," +
                                    "'" + classModel.damageBase + "'," +
                                    "'" + classModel.damageModifier + "'," +
                                    "'" + classModel.skillBase + "'," +
                                    "'" + classModel.skillModifier + "'," +
                                    "'" + classModel.magicBase + "'," +
                                    "'" + classModel.magicModifier + "'," +
                                    "'" + classModel.healthregenerateBase + "'," +
                                    "'" + classModel.healthregenerateModifier + "'," +
                                    "'" + classModel.manaregenerateBase + "'," +
                                    "'" + classModel.manaregenerateModifier + "'" +
                                    ")", PreparedStatement.RETURN_GENERATED_KEYS);
                            st2.setString(1, name);
                            st2.executeUpdate();
                            ResultSet rs2 = st2.getGeneratedKeys();
                            if (rs2.next()){
                                charId = rs2.getInt(1);
                            }
                            rs2.close();
                            conn.commit();
                            st2.close();

                            responseCode = ResponseCode.OK;
                            responseStr = client.getGL("character_created");


                        } else {
                            responseStr = client.getGL("character_name_inuse");
                        }

                        rs.close();
                        st.close();

                    }
                } else {
                    responseStr = client.getGL("error_unknown");
                }


                resultSet.close();
                state.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }


        client.writeMessage(new GameUserCharacterCreateResult(responseCode, responseStr).getData());


        if(responseCode == ResponseCode.OK) {
            try {
                conn = DataSource.getInstance().getConnection();

                Statement state = conn.createStatement();
                ResultSet resultSet = state.executeQuery("SELECT * FROM characters WHERE id='" + charId + "' AND state=0");

                if (resultSet.next()) {
                    CharacterModel cm = CharacterMessageFill.fillFromDb(resultSet);
                    client.characters.put(cm.id, cm);

                    client.writeMessage(new GameUserCharacterForList(cm).getData());
                }

                resultSet.close();
                state.close();

            } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }
        }
    }
}

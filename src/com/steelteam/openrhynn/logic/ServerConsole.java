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
package com.steelteam.openrhynn.logic;

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.data.SharedVariables;
import com.steelteam.openrhynn.models.CharacterClassModel;
import com.steelteam.openrhynn.utilits.Encryption;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerConsole {
    public static void bind() {
        Console console = System.console();
        String s = "";
        while (!"exit".equals(s)) {
            s = console.readLine();
            switch (s) {
                case "gc":
                    System.gc();
                    break;
                case "massreg":
                    massRegTest();
                    break;
            }
        }
        SharedVariables.serverInstance.channelFuture.channel().closeFuture();
        SharedVariables.serverInstance.workerGroup.shutdownGracefully();
        SharedVariables.serverInstance.bossGroup.shutdownGracefully();

        /* system.exit, or wait future? */
    }


    private static void massRegTest() {
        for(int i = 0; i < 1001; i++) {
            //register user with name test + i, and character with name test + i
            Connection conn = null;
            try {
                conn = DataSource.getInstance().getConnection();


                //create user
                PreparedStatement st = conn.prepareStatement("INSERT INTO users (login, pass) VALUES (?, '" + Encryption.sha256("ptwg") + "')", PreparedStatement.RETURN_GENERATED_KEYS);
                st.setString(1, "test" + i);
                st.executeUpdate();
                ResultSet rs2 = st.getGeneratedKeys();
                int userId = 0;
                if (rs2.next()){
                    userId = rs2.getInt(1);
                }
                rs2.close();
                conn.commit();
                st.close();

                //create character


                CharacterClassModel classModel = ServerConfig.classes.get(0);
                PreparedStatement st2 = conn.prepareStatement("INSERT INTO characters (user_id, class_id, graphics_id, display_name, graphics_x, graphics_y, graphics_dim, health_base, health_effect_extra, health_current, mana_base, mana_effect_extra, mana_current, attack_base, attack_effect_extra, defense_base, defense_effect_extra, damage_base, damage_effect_extra, skill_base, skill_effect_extra, magic_base, magic_effect_extra, healthregenerate_base, healthregenerate_effect_extra, manaregenerate_base, manaregenerate_effect_extra) VALUES (" +
                        "'" + userId + "'," +
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
                st2.setString(1, "test" + i);
                st2.executeUpdate();
                conn.commit();
                st2.close();




            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }
}

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
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameUserRegisterRequest;
import com.steelteam.openrhynn.network.messages.server.GameUserCharacterClassForList;
import com.steelteam.openrhynn.network.messages.server.GameUserRegisterResult;
import com.steelteam.openrhynn.utilits.Encryption;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HandleGameUserRegisterRequest {
    public HandleGameUserRegisterRequest(ORClient client, ChannelHandlerContext ctx, GameUserRegisterRequest message) {

        String username = message.getUsername().toLowerCase();
        String password = Encryption.sha256(message.getPassword().toLowerCase());

        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");

        if(username.length() < 4) {
            responseStr = client.getGL("username_length_notenough");
        } else if(username.length() > 16) {
            responseStr = client.getGL("username_length_tobig");
        } else if(message.getPassword().length() < 4) {
            responseStr = client.getGL("password_length_notenough");
        } else if(!username.matches("^(?=[A-Za-z0-9])(?!.*[._()\\[\\]-]{2})[A-Za-z0-9._()\\[\\]-]{3,15}$")) {
            responseStr = client.getGL("username_invalid");
        } else {

            Connection conn = null;
            try {
                conn = DataSource.getInstance().getConnection();

                PreparedStatement state = conn.prepareStatement("SELECT id FROM users WHERE login=?");
                state.setString(1, username);
                ResultSet resultSet = state.executeQuery();
                if(!resultSet.next()) {
                    //seems that login is free
                    resultSet.close();
                    state.close();

                    PreparedStatement st = conn.prepareStatement("INSERT INTO users (login, pass) VALUES (?, '" + password + "')");
                    st.setString(1, username);
                    st.executeUpdate();
                    conn.commit();
                    st.close();

                    //send classes
                    for(CharacterClassModel mdl : ServerConfig.classes)
                        client.writeMessage(new GameUserCharacterClassForList(mdl).getData());

                    responseCode = ResponseCode.OK;
                    responseStr = client.getGL("registration_success");

                } else {
                    resultSet.close();
                    state.close();
                    responseStr = client.getGL("username_is_taken");
                }

            } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        }

        client.writeMessage(new GameUserRegisterResult(responseCode, responseStr).getData());
    }
}

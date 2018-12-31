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
import com.steelteam.openrhynn.enums.UserType;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameUserLoginRequest;
import com.steelteam.openrhynn.network.messages.server.GameUserLoginResult;
import com.steelteam.openrhynn.utilits.Encryption;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HandleGameUserLoginRequest {
    public HandleGameUserLoginRequest(ORClient client, ChannelHandlerContext ctx, GameUserLoginRequest message) {
        Connection conn = null;
        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");
        try {
            conn = DataSource.getInstance().getConnection();

            PreparedStatement state = conn.prepareStatement("SELECT id, email, type FROM users WHERE login=? AND pass='" + Encryption.sha256(message.getPassword().toLowerCase()) + "'");
            state.setString(1, message.getUsername().toLowerCase());
            ResultSet resultSet = state.executeQuery();
            if(resultSet.next()) {
                //seems that credentials is right
                //check if user is already online and kick if so
                int id = resultSet.getInt(1);
                client.email = resultSet.getString(2);
                UserType userType = UserType.fromInt(resultSet.getInt(3));
                if(userType != UserType.BANNED) {
                    if (ORClient.signedIn.containsKey(id)) {
                        ORClient oclient = ORClient.signedIn.get(id);
                        ORClient.signedIn.remove(id);
                        oclient.close(true);
                        responseStr = client.getGL("login_try_again");
                    } else {
                        //add user online hashmap
                        try {
                            client.userAuthenticated = true;
                            ORClient.signedIn.put(id, client);
                            client.userId = id;
                            client.userType = userType;
                            responseCode = ResponseCode.OK;
                        } catch (Exception ex) {
                            client.userAuthenticated = false;
                        }
                    }
                } else {
                    responseStr = client.getGL("banned");
                }

            } else {
                responseStr = client.getGL("error_credentials");
            }


            resultSet.close();
            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        client.writeMessage(new GameUserLoginResult(responseCode, client.userId, ServerConfig.messageEncodeNumber, responseStr).getData());
    }
}

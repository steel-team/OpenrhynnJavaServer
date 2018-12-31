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
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameUserCharacterCreatePermissionRequest;
import com.steelteam.openrhynn.network.messages.server.GameUserCharacterCreatePermissionResult;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HandleGameUserCharacterCreatePermissionRequest {
    public HandleGameUserCharacterCreatePermissionRequest(ORClient client, ChannelHandlerContext ctx, GameUserCharacterCreatePermissionRequest message) {
        Connection conn = null;
        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT COUNT(id) FROM characters WHERE user_id='" + client.userId + "' AND state=0");
            if(resultSet.next()) {
                //seems that credentials is right
                //check if user is already online and kick if so
                int count = resultSet.getInt(1);
                if(count > ServerConfig.maxCharacters - 1) {
                    responseStr = client.getGL("too_many_characters");
                } else {
                    responseCode = ResponseCode.OK;
                    responseStr = "ok";//client.getGL("character_created");
                }
            } else {
                responseStr = client.getGL("error_unknown");
            }


            resultSet.close();
            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        client.writeMessage(new GameUserCharacterCreatePermissionResult(responseCode, responseStr).getData());
    }
}

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
import com.steelteam.openrhynn.enums.ResponseCode;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameUserCharacterDeleteRequest;
import com.steelteam.openrhynn.network.messages.server.GameUserCharacterDeleteResult;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HandleGameUserCharacterDeleteRequest {
    public HandleGameUserCharacterDeleteRequest(ORClient client, ChannelHandlerContext ctx, GameUserCharacterDeleteRequest message) {
        Connection conn = null;
        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT id FROM characters WHERE id='" + message.getCharId() + "' AND user_id='" + client.userId + "'");
            if(resultSet.next()) {

                Statement st3 = conn.createStatement();
                st3.executeUpdate("UPDATE characters SET state=1, state_time=NOW() WHERE id='" + message.getCharId() + "'");
                conn.commit();
                st3.close();

                responseCode = ResponseCode.OK;

            } else {
                responseStr = client.getGL("error_char_notfound");
            }


            resultSet.close();
            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        client.writeMessage(new GameUserCharacterDeleteResult(responseCode, message.getCharId(), responseStr).getData());
    }
}

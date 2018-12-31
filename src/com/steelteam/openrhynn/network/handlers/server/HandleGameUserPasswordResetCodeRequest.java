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
import com.steelteam.openrhynn.network.messages.client.GameUserPasswordResetCodeRequest;
import com.steelteam.openrhynn.network.messages.server.GameUserPasswordResetCodeResult;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class HandleGameUserPasswordResetCodeRequest {
    private static Random rand = new Random();
    public HandleGameUserPasswordResetCodeRequest(ORClient client, ChannelHandlerContext ctx, GameUserPasswordResetCodeRequest message) {
        String username = message.getUsername().toLowerCase();
        Connection conn = null;
        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");
        try {
            conn = DataSource.getInstance().getConnection();

            PreparedStatement state = conn.prepareStatement("SELECT id FROM users WHERE login=?");
            state.setString(1, username);
            ResultSet resultSet = state.executeQuery();
            if(resultSet.next()) {
                int userId = resultSet.getInt(1);

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT code FROM recovery WHERE user_id='" + userId + "'");
                if(rs.next()) {
                    //TO-DO resend code
                } else {
                    //create code
                    Statement st2 = conn.createStatement();
                    st2.executeUpdate("INSERT INTO recovery (user_id, code) VALUES ('" + userId + "', '" + rand.nextInt(10000) + "')");
                    conn.commit();
                    st2.close();
                    //TO-DO send code
                }
                responseCode = ResponseCode.OK;
                rs.close();
                st.close();

            } else {
                responseStr = client.getGL("error_unknown");
            }


            resultSet.close();
            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        client.writeMessage(new GameUserPasswordResetCodeResult(responseCode, responseStr).getData());
    }
}

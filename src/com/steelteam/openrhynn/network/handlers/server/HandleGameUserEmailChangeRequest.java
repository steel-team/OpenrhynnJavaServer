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
import com.steelteam.openrhynn.network.messages.client.GameUserEmailChangeRequest;
import com.steelteam.openrhynn.network.messages.server.GameUserEmailChangeResult;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class HandleGameUserEmailChangeRequest {
    public HandleGameUserEmailChangeRequest(ORClient client, ChannelHandlerContext ctx, GameUserEmailChangeRequest message) {
        String email = (message.getEmailPart1() + "@" + message.getEmailPart2()).toLowerCase();

        ResponseCode responseCode = ResponseCode.ERROR;
        String responseStr = client.getGL("error_unknown");

        if(!"".equals(client.email)) {
            responseStr = client.getGL("email_already_set");
        } else if(!email.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")) {
            responseStr = client.getGL("email_not_match");
        } else {
            responseCode = ResponseCode.OK;
            responseStr = client.getGL("mail_ok");
            Connection conn = null;
            try {
                conn = DataSource.getInstance().getConnection();


                client.email = email;

                PreparedStatement st3 = conn.prepareStatement("UPDATE users SET email=? WHERE id='" + client.userId + "'");
                st3.setString(1, email);
                st3.executeUpdate();
                conn.commit();
                st3.close();

            } catch (Exception ex) { ex.printStackTrace(); } finally { try { conn.close(); } catch (Exception e) {} }
        }
        client.writeMessage(new GameUserEmailChangeResult(responseCode, responseStr).getData());
    }
}

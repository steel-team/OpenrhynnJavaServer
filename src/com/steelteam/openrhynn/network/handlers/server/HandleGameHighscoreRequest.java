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
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameCharacterHighscoreRequest;
import com.steelteam.openrhynn.network.messages.server.GameCharacterHighscoreListEntry;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HandleGameHighscoreRequest {
    public HandleGameHighscoreRequest(ORClient client, ChannelHandlerContext ctx, GameCharacterHighscoreRequest message) {
        int lowest = message.getLowest();
        int listLength = message.getListLength();

        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT display_name, exp FROM characters ORDER BY exp DESC LIMIT 0, " + listLength);

            int i = 0;
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                int exp = resultSet.getInt(2);

                client.writeMessage(new GameCharacterHighscoreListEntry(lowest, exp, i, listLength, name).getData());
                i++;
            }

            resultSet.close();
            state.close();

        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }
    }
}

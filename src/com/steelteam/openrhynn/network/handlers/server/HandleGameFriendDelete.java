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
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.models.Friend;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameFriendDelete;
import com.steelteam.openrhynn.network.messages.server.GameInfo;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.Statement;

public class HandleGameFriendDelete {
    public HandleGameFriendDelete(ORClient client, ChannelHandlerContext ctx, GameFriendDelete message) {
        int friendId = message.getObjectId();
        if(client.currentChar.friends.containsKey(friendId)) {
            Friend fr = client.currentChar.friends.get(friendId);
            if(fr.isConfirmed) {
                //notify other friend about delition
                if(Character.characters.containsKey(friendId)) {
                    Character.characters.get(friendId).connectedClient.writeMessage(new GameInfo(Character.characters.get(friendId).connectedClient.getGL("friend_deleted").replace("{0}", client.currentChar.name)).getData());
                    //to-do create remove message client/server side
                }
            }

            /* remove from DB */
            int lower = message.getObjectId();
            int higher = client.currentChar.objectId;
            if(lower > higher) {
                lower = higher;
                higher = message.getObjectId();
            }

            Connection conn = null;
            try {
                conn = DataSource.getInstance().getConnection();
                Statement state = conn.createStatement();
                state.executeUpdate("DELETE FROM friends WHERE lower_id='" + lower + "' AND higher_id='" + higher + "';");
                conn.commit();
                state.close();

            } catch (Exception ex) { ex.printStackTrace();} finally { try { conn.close(); } catch (Exception e) {} }

            client.currentChar.friends.remove(friendId);
            if(Character.characters.containsKey(friendId))
                Character.characters.get(friendId).friends.remove(client.currentChar.objectId);

        }
    }
}

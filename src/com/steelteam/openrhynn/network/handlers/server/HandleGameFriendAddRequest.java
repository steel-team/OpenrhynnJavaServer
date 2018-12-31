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
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.models.Friend;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameFriendAddRequest;
import com.steelteam.openrhynn.network.messages.server.GameFriendAddPlayer;
import com.steelteam.openrhynn.network.messages.server.GameInfo;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.Statement;

public class HandleGameFriendAddRequest {
    public HandleGameFriendAddRequest(ORClient client, ChannelHandlerContext ctx, GameFriendAddRequest message) {
        int friendId = message.getObjectId();
        if(Character.characters.containsKey(friendId)) {
            if(friendId == client.currentChar.objectId)
                return;
            int lowerId = friendId;
            int higherId = client.currentChar.objectId;
            if(lowerId > higherId) {
                lowerId = higherId;
                higherId = friendId;
            }

            if(client.currentChar.friends.size() >= ServerConfig.maxFriends) {
                client.writeMessage(new GameInfo(client.getGL("friend_max_error")).getData());
                return;
            }

            if(client.currentChar.friends.containsKey(friendId))
                client.writeMessage(new GameInfo(client.getGL("friend_add_error")).getData());
            else {
                Connection conn = null;
                try {
                    conn = DataSource.getInstance().getConnection();
                    Statement st = conn.createStatement();
                    st.executeUpdate("INSERT INTO friends (lower_id, higher_id, initiator, state) VALUES ('" + lowerId + "', '" + higherId + "', '" + client.currentChar.objectId + "', '0')");
                    conn.commit();
                    st.close();

                    //GAME_FRIEND_ADD_PLAYER true
                    Friend fr1 = new Friend();
                    fr1.friend_id = friendId;
                    fr1.frinend_name = Character.characters.get(friendId).name;
                    fr1.initiator = client.currentChar.objectId;
                    client.currentChar.friends.put(friendId, fr1);

                    Friend fr2 = new Friend();
                    fr2.friend_id = client.currentChar.objectId;
                    fr2.frinend_name = client.currentChar.name;
                    fr2.initiator = client.currentChar.objectId;
                    Character.characters.get(friendId).friends.put(client.currentChar.objectId, fr2);

                    Character.characters.get(friendId).connectedClient.writeMessage(new GameFriendAddPlayer(client.currentChar.objectId, client.currentChar.name).getData());
                    Character.characters.get(friendId).connectedClient.writeMessage(new GameInfo(Character.characters.get(friendId).connectedClient.getGL("friend_new_request")).getData());

                } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }
            }
        } else {
            client.writeMessage(new GameInfo(client.getGL("player_offline")).getData());
        }
    }
}

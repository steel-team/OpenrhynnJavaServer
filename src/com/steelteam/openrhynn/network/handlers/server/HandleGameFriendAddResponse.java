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
import com.steelteam.openrhynn.enums.FriendAddResponse;
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameFriendAddResponse;
import com.steelteam.openrhynn.network.messages.server.GameFriendAddtolist;
import com.steelteam.openrhynn.network.messages.server.GameInfo;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.Statement;

public class HandleGameFriendAddResponse {
    public HandleGameFriendAddResponse(ORClient client, ChannelHandlerContext ctx, GameFriendAddResponse message) {
        int friendId = message.getObjectId();
        FriendAddResponse response = message.getResponse();
        if(client.currentChar.friends.containsKey(friendId)) {

            int lowerId = friendId;
            int higherId = client.currentChar.objectId;
            if(lowerId > higherId) {
                lowerId = higherId;
                higherId = friendId;
            }

            Connection conn = null;
            switch (response) {
                case ACCEPTED: {

                    try {
                        conn = DataSource.getInstance().getConnection();
                        Statement st = conn.createStatement();
                        st.executeUpdate("UPDATE friends SET state=1 WHERE lower_id='" + lowerId + "' AND higher_id='" + higherId + "'");
                        conn.commit();
                        st.close();
                    } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

                    //GAME_FRIEND_ADDTOLIST
                    YesNo isOnline = YesNo.NO;
                    if(Character.characters.containsKey(friendId))
                        isOnline = YesNo.YES;

                    client.currentChar.friends.get(friendId).isConfirmed = true;

                    client.writeMessage(new GameFriendAddtolist(friendId, isOnline, client.currentChar.friends.get(friendId).frinend_name).getData());

                    if(Character.characters.containsKey(friendId)) {
                        Character.characters.get(friendId).friends.get(client.currentChar.objectId).isConfirmed = true;
                        Character.characters.get(friendId).connectedClient.writeMessage(new GameFriendAddtolist(client.currentChar.objectId, YesNo.YES, client.currentChar.name).getData());
                        Character.characters.get(friendId).connectedClient.writeMessage(new GameInfo(Character.characters.get(friendId).connectedClient.getGL("friend_request_accepted")).getData());
                    }
                    break;
                }
                case DECLINED: {

                    try {
                        conn = DataSource.getInstance().getConnection();
                        Statement st = conn.createStatement();
                        st.executeUpdate("DELETE FROM friends WHERE lower_id='" + lowerId + "' AND higher_id='" + higherId + "'");
                        conn.commit();
                        st.close();
                    } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

                    client.currentChar.friends.remove(friendId);

                    if(Character.characters.containsKey(friendId)) {
                        Character.characters.get(friendId).friends.remove(client.currentChar.objectId);
                        Character.characters.get(friendId).connectedClient.writeMessage(new GameInfo(Character.characters.get(friendId).connectedClient.getGL("friend_request_declined")).getData());
                    }
                    break;
                }
            }
        } else {
            //error?
        }
    }
}

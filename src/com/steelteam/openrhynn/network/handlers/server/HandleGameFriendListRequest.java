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
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.models.Friend;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameFriendListRequest;
import com.steelteam.openrhynn.network.messages.server.*;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HandleGameFriendListRequest {
    public HandleGameFriendListRequest(ORClient client, ChannelHandlerContext ctx, GameFriendListRequest message) {

        /* load friend list from DB */
        Connection conn = null;
        try {
            conn = DataSource.getInstance().getConnection();

            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT lower_id, higher_id, initiator, state FROM friends WHERE (lower_id='" + client.currentChar.objectId + "' OR higher_id='" + client.currentChar.objectId + "')");
            while (resultSet.next()) {
                Friend friend = new Friend();
                int friendId = 0;
                int lower = resultSet.getInt(1);
                int higher = resultSet.getInt(2);
                if(lower == client.currentChar.objectId)
                    friendId = higher;
                else
                    friendId = lower;

                friend.frinend_name = Friend.loadFriendName(friendId);
                friend.initiator = resultSet.getInt(3);
                friend.friend_id = friendId;
                if(resultSet.getInt(4) == 1)
                    friend.isConfirmed = true;
                else
                    friend.isConfirmed = false;

                client.currentChar.friends.put(friendId, friend);


            }

            /* send requested friends to my char */
            for(Friend fr : client.currentChar.friends.values()) {
                if(!fr.isConfirmed) {
                    if(fr.initiator != client.currentChar.objectId) {
                        client.writeMessage(new GameFriendAddPlayer(fr.friend_id, fr.frinend_name).getData());
                    }
                } else {
                        /* add to list */
                    YesNo isOnline = YesNo.NO;
                    if(Character.characters.containsKey(fr.friend_id)) {
                        //send online
                        Character.characters.get(fr.friend_id).connectedClient.writeMessage(new GameFriendStatus(client.currentChar.objectId, YesNo.YES).getData());
                        Character.characters.get(fr.friend_id).connectedClient.writeMessage(new GameInfo(Character.characters.get(fr.friend_id).connectedClient.getGL("friend_online").replace("{0}", client.currentChar.name)).getData());
                        isOnline = YesNo.YES;
                    }
                    client.writeMessage(new GameFriendAddtolist(fr.friend_id, isOnline, fr.frinend_name).getData());
                    /* send online */
                    //client.writeMessage(new GameFriendStatus(fr.friend_id, isOnline).getData());
                    /* went online message */
                }
            }



            resultSet.close();
            state.close();
        } catch (Exception ex) { } finally { try { conn.close(); } catch (Exception e) {} }

        client.writeMessage(new GameFriendListEnd().getData());

        /* inform friends about player status change(online) */
    }
}

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
package com.steelteam.openrhynn.network;

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.enums.ClientType;
import com.steelteam.openrhynn.enums.UserType;
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.CharacterModel;
import com.steelteam.openrhynn.models.Friend;
import com.steelteam.openrhynn.models.Language;
import com.steelteam.openrhynn.network.codec.NetTools;
import com.steelteam.openrhynn.network.messages.ORMessage;
import com.steelteam.openrhynn.network.messages.OutgoingPipe;
import com.steelteam.openrhynn.network.messages.server.GameFriendStatus;
import com.steelteam.openrhynn.network.messages.server.GameInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ORClient {

    public ORClient() {
        clients.add(this);
    }

    /* global storage */
    public static Map<Integer, ORClient> signedIn = new ConcurrentHashMap<>();
    public static Set<ORClient> clients = new ConcurrentSet<>();

    /* debug */
    public Random rand = new Random();
    public int random = 0;

    /* basic orclient data */
    public ClientType clientType = ClientType.UNKNOWN;
    public String language = null;
    private Language languageLink = null;
    public ChannelHandlerContext context = null;

    /* user data */
    public boolean userAuthenticated = false;
    public boolean characterSelected = false;
    public boolean worldFlagLocked = true;
    public int userId = 0;
    public String email = "";
    public UserType userType = UserType.UNKNOWN;
    public Character currentChar = null;
    public HashMap<Integer, CharacterModel> characters = new HashMap<>();
    public boolean busy = false;


    public boolean stopCommunication = false;


    /* communications */
    public ArrayList<ORMessage> messageQueue = new ArrayList<>();
    private int currentQueueSizeInBytes = 0;
    private final Object writeLock = new Object();
    private final Object flushLock = new Object();
    private final Object messageLock = new Object();
    public boolean flushLocked = false;

    public void writeMessage(ORMessage msg) {
        synchronized (writeLock) {
            int cMsgSize = msg.getRWIndex();
            if(msg.isLongMessage())
                cMsgSize += 2;
            if (currentQueueSizeInBytes + cMsgSize < ServerConfig.netBufferSize) {
                synchronized (messageLock) {
                    currentQueueSizeInBytes += cMsgSize;
                    messageQueue.add(msg);
                }
            } else {
                flushQueue();
                writeMessage(msg);
            }
        }
    }

    public void flushQueue() {
        synchronized (flushLock) {
            flushLocked = true;

            /* build outgoing pipe */
            OutgoingPipe pipe = new OutgoingPipe();
            ORMessage[] msgs;
            synchronized (messageLock) {
                msgs = messageQueue.toArray(new ORMessage[messageQueue.size()]);
                currentQueueSizeInBytes = 0;
                messageQueue.clear();
            }

            for (ORMessage msg : msgs) {
                pipe.addMessage(msg);
            }

            context.writeAndFlush(pipe);

            flushLocked = false;
        }
    }

    /* functions */
    public void close(boolean force) {
        if(force) {
            try {
                stopCommunication = true;
                context.close();
            } catch (Exception ex) {

            }
            return;
        }
        try {
            if(clients.contains(this))
                clients.remove(this);
        } catch (Exception ex) {

        }
        try {
            if(userAuthenticated) {
                if(signedIn.containsKey(userId))
                    signedIn.remove(userId);
            }
            if(characterSelected) {
                if(currentChar != null) {

                    currentChar.entityDisposed = true;

                    /* notify friends */
                    for(Friend fr : currentChar.friends.values()) {
                        if(fr.isConfirmed) {
                            if(Character.characters.containsKey(fr.friend_id)) {
                                Character.characters.get(fr.friend_id).connectedClient.writeMessage(new GameFriendStatus(currentChar.objectId, YesNo.NO).getData());
                                /* went offline message */
                                Character.characters.get(fr.friend_id).connectedClient.writeMessage(new GameInfo(Character.characters.get(fr.friend_id).connectedClient.getGL("friend_offline").replace("{0}", currentChar.name)).getData());
                            }
                        }
                    }

                    Character.characters.remove(currentChar.objectId);
                    //remove entity from world
                    currentChar.saveModelToDb();
                    if(!worldFlagLocked) {
                        //remove from world
                        World world = World.registeredWorlds.get(currentChar.connectedModel.worldId);
                        world.removeEntity(currentChar, false);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("must be fixed!");
            ex.printStackTrace();
        }

        //null everything
        userAuthenticated = false;
        characterSelected = false;
        worldFlagLocked = true;
        userId = 0;
        email = "";
        rand = null;
        languageLink = null;
        context = null;
        currentChar = null;
        characters = null;
    }

    /* fast accessors */
    public String getGL(String key) {
        if(languageLink == null)
            languageLink = ServerConfig.languages.get(this.language);
        return languageLink.getEntry(key);
    }
}

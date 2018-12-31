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
package com.steelteam.openrhynn.logic;

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.enums.UserType;
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.models.ItemTemplate;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.server.GameBlock;
import com.steelteam.openrhynn.network.messages.server.GameInfo;
import com.steelteam.openrhynn.network.messages.server.GameSpeed;

public class CommandProcessor {
    public static void handleCommand(ORClient client, String command) {
        String[] commandData = command.split(" ");
        /* common commands */
        switch (commandData[0]) {
            case "!u": {
                handleUnstuck(client.currentChar);
                break;
            }
        }
        /* gm commands */
        if(client.userType != UserType.GM)
            return;
        switch (commandData[0]) {
            case "!g": {
                int units = 1;
                Character chr = client.currentChar;
                if(commandData.length > 2)
                    units = Integer.parseInt(commandData[2]);
                if(commandData.length > 3) {
                    int chrId = Integer.parseInt(commandData[3]);
                    if(Character.characters.containsKey(chrId))
                        chr = Character.characters.get(chrId);
                }
                handleGive(chr, Integer.parseInt(commandData[1]), units);
                break;
            }
            case "!b": {
                handleBlocked(client.currentChar);
                break;
            }
            case "!m": {
                int speed = ServerConfig.defaultPlayerSpeed;
                if(commandData.length > 1)
                    speed = Integer.parseInt(commandData[1]);
                handleSpeed(client.currentChar, speed);
                break;
            }
            case "!c": {
                handleCoordinates(client.currentChar);
                break;
            }
            case "!w": {
                handleWorld(client.currentChar);
                break;
            }
            case "!t": {
                int x = -1;
                int y = -1;
                int desination = client.currentChar.connectedModel.worldId;
                if(commandData.length > 1)
                    desination = Integer.parseInt(commandData[1]);
                if(commandData.length == 3) {
                    x = Integer.parseInt(commandData[1]);
                    y = Integer.parseInt(commandData[2]);
                }
                if(commandData.length == 4) {
                    desination = Integer.parseInt(commandData[1]);
                    x = Integer.parseInt(commandData[2]);
                    y = Integer.parseInt(commandData[3]);
                }
                handleTeleport(client.currentChar, desination, x, y);
                break;
            }
            case "!ban": {
                break;
            }
            case "!p": {
                Character chr = null;
                int chrId = Integer.parseInt(commandData[1]);
                if(Character.characters.containsKey(chrId))
                    chr = Character.characters.get(chrId);
                if(chr != null) {
                    handlePrison(chr);
                }
                break;
            }
            case "!i": {
                handleInvincible(client.currentChar);
            }
        }
    }

    public static void handleInvincible(Character character) {
        character.invincible = !character.invincible;
    }

    public static void handlePrison(Character character) {
        character.teleport(ServerConfig.prisonWorldId, -1, -1);
    }

    public static void handleTeleport(Character character, int destinationId, int x, int y) {
        character.teleport(destinationId, x, y);
    }

    public static void handleUnstuck(Character character) {
        if(character.connectedClient.userType == UserType.GM || character.connectedModel.worldId != ServerConfig.prisonWorldId)
            character.teleport(ServerConfig.unstuckWorldId, -1, -1);
    }

    public static void handleGive(Character character, int item_id, int units) {
        if(ItemTemplate.itemTemplates.containsKey(item_id))
            character.addItem(ItemTemplate.itemTemplates.get(item_id), units);
    }

    public static void handleBlocked(Character character) {
        character.skipBlocked = !character.skipBlocked;
        character.connectedClient.writeMessage(new GameBlock(YesNo.fromBoolean(character.skipBlocked)).getData());
    }

    public static void handleSpeed(Character character, int speed) {
        character.movementSpeed = speed;
        character.connectedClient.writeMessage(new GameSpeed(speed).getData());
    }

    public static void handleCoordinates(Character character) {
        character.connectedClient.writeMessage(new GameInfo("X: " + character.x + ", Y: " + character.y).getData());
    }

    public static void handleWorld(Character character) {
        character.connectedClient.writeMessage(new GameInfo("World ID: " + character.connectedModel.worldId).getData());
    }
}

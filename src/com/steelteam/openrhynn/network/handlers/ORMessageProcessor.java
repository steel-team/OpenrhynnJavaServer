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
package com.steelteam.openrhynn.network.handlers;

import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.handlers.server.*;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;
import com.steelteam.openrhynn.network.messages.client.*;
import com.steelteam.openrhynn.network.messages.server.GameTriggerTP;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class ORMessageProcessor {
    public static void ProcessMessage(ORClient client, ChannelHandlerContext ctx, ORMessage message) {
        if(message.getMessageId() == ORMessageIDs.MSGID_GAME_PING) {
            new HandleGamePing(client, ctx, new GamePing(message));
            return;
        }

        if(!client.userAuthenticated) {
            switch (message.getMessageId()) {
                case ORMessageIDs.MSGID_GAME_SERVER_LIST_REQUEST:
                    new HandleGameServerListRequest(client, ctx, new GameServerListRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_VERSION_REQUEST:
                    new HandleGameVersionRequest(client, ctx, new GameVersionRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_PASSWORD_RESET_CODE_REQUEST:
                    new HandleGameUserPasswordResetCodeRequest(client, ctx, new GameUserPasswordResetCodeRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_PASSWORD_RESET_NEW_REQUEST:
                    new HandleGameUserPasswordResetNewRequest(client, ctx, new GameUserPasswordResetNewRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_REGISTER_REQUEST:
                    new HandleGameUserRegisterRequest(client, ctx, new GameUserRegisterRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_LOGIN_REQUEST:
                    new HandleGameUserLoginRequest(client, ctx, new GameUserLoginRequest(message));
                    break;
                default:
                    Logger.getGlobal().warning("Unknown message id: " + message.getMessageId() + "[" + client.random + "]");
                    break;
            }
        } else if(!client.characterSelected){
            switch (message.getMessageId()) {
                case ORMessageIDs.MSGID_GAME_USER_GET_EMAIL_REQUEST:
                    new HandleGameUserGetEmailRequest(client, ctx, new GameUserGetEmailRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_EMAIL_CHANGE_REQUEST:
                    new HandleGameUserEmailChangeRequest(client, ctx, new GameUserEmailChangeRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_CHALLENGENUMBER:
                    //no handler
                    break;
                case ORMessageIDs.MSGID_GAME_USER_CHARACTER_CREATE_PERMISSION_REQUEST:
                    new HandleGameUserCharacterCreatePermissionRequest(client, ctx, new GameUserCharacterCreatePermissionRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_GET_CHARACTERS_REQUEST:
                    new HandleGameUserGetCharactersRequest(client, ctx, new GameUserGetCharactersRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_CHARACTER_CREATE_REQUEST:
                    new HandleGameUserCharacterCreateRequest(client, ctx, new GameUserCharacterCreateRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_CHARACTER_DELETE_REQUEST:
                    new HandleGameUserCharacterDeleteRequest(client, ctx, new GameUserCharacterDeleteRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_USER_CHARACTER_SELECT_REQUEST:
                    new HandleGameUserCharacterSelectRequest(client, ctx, new GameUserCharacterSelectRequest(message));
                    break;
                default:
                    Logger.getGlobal().warning("Unknown message id: " + message.getMessageId() + "[" + client.random + "] [logged on]");
                    break;
            }
        } else {
            boolean notHandled = false;
            switch (message.getMessageId()) {
                case ORMessageIDs.MSGID_GAME_FRIEND_LIST_REQUEST:
                    new HandleGameFriendListRequest(client, ctx, new GameFriendListRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_PLAYFIELD_ENTER_WORLD_REQUEST:
                    new HandleGamePlayfieldEnterWorldRequest(client, ctx, new GamePlayfieldEnterWorldRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_CHARACTER_HIGHSCORE_REQUEST:
                    new HandleGameHighscoreRequest(client, ctx, new GameCharacterHighscoreRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_GRAPHICS_LOAD_REQUEST:
                    new HandleGameGraphicsLoadRequest(client, ctx, new GameGraphicsLoadRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_PLAYFIELD_LOAD_REQUEST:
                    new HandleGamePlayfieldLoadRequest(client, ctx, new GamePlayfieldLoadRequest(message));
                    break;
                case ORMessageIDs.MSGID_GAME_PLAYFIELD_ENTER_REQUEST:
                    new HandleGamePlayfieldEnterRequest(client, ctx, new GamePlayfieldEnterRequest(message));
                    break;
                default:
                    notHandled = true;
                    //Logger.getGlobal().warning("Unknown message id: " + message.getMessageId() + "[" + client.random + "] [selected char]");
                    break;
            }

            if(!client.worldFlagLocked && notHandled && !client.currentChar.dead) {
                switch (message.getMessageId()) {
                    case ORMessageIDs.MSGID_GAME_CHARACTER_MOVE:
                        new HandleGameCharacterMove(client, ctx, new GameCharacterMove(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_CHARACTER_CHAT_ALL_REQUEST:
                        new HandleGameCharacterChatAllRequest(client, ctx, new GameCharacterChatAllRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_CHARACTER_CHAT_REQUEST:
                        new HandleGameCharacterChatRequest(client, ctx, new GameCharacterChatRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_CHARACTER_ATTACK_REQUEST:
                        new HandleGameCharacterAttackRequest(client, ctx, new GameCharacterAttackRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_TRIGGERTP:
                        new HandleGameTriggerTP(client, ctx, new GameTriggerTP(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_CHARACTER_ATTRIBUTE_INCREASE:
                        new HandleGameCharacterAttributeIncrease(client, ctx, new GameCharacterAttributeIncrease(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_ITEM_PICKUP_REQUEST:
                        new HandleGameItemPickupRequest(client, ctx, new GameItemPickupRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_ITEM_DROP_REQUEST:
                        new HandleGameItemDropRequest(client, ctx, new GameItemDropRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_ITEM_EQUIP_REQUEST:
                        new HandleGameItemEquipRequest(client, ctx, new GameItemEquipRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_ITEM_UNEQUIP_REQUEST:
                        new HandleGameItemUnequipRequest(client,ctx, new GameItemUnequipRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_ITEM_USE_REQUEST:
                        new HandleGameItemUseRequest(client, ctx, new GameItemUseRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_ADDBELT:
                        new HandleGameBeltAdd(client, ctx, new GameBeltAdd(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_BELTREMOVE:
                        new HandleGameBeltRemove(client, ctx, new GameBeltRemove(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_TARGET:
                        new HandleGameTarget(client, ctx, new GameTarget(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_TRIGGERGROUND:
                        new HandleGameTriggerGround(client, ctx, new GameTriggerGround(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_HELP_REQUEST:
                        new HandleGameHelpRequest(client, ctx, new GameHelpRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_FRIEND_ADD_REQUEST:
                        new HandleGameFriendAddRequest(client, ctx, new GameFriendAddRequest(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_FRIEND_ADD_RESPONSE:
                        new HandleGameFriendAddResponse(client, ctx, new GameFriendAddResponse(message));
                        break;
                    case ORMessageIDs.MSGID_GAME_FRIEND_DELETE:
                        new HandleGameFriendDelete(client, ctx, new GameFriendDelete(message));
                        break;
                    default:
                        Logger.getGlobal().warning("Unknown message id: " + message.getMessageId() + "[" + client.random + "] [selected char]");
                        break;
                }
            } else if(!client.worldFlagLocked && notHandled) {
                if(message.getMessageId() == ORMessageIDs.MSGID_GAME_CHARACTER_RESPAWN_REQUEST)
                    new HandleGameCharacterRespawnRequest(client, ctx, new GameCharacterRespawnRequest(message));
            }
        }
    }
}

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

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.models.CharacterPointData;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameCharacterAttributeIncrease;
import com.steelteam.openrhynn.network.messages.server.GameCharacterUpdateAll2;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameCharacterAttributeIncrease {
    public HandleGameCharacterAttributeIncrease(ORClient client, ChannelHandlerContext ctx, GameCharacterAttributeIncrease message) {
        synchronized (client.currentChar.entityLock) {
            if(client.currentChar.connectedModel.levelPoints > 0)
                client.currentChar.connectedModel.levelPoints--;
            else
                return;
        }

        if(!ServerConfig.pointDatas.containsKey(client.currentChar.connectedModel.classId))
            return;

        CharacterPointData pointData = ServerConfig.pointDatas.get(client.currentChar.connectedModel.classId);

        switch (message.getAttribute()) {
            case HEALTH:
                client.currentChar.healthBase += pointData.health_base;
                break;
            case MANA:
                client.currentChar.manaBase += pointData.mana_base;
                break;
            case DAMAGE:
                client.currentChar.damageBase += pointData.damage_base;
                break;
            case ATTACK:
                client.currentChar.attackBase += pointData.attack_base;
                break;
            case DEFENSE:
                client.currentChar.defenseBase += pointData.defense_base;
                break;
            case SKILL:
                client.currentChar.skillBase += pointData.skill_base;
                break;
            case MAGIC:
                client.currentChar.magicBase += pointData.magic_base;
                break;
        }

        client.writeMessage(new GameCharacterUpdateAll2(
                client.currentChar.objectId,
                client.currentChar.connectedModel.classId,
                client.currentChar.clanId,
                client.currentChar.connectedModel.worldId,
                client.currentChar.graphicsId,
                client.currentChar.graphicsX,
                client.currentChar.graphicsY,
                client.currentChar.graphicsDim,
                client.currentChar.x,
                client.currentChar.y,
                client.currentChar.level,
                client.currentChar.connectedModel.levelPoints,
                client.currentChar.connectedModel.experience,
                client.currentChar.connectedModel.gold,

                client.currentChar.healthBase,
                client.currentChar.healthEffectsExtra,
                client.currentChar.healthCurrent,

                client.currentChar.manaBase,
                client.currentChar.manaEffectsExtra,
                client.currentChar.manaCurrent,

                client.currentChar.attackBase,
                client.currentChar.attackEffectsExtra,

                client.currentChar.defenseBase,
                client.currentChar.defenseEffectsExtra,

                client.currentChar.damageBase,
                client.currentChar.damageEffectsExtra,

                client.currentChar.skillBase,
                client.currentChar.skillEffectsExtra,

                client.currentChar.magicBase,
                client.currentChar.magicEffectsExtra,

                client.currentChar.connectedModel.healthregenerateBase,
                client.currentChar.connectedModel.healthregenerateEffectsExtra,

                client.currentChar.connectedModel.manaregenerateBase,
                client.currentChar.connectedModel.manaregenerateEffectsExtra,

                client.currentChar.name,
                client.currentChar.connectedModel.ownStatus
        ).getData());
    }
}

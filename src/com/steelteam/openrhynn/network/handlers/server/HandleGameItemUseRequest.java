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

import com.steelteam.openrhynn.enums.EffectType;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameItemUseRequest;
import com.steelteam.openrhynn.network.messages.server.GameCharacterIncreaseVitality;
import com.steelteam.openrhynn.network.messages.server.GameInfoOverlay;
import com.steelteam.openrhynn.network.messages.server.GameItuse;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameItemUseRequest {
    public HandleGameItemUseRequest(ORClient client, ChannelHandlerContext ctx, GameItemUseRequest message) {
        if(client.currentChar.inventory.containsKey(message.getObjectId())) {
            Item item = client.currentChar.inventory.get(message.getObjectId());
            if(item.units > 0 && item.usage_type == UsageType.USE) { /* check other conditions */
                /* update sell count */
                item.units_sell = 0;

                String[] effectData = item.action_effect_1_data.split(":"); //legacy format from C# server...
                boolean used = false;
                String errorMessage = null;


                switch (EffectType.fromInt(item.action_effect_1)) {
                    case NO_EFFECT:
                        /* item without effect? */
                        break;
                    case RESTORE_HEALTH: {
                        if(client.currentChar.getHealthMax() <= client.currentChar.healthCurrent) {
                            errorMessage = client.getGL("already_full_hp");
                        } else {
                            used = true;
                            client.currentChar.restoreHP(Integer.parseInt(effectData[0]));
                            client.writeMessage(new GameCharacterIncreaseVitality(client.currentChar.healthCurrent, client.currentChar.manaCurrent).getData());
                        }
                        break;
                    }
                    case RESTORE_MANA: {
                        if(client.currentChar.getManaMax() <= client.currentChar.manaCurrent) {
                            errorMessage = client.getGL("already_full_mp");
                        } else {
                            used = true;
                            client.currentChar.restoreMP(Integer.parseInt(effectData[0]));
                            client.writeMessage(new GameCharacterIncreaseVitality(client.currentChar.healthCurrent, client.currentChar.manaCurrent).getData());
                        }
                        break;
                    }
                    case SKILL_TARGET:
                        client.writeMessage(new GameItuse(item.action_effect_1, Integer.parseInt(effectData[0]), item.id).getData());
                        break;
                    case SKILL_AOE:
                        client.writeMessage(new GameItuse(item.action_effect_1, Integer.parseInt(effectData[0]), item.id).getData());
                        break;
                }

                if(used) {
                    client.currentChar.withdrawItem(item, 1, YesNo.NO);
                }

                if(errorMessage != null) {
                    client.writeMessage(new GameInfoOverlay(errorMessage).getData());
                }
            }
        }
    }
}

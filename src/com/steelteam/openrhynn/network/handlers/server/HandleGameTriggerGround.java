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

import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameTriggerGround;
import com.steelteam.openrhynn.network.messages.server.GameCharacterIncreaseVitality;
import com.steelteam.openrhynn.scrolls.FirewallScroll;
import com.steelteam.openrhynn.scrolls.MassAttackScroll;
import com.steelteam.openrhynn.scrolls.MassHealScroll;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameTriggerGround {
    public HandleGameTriggerGround(ORClient client, ChannelHandlerContext ctx, GameTriggerGround message) {
        if(client.currentChar.inventory.containsKey(message.getObjectId())) {
            Item item = client.currentChar.inventory.get(message.getObjectId());
            World world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            item.units_sell = 0;

            String[] effectData = item.action_effect_1_data.split(":");
            if (client.currentChar.manaCurrent >= item.mana_effect) {
                client.currentChar.manaCurrent += item.mana_effect;
                client.writeMessage(new GameCharacterIncreaseVitality(client.currentChar.healthCurrent, client.currentChar.manaCurrent).getData());

                client.currentChar.withdrawItem(item, 1, YesNo.NO);

                switch (effectData[0]) {
                    case "1": {
                        //firewall
                        FirewallScroll scroll = new FirewallScroll();

                        scroll.time = Integer.parseInt(effectData[1]);
                        scroll.cleanDamage = Integer.parseInt(effectData[2]);
                        scroll.flashTarget = 0;//flash

                        scroll.useAttackerIcon = 1;
                        scroll.icon = 4;//attack anim
                        scroll.useSpellVisuals = 0;
                        scroll.usevisual1 = 0;

                        scroll.cellX = message.getCell().x;
                        scroll.cellY = message.getCell().y;

                        scroll.attackerId = client.currentChar.objectId;
                        scroll.targetId = client.currentChar.objectId;

                        scroll.world = world;
                        scroll.validate();
                        scroll.tick();
                        break;
                    }
                    case "2": {
                        //mass attack
                        MassAttackScroll scroll = new MassAttackScroll();
                        scroll.sleepTime = Integer.parseInt(effectData[1]);
                        scroll.cleanDamage = Integer.parseInt(effectData[2]);
                        scroll.flashTarget = 0;//flash

                        scroll.useAttackerIcon = 1;
                        scroll.icon = 4;//attack anim
                        scroll.useSpellVisuals = 0;
                        scroll.usevisual1 = 0;

                        scroll.centerCellX = message.getCell().x;
                        scroll.centerCellY = message.getCell().y;

                        scroll.attackerId = client.currentChar.objectId;
                        scroll.targetId = client.currentChar.objectId;

                        scroll.world = world;
                        scroll.validate();
                        scroll.tick();
                        break;
                    }
                    case "3": {
                        //mass heal
                        MassHealScroll scroll = new MassHealScroll();
                        scroll.sleepTime = Integer.parseInt(effectData[1]);
                        scroll.cleanHeal = Integer.parseInt(effectData[2]);
                        scroll.flashTarget = 0;//flash

                        scroll.useAttackerIcon = 1;
                        scroll.icon = 3;//attack anim
                        scroll.useSpellVisuals = 0;
                        scroll.usevisual1 = 0;

                        scroll.centerCellX = message.getCell().x;
                        scroll.centerCellY = message.getCell().y;

                        scroll.attackerId = client.currentChar.objectId;
                        scroll.targetId = client.currentChar.objectId;

                        scroll.world = world;
                        scroll.validate();
                        scroll.tick();
                        break;
                    }
                }

            }

        }
    }
}

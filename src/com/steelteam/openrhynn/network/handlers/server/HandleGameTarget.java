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

import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameTarget;
import com.steelteam.openrhynn.network.messages.server.GameCharacterIncreaseVitality;
import com.steelteam.openrhynn.scrolls.*;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameTarget {
    public HandleGameTarget(ORClient client, ChannelHandlerContext ctx, GameTarget message) {
        if(client.currentChar.inventory.containsKey(message.getObjectId())) {
            Item item = client.currentChar.inventory.get(message.getObjectId());
            World world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            Entity target = world.getEntity(message.getTargetId());
            if(target != null && target.entityType != EntityType.NPC) {
                item.units_sell = 0;

                String[] effectData = item.action_effect_1_data.split(":");
                if(client.currentChar.manaCurrent >= item.mana_effect) {
                    client.currentChar.manaCurrent += item.mana_effect;
                    client.writeMessage(new GameCharacterIncreaseVitality(client.currentChar.healthCurrent, client.currentChar.manaCurrent).getData());

                    client.currentChar.withdrawItem(item, 1, YesNo.NO);

                    switch (effectData[1]) {
                        case "0": {
                            //blood scroll
                            BloodScroll scroll = new BloodScroll();

                            scroll.amount = Integer.parseInt(effectData[3]);

                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;
                            scroll.flashTarget = 1;//flash

                            scroll.useAttackerIcon = 1;
                            scroll.icon = 4;//attack anim

                            scroll.useSpellVisuals = 1;
                            scroll.spellVisualTime = 2;//2 sec
                            scroll.spellVisualcolor = 0;//0 - attack,1-heal

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();
                            break;
                        }
                        case "1": {
                            //morph scroll
                            MorphScroll scroll = new MorphScroll();
                            scroll.durability = Integer.parseInt(effectData[2]);
                            scroll.defenseDecreasePercent = Integer.parseInt(effectData[3]);
                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;
                            scroll.flashTarget = 1;//flash
                            scroll.useAttackerIcon = 1;
                            scroll.icon = 4;//attack anim
                            scroll.useSpellVisuals = 1;
                            scroll.spellVisualTime = 2;//2 sec
                            scroll.spellVisualcolor = 0;//0 - attack,1-heal

                            scroll.usevisual1 = 1;
                            scroll.spellVisualTime1 = scroll.durability;
                            scroll.spellVisualType1 = 5;//5-downgrade,6-upgrade

                            //SHEEP! :D
                            scroll.graphicsId = 100005;
                            scroll.graphicsDim = 20;
                            scroll.graphicsX = 0;
                            scroll.graphicsY = 100;

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();

                            break;
                        }
                        case "2": {
                            //heal scroll
                            HealScroll scroll = new HealScroll();
                            scroll.amount = Integer.parseInt(effectData[2]);
                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;
                            scroll.flashTarget = 0;//flash
                            scroll.useAttackerIcon = 1;
                            scroll.icon = 3;//attack anim
                            scroll.useSpellVisuals = 1;
                            scroll.spellVisualTime = 2;//2 sec
                            scroll.spellVisualcolor = 1;//0 - attack,1-heal

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();
                            break;
                        }
                        case "3": {
                            //bless scroll
                            BlessingScroll scroll = new BlessingScroll();
                            scroll.durability = Integer.parseInt(effectData[2]);
                            scroll.attackIncreasePercent = Integer.parseInt(effectData[3]);
                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;
                            scroll.flashTarget = 0;//flash
                            scroll.useAttackerIcon = 1;
                            scroll.icon = 3;//attack anim
                            scroll.useSpellVisuals = 1;
                            scroll.spellVisualTime = 2;//2 sec
                            scroll.spellVisualcolor = 1;//0 - attack,1-heal
                            scroll.usevisual1 = 1;
                            scroll.spellVisualTime1 = scroll.durability;
                            scroll.spellVisualType1 = 6;//5-downgrade,6-upgrade

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();
                            break;
                        }
                        case "4": {
                            //curse scroll
                            CurseScroll scroll = new CurseScroll();
                            scroll.durability = Integer.parseInt(effectData[2]);
                            scroll.defenseDecreasePercent = Integer.parseInt(effectData[3]);
                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;
                            scroll.flashTarget = 0;//flash
                            scroll.useAttackerIcon = 1;
                            scroll.icon = 4;//attack anim
                            scroll.useSpellVisuals = 1;
                            scroll.spellVisualTime = 2;//2 sec
                            scroll.spellVisualcolor = 0;//0 - attack,1-heal
                            scroll.usevisual1 = 1;
                            scroll.spellVisualTime1 = scroll.durability;
                            scroll.spellVisualType1 = 5;//5-downgrade,6-upgrade

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();
                            break;
                        }
                        case "5": {
                            //rage scroll
                            RageScroll scroll = new RageScroll();
                            scroll.durability = Integer.parseInt(effectData[2]);
                            scroll.damagePercent = Integer.parseInt(effectData[3]);

                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;

                            scroll.flashTarget = 1;//flash

                            scroll.useAttackerIcon = 1;
                            scroll.icon = 4;//attack anim

                            scroll.useSpellVisuals = 0;
                            //rs.spellVisualTime = 2;//2 sec
                            //rs.spellVisualcolor = 0;//0 - attack,1-heal
                            scroll.usevisual1 = 1;
                            scroll.spellVisualTime1 = scroll.durability;
                            scroll.spellVisualType1 = 5;//5-downgrade,6-upgrade

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();
                            break;
                        }
                        case "6": {
                            //haste scroll
                            HasteScroll scroll = new HasteScroll();
                            scroll.durability = Integer.parseInt(effectData[2]);
                            scroll.moveUpPercent = Integer.parseInt(effectData[3]);
                            scroll.targetId = message.getTargetId();
                            scroll.attackerId = client.currentChar.objectId;
                            scroll.flashTarget = 0;//flash
                            scroll.useAttackerIcon = 1;
                            scroll.icon = 3;//attack anim
                            scroll.useSpellVisuals = 1;
                            scroll.spellVisualTime = 2;//2 sec
                            scroll.spellVisualcolor = 1;//0 - attack,1-heal
                            scroll.usevisual1 = 1;
                            scroll.spellVisualTime1 = scroll.durability;
                            scroll.spellVisualType1 = 6;//5-downgrade,6-upgrade

                            scroll.world = world;
                            scroll.validate();
                            scroll.tick();
                            break;
                        }
                    }

                } else {
                    //not enough mana?
                }
            }
        }
    }
}

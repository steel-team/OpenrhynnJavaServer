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
import com.steelteam.openrhynn.models.ItemTemplate;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameItemDropRequest;
import com.steelteam.openrhynn.network.messages.server.GameInfoOverlay;
import com.steelteam.openrhynn.utilits.Time;
import io.netty.channel.ChannelHandlerContext;

public class HandleGameItemDropRequest {
    public HandleGameItemDropRequest(ORClient client, ChannelHandlerContext ctx, GameItemDropRequest message) {
        if(World.registeredWorlds.containsKey(client.currentChar.connectedModel.worldId) && client.currentChar.inventory.containsKey(message.getObjectId())) {
            World world = World.registeredWorlds.get(client.currentChar.connectedModel.worldId);
            Item item = client.currentChar.inventory.get(message.getObjectId());
            if(item.can_drop == 0) {
                client.writeMessage(new GameInfoOverlay(client.getGL("cant_drop")).getData());
                return;
            }
            if(item.units >= message.getUnits()) {

                client.currentChar.withdrawItem(item, message.getUnits(), YesNo.YES);

                Pickupable pickupable = new Pickupable();
                pickupable.present = true;
                pickupable.respawnDelay = 0;
                pickupable.pickupTime = 0;
                pickupable.units = message.getUnits();
                pickupable.usageType = item.usage_type;
                pickupable.graphicsY = item.graphics_y;
                pickupable.graphicsX = item.graphics_x;
                pickupable.graphicsId = item.graphics_id;
                pickupable.objectId = world.getPickupableId();
                pickupable.x = client.currentChar.x;
                pickupable.y = client.currentChar.y;

                ItemTemplate itpl = new ItemTemplate();
                itpl.id = item.tpl_id;
                itpl.type = item.type;
                itpl.set_id = item.set_id;
                itpl.graphics_id = item.graphics_id;
                itpl.graphics_x = item.graphics_x;
                itpl.graphics_y = item.graphics_y;
                itpl.name = item.name;
                itpl.description = item.description;
                itpl.available_status = item.available_status;
                itpl.can_sell = item.can_sell;
                itpl.can_drop = item.can_drop;
                itpl.max_units = item.max_units;
                itpl.price = item.price;
                itpl.health_effect = item.health_effect;
                itpl.mana_effect = item.mana_effect;
                itpl.attack_effect = item.attack_effect;
                itpl.defense_effect = item.defense_effect;
                itpl.damage_effect = item.damage_effect;
                itpl.skill_effect = item.skill_effect;
                itpl.magic_effect = item.magic_effect;
                itpl.healthregenerate_effect = item.healthregenerate_effect;
                itpl.manaregenerate_effect = item.manaregenerate_effect;
                itpl.action_effect_1 = item.action_effect_1;
                itpl.action_effect_1_data = item.action_effect_1_data;
                itpl.effect_duration = item.effect_duration;
                itpl.required_skill = item.required_skill;
                itpl.required_magic = item.required_magic;
                itpl.frequency = item.frequency;
                itpl.range = item.range;
                itpl.premium = item.premium;
                itpl.usage_type = item.usage_type;


                pickupable.autoRemoveSpawnTime = Time.getUnixTimeMillis();
                pickupable.itemTemplate = itpl;

                world.addPickupable(pickupable);
            }
        }
    }
}

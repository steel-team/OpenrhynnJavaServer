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
package com.steelteam.openrhynn.network.messages.server;

import com.steelteam.openrhynn.enums.ItemType;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameItemInventoryAdd {

    private ORMessage _message = null;

    /* custom start */
    private Item _item = null;
    /* custom end */

    public GameItemInventoryAdd(Item item) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _item = item;

        _message.writeInt4(item.id);
        _message.writeInt4(ItemType.toInt(item.type));
        _message.writeByte((byte) UsageType.toInt(item.usage_type));
        _message.writeInt4(item.set_id);
        _message.writeInt4(item.graphics_id);
        _message.writeInt2(item.graphics_x);
        _message.writeInt2(item.graphics_y);
        _message.writeByte((byte)item.premium);
        _message.writeByte((byte)item.can_sell);
        _message.writeByte((byte)item.can_drop);
        _message.writeInt2(item.units);
        _message.writeInt2(item.units_sell);
        _message.writeInt3(item.price);
        _message.writeByte((byte)item.equiped);

        _message.writeInt2(item.health_effect);
        _message.writeInt2(item.mana_effect);
        _message.writeInt2(item.attack_effect);
        _message.writeInt2(item.defense_effect);
        _message.writeInt2(item.damage_effect);
        _message.writeInt2(item.skill_effect);
        _message.writeInt2(item.magic_effect);
        _message.writeInt2(item.healthregenerate_effect);
        _message.writeInt2(item.manaregenerate_effect);
        _message.writeInt2(item.action_effect_1);
        _message.writeInt2(0);//action_effect_2
        _message.writeInt2(item.effect_duration);
        _message.writeInt2(item.required_skill);
        _message.writeInt2(item.required_magic);
        _message.writeByte((byte)item.frequency);
        _message.writeByte((byte)item.range);
        _message.writeString(item.name);
        _message.writeString(item.description);


        /* custom end */
    }

    public GameItemInventoryAdd(ORMessage message) {
        _message = message;

        /* custom start */
        _item = new Item();

        /* implement client-side? */

        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_ITEM_INVENTORY_ADD;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public Item getItem() {
        return _item;
    }

    /* message custom end */
}

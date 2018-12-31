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

import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.models.Pickupable;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameItemAdd {

    private ORMessage _message = null;

    /* custom start */
    private Pickupable _pickupable = null;

    /* custom end */

    public GameItemAdd(Pickupable pickupable) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _pickupable = pickupable;
        _message.writeInt4(_pickupable.objectId);
        _message.writeInt4(_pickupable.graphicsId);
        _message.writeInt2(_pickupable.graphicsX);
        _message.writeInt2(_pickupable.graphicsY);
        _message.writeByte((byte)UsageType.toInt(_pickupable.usageType));
        _message.writeInt2(_pickupable.x);
        _message.writeInt2(_pickupable.y);
        /* custom end */
    }

    public GameItemAdd(ORMessage message) {
        _message = message;
        /* custom start */
        _pickupable = new Pickupable();

        _pickupable.objectId = _message.readIntFrom4();
        _pickupable.graphicsId = _message.readIntFrom4();
        _pickupable.graphicsX = _message.readIntFrom2();
        _pickupable.graphicsY = _message.readIntFrom2();
        _pickupable.usageType = UsageType.fromInt(_message.readByte());
        _pickupable.x = _message.readIntFrom2();
        _pickupable.y = _message.readIntFrom2();

        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_ITEM_ADD;
    }

    public ORMessage getData() {
        return _message;
    }


    /* message custom */
    public Pickupable getPickupable() {
        return _pickupable;
    }

    /* message custom end */
}

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

import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameFirewall {

    private ORMessage _message = null;

    /* custom start */
    private int _objectId = 0;
    private int _cellX = 0;
    private int _cellY = 0;
    /* custom end */

    public GameFirewall(int objectId, int cellX, int cellY) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _objectId = objectId;
        _cellX = cellX;
        _cellY = cellY;

        _message.writeInt4(_objectId);
        _message.writeByte((byte)_cellX);
        _message.writeByte((byte)_cellY);

        byte f1 = 14;//time
        f1 = (byte)(f1 & 0x0F);
        byte f2 = 3;//what?
        f2 = (byte)((f2 & 0x03) << 4);
        byte f3 = 2;//what?
        f3 = (byte)((f3 & 0x03) << 6);

        _message.writeByte((byte)(f1 | f2 | f3));

        /* custom end */
    }

    public GameFirewall(ORMessage message) {
        _message = message;
        /* custom start */
        _objectId = message.readIntFrom4();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_FIREWALL;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getObjectId() {
        return _objectId;
    }

    /* message custom end */
}

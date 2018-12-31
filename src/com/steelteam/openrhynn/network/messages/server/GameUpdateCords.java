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

public class GameUpdateCords {

    private ORMessage _message = null;

    /* custom start */
    private int _x = 0;
    private int _y = 0;
    private int _direction = 0;
    /* custom end */

    public GameUpdateCords(int x, int y, int direction) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _x = x;
        _y = y;
        _direction = direction;

        _message.writeInt4(_x);
        _message.writeInt4(_y);
        _message.writeByte((byte)_direction);

        /* custom end */
    }

    public GameUpdateCords(ORMessage message) {
        _message = message;
        /* custom start */
        _x = message.readIntFrom4();
        _y = message.readIntFrom4();
        _direction = message.readByte();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_UPDATE_CORDS;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getX() {
        return _x;
    }
    public int getY() {
        return _y;
    }
    public int getDirection() {
        return _direction;
    }

    /* message custom end */
}

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

import com.steelteam.openrhynn.enums.YesNo;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameFriendAddtolist {

    private ORMessage _message = null;

    /* custom start */
    private int _targetId = 0;
    private YesNo _isOnline = YesNo.NO;
    private String _name = null;
    /* custom end */

    public GameFriendAddtolist(int targetId, YesNo isOnline, String name) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _targetId = targetId;
        _isOnline = isOnline;
        _name = name;

        _message.writeInt4(_targetId);
        _message.writeByte((byte)YesNo.toInt(_isOnline));
        _message.writeString(_name);

        /* custom end */
    }

    public GameFriendAddtolist(ORMessage message) {
        _message = message;
        /* custom start */
        _targetId = message.readIntFrom4();
        _isOnline = YesNo.fromInt(message.readByte());
        _name = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_FRIEND_ADDTOLIST;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getTargetId() {
        return _targetId;
    }
    public YesNo isOnline() {
        return _isOnline;
    }
    public String getName() {
        return _name;
    }

    /* message custom end */
}

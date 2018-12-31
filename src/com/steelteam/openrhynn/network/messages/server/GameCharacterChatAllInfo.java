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

public class GameCharacterChatAllInfo {

    private ORMessage _message = null;

    /* custom start */
    private int _objectId = 0;
    private String _msg = null;
    /* custom end */

    public GameCharacterChatAllInfo(int objectId, String msg) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _objectId = objectId;
        _msg = msg;

        _message.writeInt4(_objectId);
        _message.writeString(_msg);
        /* custom end */
    }

    public GameCharacterChatAllInfo(ORMessage message) {
        _message = message;
        /* custom start */
        _objectId = message.readIntFrom4();
        _msg = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_CHARACTER_CHAT_ALL_INFO;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getObjectId() {
        return _objectId;
    }

    public String getMsg() {
        return _msg;
    }

    /* message custom end */
}

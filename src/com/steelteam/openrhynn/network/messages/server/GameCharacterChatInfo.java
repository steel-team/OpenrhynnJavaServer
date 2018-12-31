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

public class GameCharacterChatInfo {

    private ORMessage _message = null;

    /* custom start */
    private int _partnerId = 0;
    private String _senderName = null;
    private String _msg = null;
    /* custom end */

    public GameCharacterChatInfo(int partnerId, String senderName, String msg) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _partnerId = partnerId;
        _senderName = senderName;
        _msg = msg;

        _message.writeInt4(_partnerId);
        _message.writeEmpty(4);
        _message.writeString(_senderName);
        _message.writeString(_msg);
        /* custom end */
    }

    public GameCharacterChatInfo(ORMessage message) {
        _message = message;
        /* custom start */
        _partnerId = message.readIntFrom4();
        message.readEmpty(4);
        _senderName = message.readString();
        _msg = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_CHARACTER_CHAT_INFO;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getPartnerId() {
        return _partnerId;
    }

    public String getSenderName() {
        return _senderName;
    }

    public String getMsg() {
        return _msg;
    }

    /* message custom end */
}

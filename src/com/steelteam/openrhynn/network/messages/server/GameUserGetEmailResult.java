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

public class GameUserGetEmailResult {

    private ORMessage _message = null;

    /* custom start */
    private String _emailPart1 = null;
    private String _emailPart2 = null;
    /* custom end */

    public GameUserGetEmailResult(String emailPart1, String emailPart2) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _emailPart1 = emailPart1;
        _emailPart2 = emailPart2;

        _message.writeString(_emailPart1);
        _message.writeString(_emailPart2);
        /* custom end */
    }

    public GameUserGetEmailResult(ORMessage message) {
        _message = message;
        /* custom start */
        _emailPart1 = message.readString();
        _emailPart2 = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_USER_GET_EMAIL_RESULT;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public String getEmailPart1() {
        return _emailPart1;
    }

    public String getEmailPart2() {
        return _emailPart2;
    }

    /* message custom end */
}

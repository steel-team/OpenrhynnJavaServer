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
package com.steelteam.openrhynn.network.messages.client;

import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameUserPasswordResetNewRequest {

    private ORMessage _message = null;

    /* custom start */
    private String _username = null;
    private String _resetCode = null;
    private String _newPassword = null;
    /* custom end */

    public GameUserPasswordResetNewRequest(String username, String resetCode, String newPassword) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _username = username;
        _resetCode = resetCode;
        _newPassword = newPassword;

        _message.writeString(_username);
        _message.writeString(_resetCode);
        _message.writeString(_newPassword);
        /* custom end */
    }

    public GameUserPasswordResetNewRequest(ORMessage message) {
        _message = message;
        /* custom start */
        _username = message.readString();
        _resetCode = message.readString();
        _newPassword = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_USER_PASSWORD_RESET_NEW_REQUEST;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public String getUsername() {
        return _username;
    }

    public String getResetCode() {
        return _resetCode;
    }

    public String getNewPassword() {
        return _newPassword;
    }

    /* message custom end */
}

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

import com.steelteam.openrhynn.enums.ClientType;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameVersionRequest {

    private ORMessage _message = null;

    /* custom start */
    private int _versionHigh = 0;
    private int _versionLow = 0;
    private int _versionLowSub = 0;
    private int _revision = 0;
    private ClientType _clientType = ClientType.UNKNOWN;
    private String _language = null;
    /* custom end */

    public GameVersionRequest(int versionHigh, int versionLow, int versionLowSub, int revision, ClientType clientType, String language) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _versionHigh = versionHigh;
        _versionLow = versionLow;
        _versionLowSub = versionLowSub;
        _revision = revision;
        _clientType = clientType;
        _language = language;

        _message.writeByte((byte)_versionHigh);
        _message.writeByte((byte)_versionLow);
        _message.writeByte((byte)_versionLowSub);
        _message.writeInt4(_revision);
        _message.writeByte((byte)ClientType.toInt(_clientType));
        _message.writeString(_language);
        /* custom end */
    }

    public GameVersionRequest(ORMessage message) {
        _message = message;
        /* custom start */
        _versionHigh = _message.readByte();
        _versionLow = _message.readByte();
        _versionLowSub = _message.readByte();
        _revision = _message.readIntFrom4();
        _clientType = ClientType.fromInt(_message.readByte());
        _language = _message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_VERSION_REQUEST;
    }

    public ORMessage getData() {
        return _message;
    }


    /* message custom */
    public int getVersionHigh() {
        return _versionHigh;
    }

    public int getVersionLow() {
        return _versionLow;
    }

    public int getVersionLowSub() {
        return _versionLow;
    }

    public int getRevision() {
        return _revision;
    }

    public ClientType getClientType () {
        return _clientType;
    }

    public String getLanguage() {
        return _language;
    }
    /* message custom end */
}

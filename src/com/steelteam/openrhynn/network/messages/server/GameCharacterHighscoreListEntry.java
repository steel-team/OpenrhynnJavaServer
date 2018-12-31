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

public class GameCharacterHighscoreListEntry {

    private ORMessage _message = null;

    /* custom start */
    private int _startRank = 0;
    private int _exp = 0;
    private int _i = 0;
    private int _listLength = 0;
    private String _name = null;
    /* custom end */

    public GameCharacterHighscoreListEntry(int startRank, int exp, int i, int listLength, String name) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _startRank = startRank;
        _exp = exp;
        _i = i;
        _listLength = listLength;
        _name = name;
        if(_exp > 999999)
        {
            _exp = 999999;
        }

        _message.writeInt4(_startRank + _i);
        _message.writeInt4(_exp);
        _message.writeByte((byte)_i);
        _message.writeByte((byte)_listLength);
        _message.writeString(_name);
        /* custom end */
    }

    public GameCharacterHighscoreListEntry(ORMessage message) {
        _message = message;
        /* custom start */
        _startRank = message.readIntFrom4();
        _exp = message.readIntFrom4();
        _i = message.readByte();
        _listLength = message.readByte();
        _name = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_CHARACTER_HIGHSCORE_LIST_ENTRY;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getStartRank() {
        return _startRank;
    }

    public int getExp() {
        return _exp;
    }

    public int getI() {
        return _i;
    }

    public int getListLength() {
        return _listLength;
    }

    public String getName() {
        return _name;
    }

    /* message custom end */
}

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

import com.steelteam.openrhynn.network.messages.ORMessage;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;


public class GamePlayfieldInfo {

    private ORMessage _message = null;

    /* custom start */
    private int _worldId = 0;
    private int _width = 0;
    private int _height = 0;
    private String _name = null;
    /* custom end */

    public GamePlayfieldInfo(int worldId, int width, int height, String name) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _worldId = worldId;
        _width = width;
        _height = height;
        _name = name;

        _message.writeInt4(_worldId);
        _message.writeInt2(_width);
        _message.writeInt2(_height);
        _message.writeString(_name);
        /* custom end */
    }

    public GamePlayfieldInfo(ORMessage message) {
        _message = message;
        /* custom start */
        _worldId = message.readIntFrom4();
        _width = message.readIntFrom2();
        _height = message.readIntFrom2();
        _name = message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_PLAYFIELD_INFO;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getWorldId() {
        return _worldId;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public String getName() {
        return _name;
    }

    /* message custom end */
}


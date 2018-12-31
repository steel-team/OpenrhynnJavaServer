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

public class GameMorph {

    private ORMessage _message = null;

    /* custom start */
    private int _targetId = 0;
    private int _graphicsId = 0;
    private int _graphicsDim = 0;
    private int _graphicsX = 0;
    private int _graphicsY = 0;
    /* custom end */

    public GameMorph(int targetId, int graphicsId, int graphicsDim, int graphicsX, int graphicsY) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _targetId = targetId;
        _graphicsId = graphicsId;
        _graphicsDim = graphicsDim;
        _graphicsX = graphicsX;
        _graphicsY = graphicsY;

        _message.writeInt4(_targetId);
        _message.writeInt4(_graphicsId);
        _message.writeByte((byte)_graphicsDim);
        _message.writeByte((byte)_graphicsX);
        _message.writeByte((byte)_graphicsY);

        /* custom end */
    }

    public GameMorph(ORMessage message) {
        _message = message;
        /* custom start */
        _targetId = message.readIntFrom4();
        _graphicsId = message.readIntFrom4();
        _graphicsDim = message.readByte();
        _graphicsX = message.readByte();
        _graphicsY = message.readByte();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_MORPH;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getTargetId() {
        return _targetId;
    }
    public int getGraphicsId() {
        return _graphicsId;
    }

    public int getGraphicsDim() {
        return _graphicsDim;
    }

    public int getGraphicsX() {
        return _graphicsX;
    }

    public int getGraphicsY() {
        return _graphicsY;
    }

    /* message custom end */
}

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


public class GamePlayfieldLoadChunk {

    private ORMessage _message = null;

    /* custom start */
    private int _totalSize = 0;
    private int _chunkNum = 0;
    private int _curChunkSize = 0;
    private byte[] _data = null;
    /* custom end */

    public GamePlayfieldLoadChunk(int totalSize, int chunkNum, int curChunkSize, byte[] data) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _totalSize = totalSize;
        _chunkNum = chunkNum;
        _curChunkSize = curChunkSize;
        _data = data;

        _message.writeInt2(_totalSize);
        _message.writeInt2(_chunkNum);
        _message.writeInt4(_curChunkSize);
        _message.writeBytes(data);

        /* custom end */
    }

    public GamePlayfieldLoadChunk(ORMessage message) {
        _message = message;
        /* custom start */
        _totalSize = message.readIntFrom2();
        _chunkNum = message.readIntFrom2();
        _curChunkSize = message.readIntFrom4();
        _data = message.readBytes(_curChunkSize);
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_PLAYFIELD_LOAD_CHUNK;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getTotalSize() {
        return _totalSize;
    }

    public int getChunkNum() {
        return _chunkNum;
    }

    public int getCurChunkSize() {
        return _curChunkSize;
    }

    public byte[] getIData() {
        return _data;
    }


    /* message custom end */
}


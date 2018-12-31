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
package com.steelteam.openrhynn.network.messages;

import com.steelteam.openrhynn.network.codec.NetTools;

import java.lang.reflect.Array;
import java.nio.charset.Charset;

public class ORMessage {
    private int _messageId = 0;
    private byte[] _rData = null;
    private int _readIndex = 0;
    private boolean _isLongMessage = false;

    public ORMessage(byte[] message) {
        _rData = message;
        _messageId = readIntFrom3();
    }

    public ORMessage(int messageId) {

        /* set long messages on write(read is done in decoder) */
        if(messageId == ORMessageIDs.MSGID_GAME_GRAPHICS_LOAD_CHUNK || messageId == ORMessageIDs.MSGID_GAME_PLAYFIELD_LOAD_CHUNK)
            _isLongMessage = true;

        _rData = new byte[1024];
        _readIndex = 1;
        writeInt3(messageId);
    }


    /* readers */
    public int readIntFrom3() {
        int r = NetTools.intFrom3Bytes(_rData[_readIndex], _rData[_readIndex + 1], _rData[_readIndex + 2]);
        _readIndex += 3;
        return r;
    }

    public int readIntFrom4() {
        int r = NetTools.intFrom4Bytes(_rData[_readIndex], _rData[_readIndex + 1], _rData[_readIndex + 2], _rData[_readIndex + 3]);
        _readIndex += 4;
        return r;
    }

    public byte readByte() {
        _readIndex++;
        return _rData[_readIndex - 1];
    }

    public String readString() {
        String r;
        int length = _rData[_readIndex];
        _readIndex++;
        r = new String(_rData, _readIndex, length, Charset.forName("UTF-8"));
        _readIndex += length;
        return r;
    }

    public String readLongString() {
        String r;
        int length = readIntFrom2();
        r = new String(_rData, _readIndex, length, Charset.forName("UTF-8"));
        _readIndex += length;
        return r;
    }

    public void readEmpty(int count) {
        _readIndex += count;
    }

    public int readIntFrom2() {
        int r = NetTools.intFrom2Bytes(_rData[_readIndex], _rData[_readIndex + 1]);
        _readIndex += 2;
        return r;
    }

    public byte[] readBytes(int length) {
        byte[] ret = new byte[length];
        System.arraycopy(_rData, _readIndex, ret, 0, length);
        _readIndex += length;
        return ret;
    }

    public int readUByte() {
        int r = NetTools.uintFrom1Byte(_rData[_readIndex]);
        _readIndex += 1;
        return r;
    }


    /* writers */
    public void writeInt3(int data) {
        NetTools.intTo3Bytes(data, _rData, _readIndex);
        _readIndex += 3;
    }

    public void writeString(String data) {
        byte[] str = data.getBytes(Charset.forName("UTF-8"));
        if(str.length > 0)
            System.arraycopy(str, 0, _rData, _readIndex + 1, str.length);
        _rData[_readIndex] = (byte)str.length;
        _readIndex += 1 + str.length;
    }

    public void writeLongString(String data) {
        byte[] str = data.getBytes(Charset.forName("UTF-8"));
        if(str.length > 0)
            System.arraycopy(str, 0, _rData, _readIndex + 2, str.length);
        writeInt2(str.length);
        _readIndex += str.length;
    }

    public void writeInt4(int data) {
        NetTools.intTo4Bytes(data, _rData, _readIndex);
        _readIndex += 4;
    }

    public void writeByte(byte data) {
        _rData[_readIndex] = data;
        _readIndex += 1;
    }

    public void writeEmpty(int count) {
        for(int i = _readIndex; i < _readIndex + count; i++)
            _rData[i] = 0x00;
        _readIndex += count;
    }

    public void writeInt2(int data) {
        NetTools.intTo2Bytes(data, _rData, _readIndex);
        _readIndex += 2;
    }

    public void writeBytes(byte[] data) {
        System.arraycopy(data, 0, _rData, _readIndex, data.length);
        _readIndex += data.length;
    }

    public void writeUByte(int data) {
        NetTools.intToUnsignedByte(data, _rData, _readIndex);
        _readIndex += 1;
    }


    /* getters */
    public int getMessageId() {
        return _messageId;
    }

    public int getRWIndex() {
        return _readIndex;
    }

    public boolean isLongMessage() { return _isLongMessage; }

    public byte[] getMessage() {
        return _rData;
    }
}

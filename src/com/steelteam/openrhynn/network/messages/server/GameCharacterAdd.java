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

import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameCharacterAdd {

    private ORMessage _message = null;

    /* custom start */
    private Entity _entity;
    /* custom end */

    public GameCharacterAdd(Entity entity) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _entity = entity;

        _message.writeInt4(_entity.objectId);
        _message.writeInt2(_entity.clanId);
        _message.writeInt4(_entity.graphicsId);
        _message.writeInt2(_entity.graphicsX);
        _message.writeInt2(_entity.graphicsY);
        _message.writeByte((byte)_entity.graphicsDim);
        _message.writeByte((byte)_entity.level);
        _message.writeInt2(_entity.x);
        _message.writeInt2(_entity.y);
        _message.writeByte((byte)_entity.direction);
        _message.writeInt2(_entity.healthCurrent);
        _message.writeInt2(_entity.getHealthMax());
        _message.writeString(_entity.name);
        /* custom end */
    }

    public GameCharacterAdd(ORMessage message) {
        _message = message;
        /* custom start */
        _entity = new Entity();
        _entity.objectId = _message.readIntFrom4();
        _entity.clanId = _message.readIntFrom2();
        _entity.graphicsId = _message.readIntFrom4();
        _entity.graphicsX = _message.readIntFrom2();
        _entity.graphicsY = _message.readIntFrom2();
        _entity.graphicsDim = _message.readByte();
        _entity.level = _message.readByte();
        _entity.x = _message.readIntFrom2();
        _entity.y = _message.readIntFrom2();
        _entity.direction = _message.readByte();
        _entity.healthCurrent = _message.readIntFrom2();
        _entity.healthBase = _message.readIntFrom2();//TO-DO
        _entity.name = _message.readString();
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_CHARACTER_ADD;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public Entity getEntity() {
        return _entity;
    }


    /* message custom end */
}

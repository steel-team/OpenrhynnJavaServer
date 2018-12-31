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
package com.steelteam.openrhynn.scripting;

import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.entities.Entity;
import com.steelteam.openrhynn.enums.EntityType;
import com.steelteam.openrhynn.network.messages.ORMessage;
import com.steelteam.openrhynn.utilits.AdvRandom;

public class ScriptBridge {

    public static AdvRandom sharedRandom = new AdvRandom();

    public static void sendMessageForEntity(Entity entity, ORMessage message) {
        if(entity.entityType == EntityType.CHARACTER) {
            Character chr = (Character)entity;
            chr.connectedClient.writeMessage(message);
        }
    }
}

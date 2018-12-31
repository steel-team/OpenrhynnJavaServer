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
package com.steelteam.openrhynn.network.handlers.server;

import com.steelteam.openrhynn.data.DataSource;
import com.steelteam.openrhynn.enums.UsageType;
import com.steelteam.openrhynn.models.Item;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.messages.client.GameBeltAdd;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.Statement;

public class HandleGameBeltAdd {
    public HandleGameBeltAdd(ORClient client, ChannelHandlerContext ctx, GameBeltAdd message) {
        if(client.currentChar.inventory.containsKey(message.getObjectId())) {
            Item item = client.currentChar.inventory.get(message.getObjectId());
            if (item.units > 0 && item.usage_type == UsageType.USE && message.getSlot() > -1 && message.getSlot() < 4) {
                Connection conn = null;
                try {
                    conn = DataSource.getInstance().getConnection();

                    /* set belt slot & remove previous belt slot*/
                    for(Item it : client.currentChar.inventory.values()) {
                        if(it.belt == message.getSlot()) {
                            it.belt = -1;
                            /* update */
                            Statement state = conn.createStatement();
                            state.executeUpdate("UPDATE inventory SET belt='" + it.belt + "' WHERE id='" + it.id + "'");
                            conn.commit();
                            state.close();
                            break;
                        }
                    }

                    item.belt = message.getSlot();
                    /* update */

                    Statement state = conn.createStatement();
                    state.executeUpdate("UPDATE inventory SET belt='" + item.belt + "' WHERE id='" + item.id + "'");
                    conn.commit();
                    state.close();


                } catch (Exception ex) { ex.printStackTrace();} finally { try { conn.close(); } catch (Exception e) {} }

            }
        }
    }
}

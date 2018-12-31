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

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.network.ORClient;
import com.steelteam.openrhynn.network.ORClientHandler;
import com.steelteam.openrhynn.network.messages.client.GameVersionRequest;
import com.steelteam.openrhynn.network.messages.server.GameVersion;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Logger;

public class HandleGameVersionRequest {
    public HandleGameVersionRequest(ORClient client, ChannelHandlerContext ctx, GameVersionRequest message) {
        if(message.getVersionHigh() != ServerConfig.versionHigh ||
                message.getVersionLow() != ServerConfig.versionLow ||
                message.getVersionLowSub() != ServerConfig.versionLowSub ||
                message.getRevision() != ServerConfig.revision) {
            Logger.getGlobal().warning("Unknown client version: " + message.getVersionHigh() + "." + message.getVersionLow() + "." + message.getVersionLowSub() + " (" + message.getRevision() + ")");
        }
        /* save basic client data: language, clientType, version?*/
        client.clientType = message.getClientType();
        client.language = message.getLanguage().toLowerCase();
        client.random = client.rand.nextInt();//for debug purposes
        /* check language */
        if(!ServerConfig.languages.containsKey(client.language))
            client.language = "english";

        /* send response */
        client.writeMessage(new GameVersion(ServerConfig.versionHigh, ServerConfig.versionLow, ServerConfig.versionLowSub, ServerConfig.revision).getData());
    }
}

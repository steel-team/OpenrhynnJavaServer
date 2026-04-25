/*
MIT License
-----------

Copyright (c) 2026 Ivan Yurkov (MB "Stylo tymas" http://steel-team.net)
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

package com.steelteam.openrhynn.network;

import java.util.ArrayList;

import org.json.JSONObject;
import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.logic.World;
import com.steelteam.openrhynn.models.CharApiEntry;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ORApiHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();

        // servers endpoint
        if ("/api/v1/servers".equals(uri)) {
            JSONObject version = new JSONObject();
            version.put("high", ServerConfig.versionHigh);
            version.put("low", ServerConfig.versionLow);
            version.put("lowSub", ServerConfig.versionLowSub);

            JSONObject versionFullObj = new JSONObject();
            versionFullObj.put("str", ServerConfig.version);
            versionFullObj.put("obj", version);

            JSONObject json = new JSONObject();
            json.put("status", "success");
            json.put("version", versionFullObj);
            json.put("servers", ServerConfig.servers);

            String jsonString = json.toString();

            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(jsonString, CharsetUtil.UTF_8));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            ctx.writeAndFlush(response);
            return;
        }

        // online
        if ("/api/v1/online".equals(uri)) {
            ArrayList<CharApiEntry> chars = new ArrayList<>();

            for (com.steelteam.openrhynn.entities.Character chr : com.steelteam.openrhynn.entities.Character.characters
                    .values()) {
                World world = World.registeredWorlds.get(chr.connectedModel.worldId);
                chars.add(
                        new CharApiEntry(chr.connectedModel.id, chr.connectedModel.clanId, chr.name,
                                chr.connectedModel.classId, chr.level,
                                chr.connectedClient.userType, chr.connectedModel.worldId,
                                world.name,
                                chr.getHealthMax(), chr.healthCurrent, chr.getManaMax(), chr.manaCurrent));
            }

            JSONObject json = new JSONObject();
            json.put("status", "success");
            json.put("online", com.steelteam.openrhynn.entities.Character.characters.size());
            json.put("characters", chars);

            String jsonString = json.toString();

            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(jsonString, CharsetUtil.UTF_8));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            ctx.writeAndFlush(response);
            return;
        }

        ctx.fireChannelRead(request.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
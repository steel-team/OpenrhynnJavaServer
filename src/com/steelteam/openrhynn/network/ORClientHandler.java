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
package com.steelteam.openrhynn.network;

import com.steelteam.openrhynn.network.handlers.ORMessageProcessor;
import com.steelteam.openrhynn.network.messages.ORMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.logging.Logger;


public class ORClientHandler extends ChannelInboundHandlerAdapter {

    private ORClient client = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        client = new ORClient();
        client.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Logger.getGlobal().info("Client disconnected " + ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
        if(client != null)
            client.close(false);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(client.stopCommunication)
            return;
        client.busy = true;
        ORMessage orMessage = (ORMessage)msg;
        ORMessageProcessor.ProcessMessage(client, ctx, orMessage);
        client.busy = false;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        /*ctx.flush();*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

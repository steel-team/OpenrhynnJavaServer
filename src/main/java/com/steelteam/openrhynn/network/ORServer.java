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

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.network.codec.ORMessageDecoder;
import com.steelteam.openrhynn.network.codec.ORMessageDecoderWebsocket;
import com.steelteam.openrhynn.network.codec.ORMessageEncoder;
import com.steelteam.openrhynn.network.codec.ORMessageEncoderWebsocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ORServer {
    private int tcpPort;
    private int wsPort;
    public ChannelFuture tcpChannelFuture = null;
    public ChannelFuture wsChannelFuture = null;
    public EventLoopGroup bossGroup = null;
    public EventLoopGroup workerGroup = null;

    public ORServer(int tcpPort, int wsPort) {
        this.tcpPort = tcpPort;
        this.wsPort = wsPort;
    }

    public void run() throws Exception {
        if (!Epoll.isAvailable() || ServerConfig.forceNio) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
        } else {
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
        }

        java.util.logging.Logger.getLogger("io.netty").setLevel(java.util.logging.Level.OFF);

        try {
            ServerBootstrap tcpBootstrap = new ServerBootstrap();
            tcpBootstrap.group(bossGroup, workerGroup);
            if (!Epoll.isAvailable() || ServerConfig.forceNio)
                tcpBootstrap.channel(NioServerSocketChannel.class);
            else
                tcpBootstrap.channel(EpollServerSocketChannel.class);

            tcpBootstrap.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast(new ORMessageEncoder());
                            p.addLast(new ORMessageDecoder());
                            p.addLast(new ORClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            tcpChannelFuture = tcpBootstrap.bind(tcpPort).sync();

            ServerBootstrap wsBootstrap = new ServerBootstrap();
            wsBootstrap.group(bossGroup, workerGroup);
            if (!Epoll.isAvailable() || ServerConfig.forceNio)
                wsBootstrap.channel(NioServerSocketChannel.class);
            else
                wsBootstrap.channel(EpollServerSocketChannel.class);

            wsBootstrap.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast(new HttpServerCodec());
                            p.addLast(new HttpObjectAggregator(65536));
                            p.addLast("realIp", new RealIpHandler());

                            p.addAfter("realIp", "api", new ORApiHandler());

                            p.addAfter("api", "wsHandler", new WebSocketServerProtocolHandler("/ws"));

                            p.addAfter("wsHandler", "wsClientHandler", new ORClientHandler());
                            p.addAfter("wsHandler", "wsEncoder", new ORMessageEncoderWebsocket());
                            p.addAfter("wsHandler", "wsDecoder", new ORMessageDecoderWebsocket());
                        }
                    })
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            wsChannelFuture = wsBootstrap.bind(wsPort).sync();

        } catch (Exception ex) {
            tcpChannelFuture.channel().closeFuture();
            wsChannelFuture.channel().closeFuture();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

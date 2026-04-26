package com.steelteam.openrhynn.network;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ClientIpUtil {
    private static final AttributeKey<String> CLIENT_IP_KEY = AttributeKey.valueOf("clientIp");

    public static String getClientIp(ChannelHandlerContext ctx) {
        String clientIp = ctx.channel().attr(CLIENT_IP_KEY).get();
        if (clientIp != null && !clientIp.isEmpty()) {
            return clientIp;
        }

        // Fallback to remote address
        if (ctx.channel().remoteAddress() instanceof InetSocketAddress) {
            return ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        }

        return "unknown";
    }
}
package com.steelteam.openrhynn.network;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.net.util.SubnetUtils;

public class RealIpHandler extends ChannelHandlerAdapter {

    private static final Map<String, SubnetUtils> subnetCache = new ConcurrentHashMap<>();

    private static final Set<String> TRUSTED_PROXIES = Set.of(
            "127.0.0.1",
            "10.0.0.0/8",
            "192.168.0.0/16");

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String originalClientIp = remoteAddress.getAddress().getHostAddress();

            if (isTrustedProxy(originalClientIp)) {
                String realClientIp = extractRealClientIp(request);
                System.out.println("got ip " + realClientIp);
                if (realClientIp != null) {
                    ctx.channel().attr(AttributeKey.valueOf("clientIp")).set(realClientIp);
                } else {
                    ctx.channel().attr(AttributeKey.valueOf("clientIp")).set(originalClientIp);
                }
            } else {
                System.out.println("non trusted proxy");
                ctx.channel().attr(AttributeKey.valueOf("clientIp")).set(originalClientIp);
            }
        }
        super.channelRead(ctx, msg);
    }

    private boolean isTrustedProxy(String ip) {
        for (String trusted : TRUSTED_PROXIES) {
            if (trusted.contains("/")) {
                if (isInCidr(ip, trusted)) {
                    return true;
                }
            } else if (trusted.equals(ip)) {
                return true;
            }
        }
        return false;
    }

    private String extractRealClientIp(HttpRequest request) {
        String forwardedFor = request.headers().get(X_FORWARDED_FOR).toString();
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            System.out.println(forwardedFor);
            String[] ips = forwardedFor.split(",");
            if (ips.length > 0) {
                String realIp = request.headers().get(X_REAL_IP).toString();
                if (realIp != null) {
                    System.out.println(realIp);
                }
                return ips[0].trim();
            }
        }

        String realIp = request.headers().get(X_REAL_IP).toString();
        if (realIp != null && !realIp.isEmpty()) {
            System.out.println("rap" + realIp);
            return realIp;
        }

        return null;
    }

    private boolean isInCidr(String ip, String cidr) {
        SubnetUtils subnet = subnetCache.computeIfAbsent(cidr, SubnetUtils::new);
        return subnet.getInfo().isInRange(ip);
    }
}
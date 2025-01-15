package com.pixeltalk.websocket;

import cn.dev33.satoken.stp.StpUtil;
import com.pixeltalk.domain.po.User;
import com.pixeltalk.redis.RedisComponent;
import io.netty.channel.Channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class HandlerWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

private final RedisComponent redisComponent;
private final ChannelContextUtils channelContextUtils;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接初始化
        log.info("有新的连接加入...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //连接断开
        log.info("连接断开...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //接收到消息
        Channel channel = channelHandlerContext.channel();
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        log.info("接收到消息：{}，来自：{}" ,textWebSocketFrame.text(), userId);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常处理
        log.error("异常处理...");
        cause.printStackTrace();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //用户事件触发
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            //握手成功
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = handshakeComplete.requestUri();
            String token=getToken(url);
            log.info("握手成功，token={}", token);
            if(token!=null && redisComponent.getUserInfoByToken(token)!=null){
                log.info("登录成功");
                User user=redisComponent.getUserInfoByToken(token);
                channelContextUtils.addContext(String.valueOf(user.getUserId()), ctx.channel());
            }else{
                log.info("登录失败");
                ctx.close();
            }
        }
        log.info("用户事件触发...");
    }

    private String getToken(String url) {
        if (url.contains("?token=")) {
            String[] parts = url.split("\\?token=");
            // 确保分割后数组的长度大于1
            if (parts.length > 1) {
                return parts[1];
            }
        }
        return null;
    }
}

package com.pixeltalk.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class HandlerHeartBeat extends ChannelDuplexHandler {
    @Autowired
    private ChannelHandlerContext channelHandlerContext;
    @Override
    public void userEventTriggered(final io.netty.channel.ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
                String userId = attribute.get();
                log.error("用户{}心跳超时", userId);
                ctx.close();
            }else if (e.state() == IdleState.READER_IDLE) {
                ctx.writeAndFlush("heart");
            }
            }
    }
}

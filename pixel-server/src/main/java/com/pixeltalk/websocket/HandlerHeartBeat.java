package com.pixeltalk.websocket;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class HandlerHeartBeat extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(final io.netty.channel.ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.error("心跳超时");
                ctx.close();
            }else if (e.state() == IdleState.READER_IDLE) {
                ctx.writeAndFlush("heart");
            }
            }
    }
}

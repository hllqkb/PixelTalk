package com.pixeltalk.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class NettyWebSocketStarter {
    private static EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
    private static EventLoopGroup workLoopGroup = new NioEventLoopGroup();

    @Resource
    private HandlerWebSocketHandler handlerWebSocketHandler;
    @PreDestroy
    public void close() {
        bossLoopGroup.shutdownGracefully();
        workLoopGroup.shutdownGracefully();
    }
    @Async
    public void startNetty() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossLoopGroup, workLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            //对http协议进行编解码
                            pipeline.addLast(new HttpServerCodec());
                            //对http消息进行聚合
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            //心跳检测
                            pipeline.addLast(new IdleStateHandler(6, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
                            //将http消息编码为websocket协议
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 , true,true,10000L));
                            //自定义的处理器
                            pipeline.addLast(handlerWebSocketHandler);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8081).syncUninterruptibly();
            log.info("启动netty websocket服务成功！");

            channelFuture.channel().closeFuture().syncUninterruptibly();
        } catch (Exception e) {
            log.error("启动netty websocket服务失败", e);
        } finally {
            bossLoopGroup.shutdownGracefully();
            workLoopGroup.shutdownGracefully();
        }
    }
}
package com.pixeltalk.websocket;
import com.pixeltalk.constant.MessageConstant;
import com.pixeltalk.domain.po.User;
import com.pixeltalk.mapper.UserMapper;
import com.pixeltalk.redis.RedisComponent;
import com.pixeltalk.service.IUserService;
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
    private final UserMapper userMapper;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接初始化
        log.info("有新的连接加入...");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 连接断开
        Channel channel = ctx.channel();
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf("userId"));
        String userId = null;
        if(attribute != null) {
            userId = attribute.get();
            if (userId != null) {
                redisComponent.deleteUserHeartBeat(Integer.valueOf(userId));
            }
        }
        // 清除用户上下文
        channelContextUtils.removeContext(userId);

        log.info("用户{}的连接断开...", userId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 接收到消息
        Channel channel = channelHandlerContext.channel();
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf("userId"));
        String userId = attribute.get();// 获取用户ID
        Integer status = userMapper.getIsActive(userId);

        if (userId != null) {
            log.info("接收到消息：{}，来自：{}", textWebSocketFrame.text(), userId);
            redisComponent.saveUserHeartBeat(userId);
            // 解析消息内容，判断是群发还是向特定群组发送
            String message = textWebSocketFrame.text();
            if (message.startsWith("/join ")) {
                String groupId = message.substring(6);
                channelContextUtils.removeUserAllGroup(userId);
                channelContextUtils.addUserToGroup(userId, groupId);
                channel.writeAndFlush(new TextWebSocketFrame("已加入群组：" + groupId+"当前人数：" + channelContextUtils.getGroupUserCount(groupId)));
            } else if (message.startsWith("/leave ")) {
                String groupId = message.substring(7);
                channelContextUtils.removeUserFromGroup(userId, groupId);
                channel.writeAndFlush(new TextWebSocketFrame("已退出群组：" + groupId));
            } else if (message.startsWith("/create ")) {
                if(status.equals(MessageConstant.ACCOUNT_INACTIVE_CODE)){
                    channel.writeAndFlush(new TextWebSocketFrame("账号已被禁用"));
                    return;
                }
                String groupId = message.substring(8);
                channelContextUtils.createGroup(groupId);
                channel.writeAndFlush(new TextWebSocketFrame("已创建群组：" + groupId));
            } else if (message.startsWith("/send ")) {
                if(status.equals(MessageConstant.ACCOUNT_INACTIVE_CODE)){
                    channel.writeAndFlush(new TextWebSocketFrame("账号已被禁用"));
                    return;
                }
               // 向特定群组发送消息
               String[] parts = message.split(" ", 2);
               if (parts.length == 2) {
                   String groupId = parts[0].substring(6);
                   String content = parts[1];
                   channelContextUtils.send2Group(groupId, content);
                   channel.writeAndFlush(new TextWebSocketFrame("已发送消息到群组：" + groupId));
               }else{
                   channel.writeAndFlush(new TextWebSocketFrame("消息格式错误，请按照/send groupId message的格式发送消息"));
               }

                }else if(message.startsWith("/status")){
                String statusStr = status == 1 ? "启用" : "封禁";
                channel.writeAndFlush(new TextWebSocketFrame("当前"+userId+"状态:"+statusStr+"\n当前加入群组："+channelContextUtils.getUserGroup(userId)
                +"\n当前人数："+channelContextUtils.getGroupUserCount(channelContextUtils.getUserGroup(userId))));
            }
            else {
                //没有指定群组
                //判断是否加入了群组
                String groupId = channelContextUtils.getUserGroup(userId);
                if (groupId != null) {
                    log.info("向群组{}发送消息：{}", groupId, message);
                    channelContextUtils.send2Group(groupId, message);
                } else {
                    //没有加入群组，发送到默认群组
                    channelContextUtils.send2Group(MessageConstant.DEFAULT_GROUP_ID, message);

                }
            }
            }
        }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常处理
        log.error("异常处理...");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 用户事件触发
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手成功
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = handshakeComplete.requestUri();
            String token = getToken(url);
            log.info("握手成功，token={}", token);
            if (token != null && redisComponent.getUserInfoByToken(token) != null) {
                log.info("登录成功");
                User user = redisComponent.getUserInfoByToken(token);
                String groupId = MessageConstant.DEFAULT_GROUP_ID;

                channelContextUtils.addContext(String.valueOf(user.getUserId()), ctx.channel());
                channelContextUtils.addUserToGroup(String.valueOf(user.getUserId()), groupId);
                Channel channel = ctx.channel();
                channel.writeAndFlush(new TextWebSocketFrame("已加入群组：" + groupId+"当前人数：" + channelContextUtils.getGroupUserCount(groupId)));

            } else {
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

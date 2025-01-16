package com.pixeltalk.websocket;

import com.pixeltalk.redis.RedisComponent;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author:hllqk
 * date:2025/1/15
 */
@Component
@Slf4j
public class ChannelContextUtils {
    private static final ConcurrentHashMap<String, ChannelGroup> groupContextMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Channel> userContextMap = new ConcurrentHashMap<>();

    @Resource
    private RedisComponent redisComponent;

    // 添加用户上下文
    public void addContext(String userId, Channel channel){
        String channelId = channel.id().toString();
        AttributeKey<String> attributeKey = AttributeKey.valueOf("userId");
        channel.attr(attributeKey).set(userId); // 将用户ID添加到通道属性
        userContextMap.put(userId, channel); // 将通道添加到用户上下文映射
        redisComponent.saveUserHeartBeat(userId);
    }
    // 从用户上下文移除用户
    public void removeContext(String userId){
        Channel channel = userContextMap.remove(userId);
        if(channel != null){
            AttributeKey<String> attributeKey = AttributeKey.valueOf("userId");
            channel.attr(attributeKey).set(null); // 从通道属性中移除用户ID
            redisComponent.saveUserHeartBeat(userId);
            // 从所有群组中移除用户
            for(ChannelGroup group : groupContextMap.values()){
                group.remove(channel);
            }
        }
    }

    // 创建新的群组
    public void createGroup(String groupId){
        if(!groupContextMap.containsKey(groupId)){
            ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            groupContextMap.put(groupId, channelGroup);
        }
    }

    // 将用户添加到指定群组
    public void addUserToGroup(String userId, String groupId){
        Channel channel = userContextMap.get(userId);
        if(channel != null){
            ChannelGroup channelGroup = groupContextMap.get(groupId);
            if(channelGroup == null){
                channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                groupContextMap.put(groupId, channelGroup);
            }
            channelGroup.add(channel);
        }
    }
    // 获取用户在哪个群组
    public String getUserGroup(String userId) {
        Channel channel = userContextMap.get(userId);
        if(channel != null){
            for(String groupId : groupContextMap.keySet()){
                ChannelGroup channelGroup = groupContextMap.get(groupId);
                if(channelGroup.contains(channel)){
                    return groupId;
                }
            }
        }
        return null;
    }

    // 将用户从指定群组中移除
    public void removeUserFromGroup(String userId, String groupId){
        Channel channel = userContextMap.get(userId);
        if(channel != null){
            ChannelGroup channelGroup = groupContextMap.get(groupId);
            if(channelGroup != null){
                channelGroup.remove(channel);
            }
        }
    }

    // 向指定群组发送消息
    public void send2Group(String groupId, String message){
        ChannelGroup channelGroup = groupContextMap.get(groupId);
        if(channelGroup != null){
            channelGroup.writeAndFlush(new TextWebSocketFrame(message));
        }else{
            log.error("群组[{}]不存在", groupId);
        }
    }

    // 获取指定群组的用户数量
    public String getGroupUserCount(String groupId) {
        ChannelGroup channelGroup = groupContextMap.get(groupId);
        if(channelGroup != null){
            return String.valueOf(channelGroup.size());
        }else{
            return "0";
        }
    }
// 移除用户所有群组
    public void removeUserAllGroup(String userId) {
        Channel channel = userContextMap.get(userId);
        if(channel != null){
            for(String groupId : groupContextMap.keySet()){
                ChannelGroup channelGroup = groupContextMap.get(groupId);
                if(channelGroup.contains(channel)){
                    channelGroup.remove(channel);
                }
            }
        }
    }
}

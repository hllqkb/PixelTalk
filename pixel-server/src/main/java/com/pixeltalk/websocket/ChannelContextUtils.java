package com.pixeltalk.websocket;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

/**
 * author:hllqk
 * date:2025/1/15
 */
@Component
public class ChannelContextUtils {
    public void addContext(String UserId, Channel channel){
        String channelId = channel.id().toString();
        AttributeKey attributekey=null;
        if(!AttributeKey.exists(channelId)){
            attributekey=AttributeKey.newInstance(channelId);
        }else{
            attributekey=AttributeKey.valueOf(channelId);
        }
        channel.attr(attributekey).set(UserId);//add UserId to channel attribute(添加用户ID到通道属性)
    }
}

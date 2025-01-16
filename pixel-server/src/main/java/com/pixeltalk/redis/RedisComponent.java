package com.pixeltalk.redis;

import com.alibaba.fastjson.JSON;
import com.pixeltalk.config.SkyProperties;
import com.pixeltalk.constant.MessageConstant;
import com.pixeltalk.domain.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

@Component
@Slf4j
public class RedisComponent {
    private Jedis jedis;

    @Autowired
    public RedisComponent(SkyProperties skyProperties) {
        // 初始化Redis连接
        this.jedis = new Jedis(skyProperties.getRedis().getHost(), skyProperties.getRedis().getPort());
        // 如果需要密码
        if (skyProperties.getRedis().getPassword() != null && !skyProperties.getRedis().getPassword().isEmpty()) {
            jedis.auth(skyProperties.getRedis().getPassword());
        }
    }
    //删除用户心跳
    public void deleteUserHeartBeat(Integer userId) {
        //异常处理
        try{
            jedis.del(MessageConstant.REDIS_KET_USER_HEARTBEAT+userId);

        }catch (JedisDataException e){
            log.error("Redis数据异常:{}", e.getMessage());
        }catch (JedisConnectionException e){
            log.error("Redis连接异常:{}", e.getMessage());
        }catch (Exception e){
            log.error("Redis操作异常:{}", e.getMessage());
        }
    }
    //保存用户心跳时间到redis
    public void saveUserHeartBeat(String userId) {
        // 将userId作为key，心跳时间作为value，设置过期时间为30天
        jedis.setex(MessageConstant.REDIS_KET_USER_HEARTBEAT+userId, MessageConstant.REDIS_EXPIRE_TIME_WS_TOKEN, String.valueOf(System.currentTimeMillis()));
    }
    public Long getUserHeartBeat(Integer userId) {
        // 根据userId从redis中获取心跳时间
        if(jedis.get(MessageConstant.REDIS_KET_USER_HEARTBEAT+userId)!= null) {
            return Long.parseLong(jedis.get(MessageConstant.REDIS_KET_USER_HEARTBEAT + userId));
        }
        return null;
    }
    //删除用户信息
    public void deleteTokenUserInfo(String token) {
        jedis.del(MessageConstant.REDIS_KET_WS_TOKEN+token);
    }
    //保存用户信息到redis
    public void saveTokenUserInfo(String token, User user) {
        // 将用户信息转换为字符串形式，可以使用JSON库进行转换
        String userInfo = JSON.toJSONString(user);

        // 将token作为key，用户信息作为value，设置过期时间为30天
        jedis.setex(MessageConstant.REDIS_KET_WS_TOKEN+token, MessageConstant.REDIS_EXPIRE_TIME_WS_TOKEN, userInfo);
//        // 将userId作为key，用户信息token作为value，设置过期时间为30天
//        jedis.setex(user.getUserId().toString(), MessageConstant.REDIS_EXPIRE_TIME_WS_TOKEN, token);

    }

    //从redis中获取用户信息
    public User getUserInfoByToken(String token) {
        // 根据token从redis中获取用户信息
        String userInfo = jedis.get(MessageConstant.REDIS_KET_WS_TOKEN+token);
        // 将用户信息转换为User对象
        User user = JSON.parseObject(userInfo, User.class);
        return user;
    }
    // 关闭Jedis连接
    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }
}

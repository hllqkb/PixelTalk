package com.pixeltalk.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pixeltalk.domain.po.Message;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 消息表 Mapper 接口
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
public interface MessageMapper extends BaseMapper<Message> {

    @Select("SELECT * " +
            "      FROM `message` " +
            "      WHERE (`from_id` = #{userId} AND `to_id` = #{targetId}) " +
            "         OR (`from_id` = #{targetId} AND `to_id` = #{userId}) " +
            "         OR (`source` = 'group' AND `to_id` = #{targetId}) " +
            "      ORDER BY `create_time` DESC LIMIT #{index}, #{num} ")
    @ResultMap("mybatis-plus_Message")
    List<Message> record(String userId, String targetId, int index, int num);


    @Select("SELECT username FROM user WHERE user_id = #{fromId}")
    String getUserInfo(String fromId);//发送者id


}

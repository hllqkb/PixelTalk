package com.pixeltalk.mapper;

import com.pixeltalk.domain.dto.MessageDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 消息表 Mapper 接口
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
public interface MessageMapper extends BaseMapper<MessageDto> {

    @Select("SELECT username FROM user WHERE user_id = #{fromId}")
    String getUserInfo(String fromId);//发送者id
}

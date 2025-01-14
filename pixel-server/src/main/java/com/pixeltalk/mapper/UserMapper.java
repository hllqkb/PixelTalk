package com.pixeltalk.mapper;

import com.pixeltalk.domain.dto.UserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pixeltalk.domain.po.User;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 多用户管理表 Mapper 接口
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
public interface UserMapper extends BaseMapper<UserDto> {

    @Select("SELECT COUNT(*)>0 FROM user WHERE username = #{username}")
    boolean getByUsername(String username);
    @Select("SELECT COUNT(*) > 0 FROM user WHERE email = #{email}")
    boolean getByEmail(String email);

    @Select("SELECT COUNT(*) > 0 FROM user WHERE phone = #{phone}")
    boolean getByPhone(String phone);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);
}

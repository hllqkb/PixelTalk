package com.pixeltalk.service;

import com.pixeltalk.domain.dto.UserDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pixeltalk.domain.po.User;

/**
 * <p>
 * 多用户管理表 服务类
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
public interface IUserService extends IService<User> {

    boolean getByUsername(String username);

    boolean getByEmail(String email);

    boolean getByPhone(String phone);

    User getUserByUsername(String username);

    Integer getIsActive(String userId);
}

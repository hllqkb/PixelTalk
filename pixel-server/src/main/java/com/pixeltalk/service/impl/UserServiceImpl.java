package com.pixeltalk.service.impl;

import com.pixeltalk.domain.dto.UserDto;
import com.pixeltalk.domain.po.User;
import com.pixeltalk.mapper.UserMapper;
import com.pixeltalk.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 多用户管理表 服务实现类
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDto> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    @Override
    public boolean getByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    @Override
    public boolean getByPhone(String phone) {
        return userMapper.getByPhone(phone);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}

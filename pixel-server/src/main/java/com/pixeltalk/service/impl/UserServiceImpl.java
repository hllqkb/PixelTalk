package com.pixeltalk.service.impl;

import com.pixeltalk.domain.po.User;
import com.pixeltalk.mapper.UserMapper;
import com.pixeltalk.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

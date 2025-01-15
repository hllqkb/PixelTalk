package com.pixeltalk.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.pixeltalk.constant.MessageConstant;
import com.pixeltalk.domain.dto.UserDto;
import com.pixeltalk.domain.dto.UserLoginDto;
import com.pixeltalk.domain.po.User;
import com.pixeltalk.encryption.Myencryption;
import com.pixeltalk.redis.RedisComponent;
import com.pixeltalk.result.Result;
import com.pixeltalk.service.IUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * <p>
 * 多用户管理表 前端控制器
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@RestController
@Api(tags = "用户管理接口")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final RedisComponent redisComponent;
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody UserDto user) {
        if(user==null){
            return Result.error(MessageConstant.USER_NOT_NULL);
        }
        //转dto为po (dto->po) po全，dto只存必要信息，vo展示信息
        User newuser= BeanUtil.copyProperties(user, User.class);
        if(userService.getByUsername(newuser.getUsername())){
            return Result.error(MessageConstant.USERNAME_EXIST);
        }
        if(userService.getByEmail(newuser.getEmail())){
            return Result.error(MessageConstant.EMAIL_EXIST);
        }
        if(userService.getByPhone(newuser.getPhone())){
            return Result.error(MessageConstant.PHONE_EXIST);
        }
        newuser.setPassword(Myencryption.encrypt(newuser.getPassword()));
        userService.save(newuser);
        return Result.success(MessageConstant.REGISTER_SUCCESS);
    }
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDto user) {
        if(user.getUsername()==null){
            return Result.error(MessageConstant.USERNAME_NOT_NULL);
        }
        if(user.getPassword()==null){
            return Result.error(MessageConstant.PASSWORD_NOT_NULL);
        }
        User getuser=userService.getUserByUsername(user.getUsername());
        if(getuser==null){
            return Result.error(MessageConstant.ACCOUNT_NOT_EXIST);
        }
        if(!getuser.getIsActive()){
            return Result.error(MessageConstant.ACCOUNT_INACTIVE);
        }
        //log.info("getpassword:{} ,userpassword:{} ",getuser.getPassword(),user.getPassword());
        if(!Myencryption.encrypt(user.getPassword()).equals(getuser.getPassword())){
            return Result.error(MessageConstant.PASSWORD_ERROR);
        }
        log.info("login success");
        StpUtil.setLoginId(getuser.getUserId());
        redisComponent.saveTokenUserInfo(StpUtil.getTokenValue(),getuser);
        return Result.success(MessageConstant.LOGIN_SUCCESS);
    }
    @PostMapping("/logout")
    public Result<String> logout() {
        log.info("logout success");
        if(StpUtil.isLogin()){
            StpUtil.logout();
        }else{
            return Result.error(MessageConstant.NOT_LOGIN);
        }
        return Result.success(MessageConstant.LOGOUT_SUCCESS);
    }
    @PostMapping("/isLogin")
    public Result<String> isLogin(){
        if(StpUtil.isLogin()){
            return Result.success(MessageConstant.LOGINED);
        }
        return Result.success(MessageConstant.NOT_LOGIN);
    }
    @PostMapping("/getToken")
    public Result<String> getToken() {
        String token = StpUtil.getTokenValue();
        return Result.success(token);
    }
    @PostMapping("/getUserInfo")
    public Result<UserDto> getUserInfo() {
            User user = (User) userService.getById((Serializable) StpUtil.getLoginId());
            UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
            return Result.success(userDto);
    }
    @PostMapping("/getUserByToken")
    public Result<User> getUserByToken() {
        String token = StpUtil.getTokenValue();
        return Result.success(redisComponent.getUserInfoByToken(token));
    }

}

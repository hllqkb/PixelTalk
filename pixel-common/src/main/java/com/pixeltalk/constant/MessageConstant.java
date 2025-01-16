package com.pixeltalk.constant;

/**
 * 信息提示常量类
 */
public class MessageConstant {

    public static final String PASSWORD_ERROR = "密码错误";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String USERNAME_EXIST = "用户名已存在";
    public static final String EMAIL_EXIST = "邮箱已存在";
    public static final String PHONE_EXIST = "手机号已存在";
    public static final String USER_NOT_NULL = "用户信息不能为空";
    public static final String PASSWORD_NOT_NULL = "密码不能为空";
    public static final String USERNAME_NOT_NULL = "用户名不能为空";
    public static final String ACCOUNT_NOT_EXIST = "账号不存在";
    public static final String ACCOUNT_INACTIVE = "账号被禁用，请联系管理员";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGOUT_SUCCESS = "登出成功";
    public static final String NOT_LOGIN = "未登录";
    public static final Object SEND_SUCCESS = "发送成功";
    public static final String LOGINED = "已登录";
    public static final String REDIS_KET_WS_TOKEN = "ws_token";
    public static final long REDIS_EXPIRE_TIME_WS_TOKEN = 2592000;// 30天
    public static final String UPDATE_SUCCESS = "更新成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final Integer ACCOUNT_INACTIVE_CODE = 0;
    public static final Integer ACCOUNT_ACTIVE_CODE = 1;
    public static final String NORMAL_USER = "普通用户";
    public static final String REDIS_KET_USER_HEARTBEAT = "user_heartbeat";
    public static final String USER_ALREADY_LOGIN = "用户已经登录";
    public static final String DEFAULT_GROUP_ID = "10086";
    public static final long HEARTBEAT_INTERVAL = 3600;// 1小时后断开连接
}

package com.pixeltalk.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 多用户管理表
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User对象", description="多用户管理表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号，唯一，可用于登录或接收验证码等")
    private String phone;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像URL")
    private String avatarUrl;

    @ApiModelProperty(value = "用户类型，默认为普通用户")
    private String type;

    @ApiModelProperty(value = "徽章，用于标识用户特殊身份或成就")
    private String badge;

    @ApiModelProperty(value = "最后一次登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "用户创建时间，默认为当前时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "用户信息更新时间，自动更新为当前时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否启用，1 表示启用，0 表示封禁，默认为启用")
    private Boolean isActive;


}

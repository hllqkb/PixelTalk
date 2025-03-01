package com.pixeltalk.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
public class UserUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "手机号格式不正确")
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
    @ApiModelProperty(value = "是否启用，1 表示启用，0 表示封禁，默认为启用")
    private Integer isActive;

}

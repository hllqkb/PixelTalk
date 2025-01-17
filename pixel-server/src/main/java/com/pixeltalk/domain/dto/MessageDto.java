package com.pixeltalk.domain.dto;

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

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message")
@ApiModel(value="Message对象", description="消息表")
public class MessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "发送账号不能为空")
    @ApiModelProperty(value = "发送者ID")
    private String fromId;

    @NotBlank(message = "接受账号不能为空")
    @ApiModelProperty(value = "接收者ID")
    private String toId;

    @NotBlank(message = "消息内容不能为空")
    @ApiModelProperty(value = "消息内容")
    private String message;

    @ApiModelProperty(value = "引用的消息ID")
    private String referenceMsg;

    @ApiModelProperty(value = "被@的用户ID")
    private String atUser;

    @ApiModelProperty(value = "是否显示时间戳")
    private Integer isShowTime;

    @NotBlank(message = "消息类型不能为空")
    @ApiModelProperty(value = "消息类型（文本、图片、视频等）")
    private String type;

    @ApiModelProperty(value = "消息来源（如群组ID）")
    private Integer source;

}

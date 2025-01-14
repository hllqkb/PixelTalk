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
 * 聊天群信息表
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_group")
@ApiModel(value="ChatGroup对象", description="聊天群信息表")
public class ChatGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "聊天群名称")
    private String name;

    @ApiModelProperty(value = "聊天群头像URL")
    private String avatar;

    @ApiModelProperty(value = "聊天群创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "聊天群更新时间")
    private LocalDateTime updateTime;


}

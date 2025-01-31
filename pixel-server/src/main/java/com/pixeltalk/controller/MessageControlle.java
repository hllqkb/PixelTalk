package com.pixeltalk.controller;


import cn.hutool.core.bean.BeanUtil;
import com.pixeltalk.constant.MessageConstant;
import com.pixeltalk.domain.dto.MessageDto;
import com.pixeltalk.domain.po.Message;
import com.pixeltalk.mapper.MessageMapper;
import com.pixeltalk.result.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 消息表 前端控制器
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@RestController
@Api(tags = "消息接口")
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageControlle  {
    private final MessageMapper messageMapper;
    @PostMapping("/send")
    public Object send(@Valid @RequestBody MessageDto messageDto) {
        Message message = BeanUtil.copyProperties(messageDto, Message.class);
        message.setFromInfo(messageMapper.getUserInfo(message.getFromId()));
        return Result.success(MessageConstant.SEND_SUCCESS);
    }
}

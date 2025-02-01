package com.pixeltalk.service.impl;

import com.pixeltalk.domain.dto.MessageDto;
import com.pixeltalk.domain.po.Message;
import com.pixeltalk.domain.vo.RecordVo;
import com.pixeltalk.mapper.MessageMapper;
import com.pixeltalk.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>  {

    private final MessageMapper messageMapper;
    public List<Message> record(String userId, RecordVo recordVo) {
        List<Message> messages=messageMapper.record(userId, recordVo.getTargetId(), recordVo.getIndex(), recordVo.getNum());
        return messages;

    }
}

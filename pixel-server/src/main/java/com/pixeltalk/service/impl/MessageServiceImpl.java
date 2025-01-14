package com.pixeltalk.service.impl;

import com.pixeltalk.domain.dto.MessageDto;
import com.pixeltalk.mapper.MessageMapper;
import com.pixeltalk.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息表 服务实现类
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDto> implements IMessageService {

}

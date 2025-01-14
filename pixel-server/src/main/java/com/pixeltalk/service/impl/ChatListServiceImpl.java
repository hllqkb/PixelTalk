package com.pixeltalk.service.impl;

import com.pixeltalk.domain.dto.ChatListDto;
import com.pixeltalk.mapper.ChatListMapper;
import com.pixeltalk.service.IChatListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天列表表 服务实现类
 * </p>
 *
 * @author hllqk
 * @since 2025-01-14
 */
@Service
public class ChatListServiceImpl extends ServiceImpl<ChatListMapper, ChatListDto> implements IChatListService {

}

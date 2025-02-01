package com.pixeltalk.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户访问限制统计数据对象
 */
@Data
public class UrlLimitStats {
    private int violationCount = 0;
    private boolean blocked = false;
    private LocalDateTime lastViolationTime;
}

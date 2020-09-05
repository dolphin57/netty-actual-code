/**
 * Copyright 2017 - 2025 Evergrande Group
 */
package io.dolphin.protocol.protocol.netty.struct;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: qianliang
 * @Since: 2018/10/15 11:11
 */
@Data
public final class Header {
    private int crcCode = 0xabef0101;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 会话id
     */
    private long sessionID;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 消息优先级
     */
    private byte priority;
    /**
     * 附件
     */
    private Map<String, Object> attachment = new HashMap<>();
}

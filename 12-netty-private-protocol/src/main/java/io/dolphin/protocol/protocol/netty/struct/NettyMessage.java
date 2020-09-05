/**
 * Copyright 2017 - 2025 Evergrande Group
 */
package io.dolphin.protocol.protocol.netty.struct;

import lombok.Data;

/**
 * @Author: qianliang
 * @Since: 2018/10/15 11:08
 * @Description: NettyMessage类定义
 */
@Data
public final class NettyMessage {
    /**
     * 消息头
     */
    private Header header;
    /**
     * 消息体
     */
    private Object body;
}

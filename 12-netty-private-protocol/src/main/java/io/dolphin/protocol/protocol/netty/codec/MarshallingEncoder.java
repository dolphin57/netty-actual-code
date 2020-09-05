/**
 * Copyright 2017 - 2025 Evergrande Group
 */
package io.dolphin.protocol.protocol.netty.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

/**
 * @Author: qianliang
 * @Since: 2018/10/15 11:43
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoder() {
        this.marshaller = MarshallingCodecFactory.buildMarshalling();;
    }

    public void encode(Object value, ByteBuf sendBuf) {

    }
}

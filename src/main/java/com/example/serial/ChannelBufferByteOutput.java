package com.example.serial;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * Created by yuanye on 2016/8/3.
 */
public class ChannelBufferByteOutput implements ByteOutput {
    @Getter
    private final ByteBuf buffer;

    public ChannelBufferByteOutput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public void write(int i) throws IOException {
        buffer.writeByte(i);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        buffer.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {
        buffer.writeBytes(bytes, i, i1);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}

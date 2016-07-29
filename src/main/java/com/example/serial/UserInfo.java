package com.example.serial;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by yuanye on 2016/7/28.
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private int userID;

    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo buildUserID(int userID) {
        this.userID = userID;
        return this;
    }

    public byte[] codeC() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}

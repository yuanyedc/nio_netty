package com.example.serial;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by yuanye on 2016/7/28.
 */
public class UserInfoTest {
    public static void main(String[] args) throws IOException{
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(info);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("====b:"+b.length);
        bos.close();
        System.out.println("====info:"+info.codeC().length);
    }
}

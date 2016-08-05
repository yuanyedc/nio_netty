package com.example.priproto;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanye on 2016/7/28.
 */
@ToString
@Data
public final class Header {
    private int crcCode = 0xabef0101;
    private int length;// 消息长度
    private long sessionID;// 会话ID
    private byte type;// 消息类型
    private byte priority;// 消息优先级
    private Map<String, Object> attachment = new HashMap<String, Object>(); // 附件
}

package com.example.priproto;

import lombok.AllArgsConstructor;

/**
 * Created by yuanye on 2016/7/29.
 */
@AllArgsConstructor
public enum MessageType {
    REQUEST(0, "请求"),
    RESPONSE(1, "响应"),
    ONE_WAY(2, "既是请求，又是响应"),
    HAND_REQ(3, "握手请求"),
    HAND_RES(4, "握手响应"),
    HEARTBEAT_REQ(5, "心跳请求"),
    HEARTBEAT_RES(6, "心跳响应");

    public int value;
    public String name;

}

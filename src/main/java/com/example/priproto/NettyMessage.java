package com.example.priproto;

import lombok.Data;
import lombok.ToString;

/**
 * Created by yuanye on 2016/7/28.
 */
@ToString
@Data
public final class NettyMessage {
    private Header header;
    private Object body;
}

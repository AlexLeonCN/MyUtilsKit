package org.alex.billstransfer.handler;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Data
public class StandardBillObt {
    private String date; // 日期
    private String time; // 时间
    private String price; // 金额
    private String tag; // 标签
    private String account; // 账户
    private String comment; // 备注
    private String type; // 分类
    private String subType; // 子分类

    public String[] toStringArr() {
        return new String[] {date, time, price, tag, account, comment, type, subType};
    }
}

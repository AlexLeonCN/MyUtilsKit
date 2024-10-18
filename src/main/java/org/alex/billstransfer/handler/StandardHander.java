package org.alex.billstransfer.handler;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StandardHander {
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Map<String, List<String>> typeTransferMap;
    private static final Map<String, String> subTypeTransferMap;

    static {
        Map<String, List<String>> ttMap = new HashMap<>();
        Map<String, String> stMap = new HashMap<>();

        List<String> trafficTypeList = new ArrayList<>();
        trafficTypeList.addAll(Arrays.asList(new String[]{"通行", "滴滴", "交通", "出行", "火车", "公交"}));
        ttMap.put("交通", trafficTypeList);
        stMap.put("通行", "高速费");
        stMap.put("滴滴", "打车");
        stMap.put("火车", "火车");
        stMap.put("公交", "公交");

        List<String> homeTypeList = new ArrayList<>();
        homeTypeList.addAll(Arrays.asList(new String[]{"充电", "电信","移动","联通","燃气","电网","App Store"}));
        ttMap.put("居家", homeTypeList);
        stMap.put("电信", "话费");
        stMap.put("移动", "话费");
        stMap.put("联通", "话费");
        stMap.put("充电", "水电燃气");
        stMap.put("燃气", "水电燃气");
        stMap.put("电网", "水电燃气");
        stMap.put("App Store", "软件续费");

        List<String> foodTypeList = new ArrayList<>();
        foodTypeList.addAll(Arrays.asList(new String[]{"餐饮", "美食", "膳", "食品"}));
        ttMap.put("餐饮", foodTypeList);
        stMap.put("餐饮", "早餐");
        stMap.put("美食", "晚餐");
        stMap.put("食品", "零食");
        stMap.put("膳", "午餐");

        List<String> goodTypeList = new ArrayList<>();
        goodTypeList.addAll(Arrays.asList(new String[]{"日用", "百货"}));
        ttMap.put("购物", goodTypeList);
        stMap.put("日用", "日用百货");
        stMap.put("百货", "日用百货");

        typeTransferMap = Collections.unmodifiableMap(ttMap);
        subTypeTransferMap = Collections.unmodifiableMap(stMap);
    }

    protected void setDateAndTime(String dateTime, StandardBillObt standardBillObt) {
        try {
            Date originDate = originFormat.parse(dateTime);
            standardBillObt.setDate(dateFormat.format(originDate));
            standardBillObt.setTime(timeFormat.format(originDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void handleType(String columData, StandardBillObt standardBillObt) {
        if (columData.contains("退款")) {
            standardBillObt.setType("退款");
        }
        if (columData.contains("亲情") || columData.contains("亲密") || columData.contains("亲友")) {
            standardBillObt.setTag("Ivy");
        }else {
            standardBillObt.setTag("Alex");
        }
        setTypeAndSubType(columData, standardBillObt);
    }

    protected static void handleReceiver(StandardBillObt standardBillObt, String columData, StringBuilder commentBuilder) {
        setTypeAndSubType(columData, standardBillObt);
        commentBuilder.append("交易对方: ")
                .append(columData)
                .append(System.lineSeparator());
    }

    protected static void handleGet(String columData, StandardBillObt standardBillObt) {
        if ("收入".equals(columData)) {
            standardBillObt.setType("红包");
            standardBillObt.setSubType("微信");
        }
    }

    protected static void handlerGoodsDesc(StandardBillObt standardBillObt, String columData, StringBuilder commentBuilder) {
        setTypeAndSubType(columData, standardBillObt);
        commentBuilder.append("商品: ")
                .append(columData)
                .append(System.lineSeparator());
    }

    protected static String handlePrice(String columData, StandardBillObt standardBillObt) {
        if (columData.contains("¥")) {
            columData = columData.replaceAll("¥", "");
        }
        standardBillObt.setPrice(columData);
        return columData;
    }

    protected static void handleComment(StringBuilder commentBuilder, String columData) {
        commentBuilder.append("备注: ")
                .append(columData)
                .append(System.lineSeparator());
    }

    protected static void setTypeAndSubType(String desc, StandardBillObt standardBillObt) {
        for (Map.Entry<String, List<String>> entry : typeTransferMap.entrySet()) {
            String typeStr = entry.getKey();
            List<String> valueList = entry.getValue();
            for (String value : valueList) {
                if (desc.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))) {
                    if (StringUtils.isBlank(standardBillObt.getType())) {
                        standardBillObt.setType(typeStr);
                    }
                    if (StringUtils.isBlank(standardBillObt.getSubType())) {
                        standardBillObt.setSubType(subTypeTransferMap.get(value));;
                    }
                    return;
                }
            }
        }
    }
}

package org.alex.billstransfer.handler;

import java.util.ArrayList;
import java.util.List;

public class AlipayBillsTransferHandler extends StandardHander{
    private static final int DATA_ROW_INDEX = 25;

    private static final int VALIDATE_COL_NUM = 12;

    private static final int DATE_TIME_COL_INDEX = 0; // 交易时间

    private static final int TRANS_TYPE_COL_INDEX = 1; // 交易分类

    private static final int TARGET_RECEIVER_COL_INDEX = 2; // 交易对方

    private static final int GOODS_COL_INDEX = 4; // 商品说明

    private static final int PAY_OR_GET_COL_INDEX = 5; // 收/支

    private static final int PRICE_COL_INDEX  = 6; // 金额(元)

    private static final int PAYWARD_WAY_COL_INDEX = 7; // 支付方式

    private static final int CURRENT_STATUS_COL_INDEX = 8; // 当前状态

    private static final int TRANS_ORDER_NO_COL_INDEX = 9; // 交易单号

    private static final int ACCOUNT_ORDER_NO_COL_INDEX = 10; // 商户单号

    private static final int COMMENT_COL_INDEX = 11; // 备注
    public List<StandardBillObt> handle(List<List<String>> csvData) {
        if (csvData.size() < DATA_ROW_INDEX + 1) {
            return new ArrayList<>();
        }
        List<StandardBillObt> standardBillObtList = new ArrayList<>();
        for (int i = DATA_ROW_INDEX; i < csvData.size(); i++) {
            List<String> columList = csvData.get(i);
            StandardBillObt standardBillObt = new StandardBillObt();
            StringBuilder commentBuilder = new StringBuilder();
            for (int j = 0; j < VALIDATE_COL_NUM; j++) {
                String columData = columList.get(j).trim();
                if (j == DATE_TIME_COL_INDEX) {
                    setDateAndTime(columData, standardBillObt);
                }
                if (j == TRANS_TYPE_COL_INDEX) {
                    handleType(columData, standardBillObt);
                }
                if (j == TARGET_RECEIVER_COL_INDEX) {
                    handleReceiver(standardBillObt, columData, commentBuilder);
                }
                if (j == PAY_OR_GET_COL_INDEX) {
                    handleGet(columData, standardBillObt);
                }
                if (j == GOODS_COL_INDEX) {
                    handlerGoodsDesc(standardBillObt, columData, commentBuilder);
                }
                if (j == PRICE_COL_INDEX) {
                    columData = handlePrice(columData, standardBillObt);
                }
                if (j == PAYWARD_WAY_COL_INDEX) {
                    standardBillObt.setAccount(columData);
                }
                if (j == COMMENT_COL_INDEX) {
                    handleComment(commentBuilder, columData);
                }
            }
            standardBillObt.setComment(commentBuilder.toString());
            if (standardBillObt.getType() == null) {
                standardBillObt.setType("购物");
            }
            standardBillObtList.add(standardBillObt);
        }
        return standardBillObtList;
    }
}

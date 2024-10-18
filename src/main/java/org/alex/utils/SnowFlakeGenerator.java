package org.alex.utils;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;

/**
* 整合雪花算法 (基于huTool工具类)
 */
@Slf4j
public class SnowFlakeGenerator {

    /**
     * 雪花算法对象
     */
    private static final Snowflake SNOW_FLAKE = IdUtil.getSnowflake(getWorkId(), getDataCenterId());


    /**
     * 获取雪花ID
     * @return ID
     */

    public static synchronized String nextId() {
        return SNOW_FLAKE.nextIdStr();
    }


    /**
     * 根据IP Address 生成workId
     * 工作机器ID(0~31)，2进制5位  32位减掉1位 31个
     */
    private static long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            return sums % 32;
        } catch (Exception e) {
            log.error("根据IP获取workId失败。", e);
            // 如果获取失败，则使用随机数备用
            return RandomUtils.nextLong(0, 31);
        }
    }


    /**
     * dataCenterId使用hostName生成
     * 数据中心ID(0~31)，2进制5位  32位减掉1位 31个
     */
    private static long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName();
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            for (int i: ints) {
                sums = sums + i;
            }
            return (sums % 32);
        } catch (Exception e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }

    public static void main(String[] args) {
        System.out.println(SnowFlakeGenerator.nextId());
    }
}
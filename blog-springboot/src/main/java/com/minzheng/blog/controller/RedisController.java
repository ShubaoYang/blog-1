package com.minzheng.blog.controller;

import com.minzheng.blog.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 用于redis功能测试
 */
@RestController
@RequestMapping("redis")
public class RedisController {
    public static final String SMS_PREFIX = "SMS_";
    public static final String TEST_PHONE = "13756204582_";

    @Autowired
    private RedisService redisService;

    @RequestMapping("verify")
    public String verifyCode(String code) {
        Object num = redisService.get(SMS_PREFIX + TEST_PHONE + "verify_num");
        // 5分钟允许校验三次验证码
        if (num != null && (int) num >= 3) {
            return "超过校验次数";
        }
        Object ver = redisService.get(SMS_PREFIX + TEST_PHONE);
        if (null != ver && ((String) ver).equals(code)) {
            redisService.del(SMS_PREFIX + TEST_PHONE + "verify_num");
            redisService.del(SMS_PREFIX + TEST_PHONE);
            return "验证成功";
        }
        if (null == num) {
            redisService.set(SMS_PREFIX + TEST_PHONE + "verify_num", 0, 5 * 60);
        }
        redisService.incr(SMS_PREFIX + TEST_PHONE + "verify_num", 1);
        return "验证失败";
    }

    /**
     * 生成六位数验证按
     *
     * @return 验证码
     */
    @RequestMapping("generate")
    public String generate() {
        Object num = redisService.get(SMS_PREFIX + TEST_PHONE + "generate_num");

        // 5分钟允许发送三次验证码
        if (num != null && (int) num >= 3) {
            return "超过发送次数";
        }
        // 生成并保存验证码
        String code = String.valueOf((int) (Math.random() * 9 + 1) * Math.pow(10, 5));
        redisService.set(SMS_PREFIX + TEST_PHONE, code, 5 * 60);

        // 发送次数+1
        if (null == num) {
            redisService.set(SMS_PREFIX + TEST_PHONE + "generate_num", 0, 5 * 60);
        }
        redisService.incr(SMS_PREFIX + TEST_PHONE + "generate_num", 1);

        return code;
    }

    // todo 活跃用户
    /**
     * 1. 某一天活跃用户
     * 2. 近n天活跃用户折线图
     *    近n个月活跃用户折线图
     *
     *    每天0点开始，把上一天的活跃用户合并到月活跃中。 user_login_2022-04-11 user_login_2022-04-1
     *    每月1号，把上个月的活跃用户合并到年活跃中。  user_login_2022-04 user_login__2022
     */

    /**
     * 用户登录
     *
     * @param id 用户id
     */
    public void login(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        String key = "user_login_" + today;
        redisService.bitAdd(key, id, true);
    }

    /**
     * 模拟每天凌晨，将活跃人数合并到当月和当年中
     */
    public void mergeLoginInfo() {
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");

        Calendar calendar = Calendar.getInstance();
        String thisYear = year.format(calendar.getTime());
        String thisMonth = month.format(calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        String yesterday = day.format(calendar.getTime());

        // 合并到本月
        redisService.bitOpOr("user_login_" + thisMonth, "user_login_" + thisMonth, "user_login_" + yesterday);

        // 合并到本年
        redisService.bitOpOr("user_login_" + thisYear, "user_login_" + thisYear, "user_login_" + yesterday);

        // 测试计算活跃人数
        Long monthActive = redisService.bitCount("user_login_" + thisMonth);
        Long yearActive = redisService.bitCount("user_login_" + thisYear);
        System.out.println(monthActive);
        System.out.println(yearActive);
    }

    // todo 排行榜
    public void putRank(String name) {
        redisService.zIncr("car", name, 1D);
    }

    /**
     * zverrank
     */
    public void getRank() {
        Map<Object, Double> cars = redisService.zReverseRangeWithScore("car", 0, 0);
        System.out.println(cars);
    }

    // todo 双写一致性


}

package com.mingzheng.blog.controller;


import com.minzheng.blog.BlogApplication;
import com.minzheng.blog.controller.RedisController;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class)
class RedisControllerTest {
    @Autowired
    private RedisController redisController;


    @Test
    void login() {
        int id = 5;
        redisController.login(id);
    }

    @Test
    void mergeLoginInfo() {
        redisController.mergeLoginInfo();
    }

    @Test
    void put() {
        redisController.putRank("现代");
        redisController.putRank("现代");
        redisController.putRank("金杯");
        redisController.putRank("金杯");
        redisController.putRank("金杯");
        redisController.putRank("奔驰");
        redisController.putRank("宝马");
    }
    @Test
    void getRank() {
        redisController.getRank();
    }
}
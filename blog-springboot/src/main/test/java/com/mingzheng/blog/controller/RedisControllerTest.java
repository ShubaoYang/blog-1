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
}
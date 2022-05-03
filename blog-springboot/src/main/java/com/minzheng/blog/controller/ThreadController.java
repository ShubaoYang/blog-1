package com.minzheng.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程模拟类
 */
@RestController
@RequestMapping("thread")
public class ThreadController {



    public void futureTest() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<FutureTask<Date>> futures = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            FutureTask<Date> futureTask = new FutureTask<>(() -> {
                System.out.println("z执行");
                Thread.sleep(1000);
                return new Date();
            });
            executor.execute(futureTask);
            futures.add(futureTask);
        }

        futures.forEach(future->{
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}


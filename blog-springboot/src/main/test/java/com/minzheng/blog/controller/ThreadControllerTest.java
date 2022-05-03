package com.minzheng.blog.controller;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ThreadControllerTest {

    @Test
    public void futureTest() throws ExecutionException, InterruptedException {
        new ThreadController().futureTest();
    }
}
package com.example.cs310_hw2;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsApplication extends Application {

    public ExecutorService srv = Executors.newCachedThreadPool();
}

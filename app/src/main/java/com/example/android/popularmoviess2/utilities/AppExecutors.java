package com.example.android.popularmoviess2.utilities;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    //for singleton instantiation of the object
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;

    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO,Executor networkIO,Executor mainThread){
        this.diskIO=diskIO;
        this.networkIO=networkIO;
        this.mainThread=mainThread;
    }

    public static AppExecutors getInstance(){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    private static class MainThreadExecutor implements Executor{
        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }

    public Executor getDiskIO(){return diskIO;}
    public Executor getNetworkIO(){return networkIO;}
    public Executor getMainThread(){return mainThread;}
}

package com.uncle.Util;

/**
 * Created by hakonzhao on 2018/3/5.
 */

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * 线程管理器，将任务抛给UI主线程或者worker线程
 *
 */
public class SHandlerThread {

    private static HandlerThread thread = null;
    private static Handler workerHandler = null;
    private static Handler mainHandler = null;

    private static void initWorkerHandler() {
        if (thread == null) {
            thread = new HandlerThread("SHandlerThread", android.os.Process.THREAD_PRIORITY_DEFAULT);
            thread.start();
            workerHandler = new Handler(thread.getLooper());
        }
    }

    private static Handler getWorkerHandler() {
        if (workerHandler == null) {
            initWorkerHandler();
        }
        return workerHandler;
    }

    private static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    /**
     * 将任务抛给子线程执行
     * @param r 任务
     */
    public static void postToWorker(final Runnable r) {
        if (r == null) {
            return;
        }
        getWorkerHandler().post(r);
        return;
    }

    /**
     * 将任务抛给子线程延时执行
     * @param r  任务
     * @param delayMillis  延时多少毫秒执行
     */
    public static void postToWorkerDelayed(final Runnable r, long delayMillis) {
        if (r == null || delayMillis < 0) {
            return;
        }
        getWorkerHandler().postDelayed(r, delayMillis);
        return;
    }

    /**
     * 当前线程是否主线程
     * @return 如果是主线程则返回true，否则返回false
     */
    public static boolean isMainThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    /**
     * 把任务抛到主线程去执行
     * @param run 任务
     */
    public static void postToMainThread(final Runnable run) {
        if (run == null) {
            return;
        }
        getMainHandler().post(run);
    }

    /**
     * 把任务抛到主线程去延时执行
     * @param run  任务
     * @param delayMillis  延时多少毫秒执行任务
     */
    public static void postToMainThreadDelayed(final Runnable run, long delayMillis) {
        if (run == null || delayMillis < 0) {
            return;
        }
        getMainHandler().postDelayed(run, delayMillis);
    }

}

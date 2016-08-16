package com.esapos.lib.Controller;


import com.esapos.lib.model.Component.RxJava.ResultCall;
import com.esapos.lib.model.Component.RxJava.RxTask;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Subscriber;

/**
 * Created by VickyLeu on 2016/7/20.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class Rx观察者控制器
 */
public class ObservableController {

    private static final String TAG = ObservableController.class.getSimpleName();
    // 请求队列
    private static LinkedList<RxTask> tasks;
    // 任务不能重复
    private static Set<String> taskIdSet;
    private static RXTaskThread taskThread;

    public ObservableController() {
    }

    private RxTask currentTask;

    public RxTask getCurrentTask() {
        return currentTask;
    }

    private static ObservableController instance;

    public static synchronized ObservableController getInstance() {
        if (instance == null) {
            instance = new ObservableController();
            tasks = new LinkedList<>();
            taskIdSet = new HashSet<>();
            taskThread = new RXTaskThread();
        }
        return instance;
    }

    private static void startTask() {
        taskThread.setStop(false);
        new Thread(taskThread).start();
    }


    public void addTask(RxTask rxTask) {
        if (currentTask != null) {
            if (currentTask.getName().equals("搜索蓝牙设备"))
                currentTask.setCall(null);
        }
        synchronized (tasks) {
            if (!isTaskRepeat(rxTask.getName())) {
                // 增加下载任务
                tasks.addLast(rxTask);
                if (taskThread.isStop) {
                    startTask();
                }
            }
        }
    }

    public boolean isTaskRepeat(String taskId) {
        synchronized (taskIdSet) {
            if (taskIdSet.contains(taskId)) {
                return true;
            } else {
                taskIdSet.add(taskId);
                return false;
            }
        }
    }

    public void shutdown() {
        taskThread.setStop(true);
    }

    public RxTask getTask() {
        synchronized (tasks) {
            if (tasks.size() > 0) {
                RxTask task = tasks.removeFirst();
                currentTask = task;
                return task;
            } else {
                shutdown();
            }
        }
        return null;
    }


    public static <T, D> ResultCall<T, D> handleFinal(T t, final Subscriber<D> subscriber) {
        return new ResultCall<T, D>(t) {
            @Override
            public void result(T callParam, D param) {
                super.result(callParam, param);
                subscriber.onNext(param);
            }
        };
    }


    static class RXTaskThread implements Runnable {
        private ObservableController controller;

        // 创建一个可重用固定线程数的线程池
        private ExecutorService pool;
        // 线程池大小
        private final int POOL_SIZE = 5;
        // 轮询时间
        private final int SLEEP_TIME = 1000;
        // 是否停止
        private boolean isStop = true;

        public RXTaskThread() {
            if (controller == null) controller = instance;
            if (pool == null) pool = Executors.newFixedThreadPool(POOL_SIZE);

        }

        public void setController(ObservableController controller) {
            this.controller = controller;
        }


        @Override
        public void run() {
            while (!isStop) {
                RxTask task = controller.getTask();
                if (task != null) {
                    if (pool.isShutdown()) {
                        pool = Executors.newFixedThreadPool(POOL_SIZE);
                    }
                    task.mountThread(taskIdSet);
                    pool.execute(task);

                } else {  //如果当前未有downloadTask在任务队列中
                    try {
                        // 查询任务完成失败的,重新加载任务队列
                        // 轮询,
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            if (isStop) {
                pool.shutdown();
            }

        }

        /**
         * @param isStop the isStop to set
         */
        public void setStop(boolean isStop) {
            this.isStop = isStop;
        }

    }
}

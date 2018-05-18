package org.springsource.loaded.remote;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskThreadFactory implements ThreadFactory {

    private boolean daemon;
    private int threadPriority;
    private String threadName;
    private AtomicInteger num;

    private TaskThreadFactory(String threadName, boolean daemon, int threadPriority) {
        this.daemon = daemon;
        this.threadPriority = threadPriority;
        this.threadName = threadName;
        this.num = new AtomicInteger();
    }

    public static final ThreadFactory createFactory(String threadName, boolean daemon, int threadPriority) {
        if (threadName == null) {
            throw new IllegalArgumentException("[TaskThreadFactory] createFactory : must have threadName.");
        }
        return new TaskThreadFactory(threadName, daemon, threadPriority);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(this.daemon);
        thread.setPriority(this.threadPriority);
        thread.setName(this.threadName + "-" + this.num.getAndDecrement());
        return thread;
    }
}

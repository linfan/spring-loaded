package org.springsource.loaded.remote;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SpringLoadedRemoteServer {

    private static HttpServer httpServer;
    private static ExecutorService executor;

    public static void start() {
        InetSocketAddress inetSock = new InetSocketAddress(50998);
        try {

            executor = new ThreadPoolExecutor(1, 10, 10L, TimeUnit.MINUTES,
                new LinkedBlockingQueue(1000),
                TaskThreadFactory.createFactory("springloaded-remote-worker", true, 5),
                new ThreadPoolExecutor.CallerRunsPolicy());

            httpServer = HttpServer.create(inetSock, 0);
            httpServer.createContext("/swap", new ClassSwapper());
            httpServer.setExecutor(executor);
            httpServer.start();
            System.out.println("HttpServer Start !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

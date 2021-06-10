package rainbow.kuzwlu.util;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class ThreadPool {

    private static int threadInitNumber = 1;
    private static int pool_size;
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public ExecutorService executorService ;
    private ThreadPool() {
        pool_size = DEFAULT_POOL_SIZE;
        executorService = Executors.newFixedThreadPool(pool_size, r -> new Thread(r,"kuzwlu-pool-"+nextThreadNum()));

    }

    public ThreadPool config(
            Integer poolSize
    ){
        if (poolSize > DEFAULT_POOL_SIZE) System.err.println("Warn！Warn！Warn！\t推荐线程数："+DEFAULT_POOL_SIZE+"！");
        pool_size = poolSize;
        executorService = Executors.newFixedThreadPool(pool_size, r -> new Thread(r,"kuzwlu-pool-"+nextThreadNum()));
        return this;
    }



    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

    private static ThreadPool instance = new ThreadPool();
    public static ThreadPool getInstance(){
        return instance;
    }
}

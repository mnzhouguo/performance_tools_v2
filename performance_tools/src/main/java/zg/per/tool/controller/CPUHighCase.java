package zg.per.tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * CPU常见问题
 * 1、线程死锁
 * 2、bug导致的死循环
 */
@RestController()
public class CPUHighCase {
    /**
     * 开启一个线程
     */
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            15,
            2,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(50),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 模拟CPU使用率高
     *
     * @return
     */
    @GetMapping("/highCPUUsage ")
    public String highCPUUsage() {
        executor.submit(() -> {
            int i = 0;
            while (true) {
                i = i++ * 10 + 5;
            }
        });
        return "success";
    }

    @GetMapping("/threadLock")
    public String threadLock() {
        Object resourceA = new Object();
        Object resourceB = new Object();

        // 第一线程先锁住A资源
        // 运行1秒后再获取B资源
        executor.submit(() -> {
            synchronized (resourceA) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceB) {
                }
            }
        });

        // 第二线程先锁住B资源
        // 运行1秒后获取A资源
        executor.submit(() -> {
            synchronized (resourceB) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceA) {
                }
            }
        });
        return "success";
    }
}



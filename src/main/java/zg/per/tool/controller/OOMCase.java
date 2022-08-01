package zg.per.tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 常见的内存泄漏情况
 * 1、线程本地变量使用不当ThreadLocal
 * 2、单例模式使用不当，单例中有对外部的引用，JVM将不会进行回收
 * 3、系统资源（IO，网络，数据库等）未释放
 * 4、全局变量引起，对象不释放
 */
@RestController()
public class OOMCase {
    /**
     * 线程本地变量使用不当带来的内存泄漏
     * 正确的写法：
     * try {
     * localVariable.set("localVariable");
     * } finally {
     * localVariable.remove("localVariable");
     * }
     *
     * @return
     */
    @RequestMapping(value = "/threadLocalVariable")
    public String threadLocalVariable() {
        ThreadLocal localVariable = new ThreadLocal();
        localVariable.set(new Byte[1 * 1024 * 1024]); //为线程添加变量 1M大小, 1M=1024K = 1024*1024 Byte
        return "success";
    }

    /**
     * 线程本地变量使用不当带来的内存泄漏
     * 正确的写法：
     * try {
     * localVariable.set("localVariable");
     * } finally {
     * localVariable.remove("localVariable");
     * }
     *
     * @return
     */
    @RequestMapping(value = "/threadLocalVariableV2")
    public String threadLocalVariableV2() {
        ThreadLocal localVariable = new ThreadLocal();
        try {
            localVariable.set(new Byte[1 * 1024 * 1024]); //为线程添加变量 1M大小, 1M=1024K = 1024*1024 Byte
        } finally {
            localVariable.remove();
        }
        return "success";
    }

    /**
     * 模拟全局变量引起的内存泄漏
     */
    List<byte[]> memoryList = new ArrayList<>();
    @GetMapping("/globalVariable")
    public String globalVariable() {
        byte[] b = new byte[2 * 1024 * 1024];
        memoryList.add(b);
        return "success";
    }

    /**
     * 模拟高并发引起的内存泄漏
     *
     * @return
     */
    @GetMapping("/highConcurrency")
    public String highConcurrency() {
        return "success";
    }

    @RequestMapping(value = "/vector")
    public String vector() {
        Vector vector = new Vector();
        for (int i = 1; i < 100; i++) {
            Object object = new Object();
            vector.add(object);
            object = null;
        }
        return "OK";
    }
}



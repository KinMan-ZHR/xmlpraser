package com.jiuaoedu.monitor.platform.utils.exceptions;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * 异常工厂
 */
public class ExceptionFactory {

    private ExceptionFactory() {
        // Prevent Instantiation
    }

    public static RuntimeException wrapException(String message, Exception e) {
        //查找错误上下文，得到错误原因，传给PersistenceException
        //每个线程都会有一个ErrorContext，所以可以得到，  .message(message).cause是典型的构建器模式
        return new PersistenceException(ErrorContext.instance().message(message).cause(e).toString(), e);
    }

}

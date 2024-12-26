package com.jiuaoedu.monitor.platform.utils.parsing;


import com.jiuaoedu.monitor.platform.utils.exceptions.PersistenceException;

/**
 * 解析异常
 */
public class ParsingException extends PersistenceException {
    private static final long serialVersionUID = -176685891441325943L;

    public ParsingException() {
        super();
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}

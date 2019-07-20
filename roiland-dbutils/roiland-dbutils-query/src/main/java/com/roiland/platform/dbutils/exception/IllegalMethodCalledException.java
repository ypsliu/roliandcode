package com.roiland.platform.dbutils.exception;

/**
 * 非法方法调用异常
 */
public class IllegalMethodCalledException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1338352930860636550L;

	public IllegalMethodCalledException(String message) {
        super(message);
    }

    public IllegalMethodCalledException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

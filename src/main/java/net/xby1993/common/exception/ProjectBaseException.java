package net.xby1993.common.exception;

/**
 * 项目基础异常类
 * @author taojw
 *
 */
public class ProjectBaseException extends Exception{

	private static final long serialVersionUID = -7184359810390651650L;

	public ProjectBaseException() {
		super();
	}

	public ProjectBaseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProjectBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectBaseException(String message) {
		super(message);
	}

	public ProjectBaseException(Throwable cause) {
		super(cause);
	}
	
}

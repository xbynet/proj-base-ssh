package net.xby1993.common.session;

/**
 * Created by taojw .
 */
public class RedisSessionException  extends RuntimeException{
	private static final long serialVersionUID = -7327453858603867313L;

	public RedisSessionException() {
        super();
    }

    public RedisSessionException(String message) {
        super(message);
    }

    public RedisSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisSessionException(Throwable cause) {
        super(cause);
    }
}

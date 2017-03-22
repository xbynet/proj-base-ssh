package net.xby1993.common.redis;

/**
 * Created by taojw.
 */
public class JedisException extends RuntimeException{
    public JedisException() {
        super();
    }

    public JedisException(String message) {
        super(message);
    }

    public JedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public JedisException(Throwable cause) {
        super(cause);
    }
}

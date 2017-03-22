package net.xby1993.common.session;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by taojw .
 */
public class RedisSession implements Session,Serializable {
    private static final long serialVersionUID = 3728882718536504921L;
    private volatile String id;
    private ConcurrentHashMap<String,Serializable> attrs;

    public RedisSession() {
        id= UUID.randomUUID().toString();
        attrs=new ConcurrentHashMap<String, Serializable>();
    }

    public RedisSession(String id) {
        this.id=id;
        this.attrs=new ConcurrentHashMap<String, Serializable>();
    }

    public RedisSession(String id, ConcurrentHashMap<String, Serializable> attrs) {
        this.id = id;
        this.attrs = attrs;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return (T) attrs.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return attrs.keySet();
    }

    @Override
    public void setAttribute(String attributeName, Serializable attributeValue) {
        attrs.put(attributeName,attributeValue);
    }

    @Override
    public void removeAttribute(String attributeName) {
        attrs.remove(attributeName);
    }
}

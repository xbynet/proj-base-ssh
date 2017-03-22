package net.xby1993.common.session;

import java.io.Serializable;
import java.util.Set;

/**
 * Session接口.
 * Created by taojw on 2017/1/10.
 */
public interface Session {
    String getId();
    <T> T getAttribute(String attributeName);
    Set<String> getAttributeNames();
    void setAttribute(String attributeName, Serializable attributeValue);
    void removeAttribute(String attributeName);
}

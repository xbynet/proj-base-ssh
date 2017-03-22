package net.xby1993.common.session;

/**
 * Session仓库,用户创建、获取、删除session等
 * Created by taojw on 2017/1/10.
 */
public interface SessionRepository<S extends Session> {
//    S createSession();
    void save(S session);
    S getSession(String id);
    void delete(String id);
}

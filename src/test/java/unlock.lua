--
-- Created by IntelliJ IDEA.
-- User: taojw
-- Date: 2017/1/10
-- Time: 15:30
-- To change this template use File | Settings | File Templates.
--
-- 释放锁
if redis.call('get',KEYS[1])==ARGV[1] then
    return redis.call('del',KEYS[1]) or 1
end
return 1
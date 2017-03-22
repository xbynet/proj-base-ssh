--
-- Created by IntelliJ IDEA.
-- User: taojw
-- Date: 2017/1/10
-- Time: 15:27
-- To change this template use File | Settings | File Templates.
--
-- 检测锁是否已经存在。KEYS参数为锁键，ARGV参数为过期时间与标识符组成的表格
if redis.call('exists',KEYS[1])==0 then
    redis.call('setex',KEYS[1],unpack(ARGV))
    return 1
end
return 0


local key = KEYS[1] -- rate_limit:URL:IP
local limit = tonumber(ARGV[1]) -- 1회 제한
local expire_time = tonumber(ARGV[2]) -- 300초 (5분)

-- 5분 내에 요청한 이력이 있으면 return -1
if redis.call('EXISTS', key) == 1 then
    return -1
end

-- 요청 내역 저장
redis.call('SETEX', key, expire_time, '1')
return 1

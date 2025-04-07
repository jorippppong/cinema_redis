local key = KEYS[1]         -- "request_ip:요청URL:127.0.0.1"
local ban_key = KEYS[2]     -- "banned_ip:요청URL:127.0.0.1"
local limit = tonumber(ARGV[1])  -- 50회 제한
local expire_time = tonumber(ARGV[2])  -- 제한 시간 : 60초 (1분)
local ban_duration = tonumber(ARGV[3]) -- 차단 시간 : 3600초 (1시간)

-- 이미 차단되어 있으면 return -1
if redis.call('EXISTS', ban_key) == 1 then
    return -1
end

-- count 1 증가
local current = redis.call('INCR', key)

-- 첫 요청이면 expire 설정해서 초기화
if current == 1 then
    redis.call('EXPIRE', key, expire_time)
end

-- 요청 횟수를 초과하면 ban_key에 등록 & return -1
if current > limit then
    redis.call('SETEX', ban_key, ban_duration, '1')
    return -1
end

-- 요청 횟수를 return (문제 없을 경우)
return current

local key = KEYS[1]         -- "request_ip:요청URL:127.0.0.1"
local ban_key = KEYS[2]     -- "banned_ip:요청URL:127.0.0.1"
local limit = tonumber(ARGV[1])  -- 50회 제한
local expire_time = tonumber(ARGV[2])  -- 제한 시간 : 60초 (1분)
local ban_duration = tonumber(ARGV[3]) -- 차단 시간 : 3600초 (1시간)

-- 이미 차단되어 있으면 return -1
if redis.call('EXISTS', ban_key) == 1 then
    return -1
end

-- 현재 카운트 가져오기
local current = tonumber(redis.call('GET', key) or '0')

if current > limit then
    -- 초과 요청: 차단 등록
    redis.call('SETEX', ban_key, ban_duration, '1')
    return -1
else
    -- 요청 허용: count 증가 & expire 설정
    redis.call('INCRBY', key, 1)
    redis.call('EXPIRE', key, expire_time)
    return current + 1
end

-- theater insert
INSERT INTO theater (created_at, created_by, updated_at, updated_by, name)
SELECT
    NOW(6),
    1,
    NOW(6),
    1,
    CONCAT(num, '영화관')
FROM (
         WITH RECURSIVE numbers AS (
             SELECT 4 AS num
             UNION ALL
             SELECT num + 1 FROM numbers WHERE num < 100
         )
         SELECT * FROM numbers
     ) AS temp;


-- screen insert
INSERT INTO screen (created_at, created_by, updated_at, updated_by, theater_id, name)
SELECT
    NOW(6),
    1,
    NOW(6),
    1,
    combo.theater_id,
    CONCAT(combo.screen_num, '관')
FROM (
         WITH RECURSIVE theater_ids AS (
             SELECT 1 AS theater_id
             UNION ALL
             SELECT theater_id + 1 FROM theater_ids WHERE theater_id < 100
         ),
                        screen_numbers AS (
                            SELECT 1 AS screen_num
                            UNION ALL
                            SELECT screen_num + 1 FROM screen_numbers WHERE screen_num < 10
                        )
         SELECT t.theater_id, s.screen_num
         FROM theater_ids t
                  CROSS JOIN screen_numbers s
     ) AS combo;

-- movie insert
INSERT INTO movie (running_time, created_at, created_by, updated_at, updated_by, released_at, poster_url, title, genre, rating)
SELECT
        FLOOR(RAND() * (120 - 80 + 1)) + 80,                      -- running_time (80~120)
        NOW(6),                                                  -- created_at
        1,                                                       -- created_by
        NOW(6),                                                  -- updated_at
        1,                                                       -- updated_by
        DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 365) DAY), -- released_at
        CONCAT(seq, '.png'),                                     -- poster_url
        CONCAT(seq, '영화'),                                     -- title
        ELT(FLOOR(RAND() * 6) + 1, 'ACTION', 'COMEDY', 'DRAMA', 'HORROR', 'ROMANCE', 'SCI_FI'), -- genre
        ELT(FLOOR(RAND() * 5) + 1, 'ALL', 'NC_19', 'PG_12', 'PG_15', 'RESTRICT')                -- rating
FROM (
         WITH RECURSIVE seq_gen AS (
             SELECT 1 AS seq
             UNION ALL
             SELECT seq + 1 FROM seq_gen WHERE seq < 1000
         )
         SELECT seq FROM seq_gen
     ) AS temp;

-- movieScheduleProjection insert
-- 스케줄 테이블에 데이터를 삽입하는 쿼리
INSERT INTO movieScheduleProjection (movie_id, screen_id, start_at, end_at, created_at, updated_at)
WITH RECURSIVE DateSeries AS (
    -- 2025년 8월 1일부터 2025년 9월 30일까지의 날짜를 생성
    SELECT CAST('2025-08-01' AS DATE) AS schedule_date
    UNION ALL
    SELECT DATE_ADD(schedule_date, INTERVAL 1 DAY)
    FROM DateSeries
    WHERE schedule_date < '2025-09-30'
),
TimeSlots AS (
    -- 하루에 고정된 10개의 시간 슬롯 정의
    SELECT 1 AS slot_order, '04:00:00' AS start_time_str, '06:00:00' AS end_time_str UNION ALL
    SELECT 2, '06:00:00', '08:00:00' UNION ALL
    SELECT 3, '08:00:00', '10:00:00' UNION ALL
    SELECT 4, '10:00:00', '12:00:00' UNION ALL
    SELECT 5, '12:00:00', '14:00:00' UNION ALL
    SELECT 6, '14:00:00', '16:00:00' UNION ALL
    SELECT 7, '16:00:00', '18:00:00' UNION ALL
    SELECT 8, '18:00:00', '20:00:00' UNION ALL
    SELECT 9, '20:00:00', '22:00:00' UNION ALL
    SELECT 10, '22:00:00', '00:00:00' -- 다음 날 00:00:00 (자정)
),
DailyScreenMovie AS (
    -- 각 날짜와 스크린별로 상영할 영화를 하나씩 선택
    -- (상영 시작일 기준 한 달 이내 개봉한 영화 중 랜덤으로 선택)
    SELECT
        ds.schedule_date,
        s.id AS screen_id,
        (SELECT m.id
         FROM movie m
         -- 영화 개봉일이 상영 시작일로부터 한 달 이내여야 함
         WHERE m.released_at >= DATE_SUB(ds.schedule_date, INTERVAL 1 MONTH)
           AND m.released_at <= ds.schedule_date -- 영화는 스케줄 날짜 이전에 개봉했어야 함
         ORDER BY RAND() -- 유효한 영화 중 랜덤으로 하나 선택
         LIMIT 1
        ) AS movie_id
    FROM DateSeries ds
    CROSS JOIN screen s
)
-- 최종 스케줄 데이터를 조합하여 삽입
SELECT
    dsm.movie_id,
    dsm.screen_id,
    -- start_at DATETIME 생성
    STR_TO_DATE(CONCAT(dsm.schedule_date, ' ', ts.start_time_str), '%Y-%m-%d %H:%i:%s') AS start_at,
    -- end_at DATETIME 생성 (자정 슬롯은 다음 날짜로 처리)
    CASE
        WHEN ts.end_time_str = '00:00:00' THEN STR_TO_DATE(CONCAT(DATE_ADD(dsm.schedule_date, INTERVAL 1 DAY), ' ', ts.end_time_str), '%Y-%m-%d %H:%i:%s')
        ELSE STR_TO_DATE(CONCAT(dsm.schedule_date, ' ', ts.end_time_str), '%Y-%m-%d %H:%i:%s')
        END AS end_at,
    NOW() AS created_at, -- 현재 시간으로 created_at 설정
    NOW() AS updated_at -- 현재 시간으로 updated_at 설정
FROM DailyScreenMovie dsm
         CROSS JOIN TimeSlots ts
WHERE dsm.movie_id IS NOT NULL; -- 유효한 영화가 선택된 경우에만 스케줄 생성

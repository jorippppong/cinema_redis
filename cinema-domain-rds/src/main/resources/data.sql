INSERT INTO user_account(name, created_at, updated_at, created_by, updated_by)
VALUES ('Flint', '2025-01-01', '2025-01-01', 1, 1),
       ('Melita', '2025-01-01', '2025-01-01', 1, 1),
       ('Sue', '2025-01-01', '2025-01-01', 1, 1),
       ('Mathew', '2025-01-01', '2025-01-01', 1, 1),
       ('Tillie', '2025-01-01', '2025-01-01', 1, 1),
       ('Thaine', '2025-01-01', '2025-01-01', 1, 1),
       ('Blondy', '2025-01-01', '2025-01-01', 1, 1),
       ('Patricio', '2025-01-01', '2025-01-01', 1, 1),
       ('Archibold', '2025-01-01', '2025-01-01', 1, 1),
       ('Faythe', '2025-01-01', '2025-01-01', 1, 1);


-- 영화 600(6 *100)개
DROP PROCEDURE IF EXISTS generate_movies;

CREATE PROCEDURE generate_movies()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE movie_title VARCHAR(255);
    DECLARE movie_genre ENUM ('ACTION', 'COMEDY', 'DRAMA', 'HORROR', 'ROMANCE', 'SCI_FI');
    DECLARE movie_rating ENUM ('ALL', 'NC_19', 'PG_12', 'PG_15', 'RESTRICT');
    DECLARE running_time INT;
    DECLARE release_year INT;
    DECLARE release_month INT;
    DECLARE release_day INT;

    WHILE i <= 600 DO
        -- 장르 선택 (100개씩 배분)
        CASE ((i - 1) DIV 100) + 1
            WHEN 1 THEN SET movie_genre = 'ACTION';
            WHEN 2 THEN SET movie_genre = 'COMEDY';
            WHEN 3 THEN SET movie_genre = 'DRAMA';
            WHEN 4 THEN SET movie_genre = 'HORROR';
            WHEN 5 THEN SET movie_genre = 'ROMANCE';
            WHEN 6 THEN SET movie_genre = 'SCI_FI';
        END CASE;

        -- 랜덤한 제목 생성
        SET movie_title = CONCAT(movie_genre, ' Movie ', i);

        -- 연령 등급 무작위 선택
        SET movie_rating = ELT(FLOOR(1 + RAND() * 5), 'ALL', 'NC_19', 'PG_12', 'PG_15', 'RESTRICT');

        -- 랜덤 러닝타임 (최대 120분)
        SET running_time = FLOOR(60 + RAND() * 61);

        -- 랜덤 개봉일 (2020~2025년)
        SET release_year = 2020 + FLOOR(RAND() * 6);
        SET release_month = 1 + FLOOR(RAND() * 12);
        SET release_day = 1 + FLOOR(RAND() * 28);

        INSERT INTO movie (title, rating, released_at, poster_url, running_time, genre, created_at, updated_at, created_by, updated_by)
        VALUES (
           movie_title,
           movie_rating,
           CONCAT(release_year, '-', LPAD(release_month, 2, '0'), '-', LPAD(release_day, 2, '0')),
           CONCAT('https://example.com/', LOWER(movie_genre), '_', i, '.jpg'),
           running_time,
           movie_genre,
           NOW(),
           NOW(),
           1,
           1
        );
        SET i = i + 1;
    END WHILE;
END;

CALL generate_movies();


-- 극장 10개
INSERT INTO theater(name, created_at, updated_at, created_by, updated_by)
VALUES ('A영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('B영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('C영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('D영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('E영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('F영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('G영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('H영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('I영화관', '2025-01-01', '2025-01-01', 1, 1),
       ('J영화관', '2025-01-01', '2025-01-01', 1, 1);

-- 스크린 50(10 * 5)개
INSERT INTO screen (name, theater_id, created_at, updated_at, created_by, updated_by)
SELECT
    CONCAT(screen_index, '관') AS screen_name,  -- 스크린 이름 (1관, 2관, 3관 ... 5관)
    theater_index AS theater_id,  -- 극장 ID (1~10)
    NOW() AS created_at,  -- 생성 시간
    NOW() AS updated_at,  -- 업데이트 시간
    1 AS created_by,  -- 생성자
    1 AS updated_by  -- 업데이트자
FROM
    (SELECT 1 AS theater_index UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
     UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS theaters,  -- 극장 ID (1~10)
    (SELECT 1 AS screen_index UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS screens  -- 1관~5관

ORDER BY
    theater_index, screen_index;  -- 극장 순으로 스크린 생성


-- 좌석 (모든 스크린 각 25개)
DROP PROCEDURE IF EXISTS generate_seats;

CREATE PROCEDURE generate_seats()
BEGIN
    DECLARE screen_index INT DEFAULT 1;  -- 스크린 ID (1~50)
    DECLARE row_char CHAR(1);            -- 좌석 행 (A, B, C, D, E)
    DECLARE seat_number VARCHAR(255);    -- 좌석 번호 (A1, A2, ..., E5)
    DECLARE seat_index INT;              -- 좌석 번호 (1~5)
    DECLARE row_index INT;               -- 행 번호 (A, B, C, D, E)

    -- 스크린 ID 1부터 50까지 반복
    WHILE screen_index <= 50 DO
        -- 행 (A, B, C, D, E) 반복
        SET row_index = 1;  -- 행을 시작 (1 ~ 5)

        WHILE row_index <= 5 DO
            SET row_char = CHAR(64 + row_index);  -- A ~ E (ASCII 값: 65부터 시작)

            -- 좌석 번호 생성 (1부터 5까지)
            SET seat_index = 1;  -- 열 번호 (1~5)

            WHILE seat_index <= 5 DO
                -- 좌석 번호 생성 (예: A1, A2, B1, B2, ...)
                SET seat_number = CONCAT(row_char, seat_index);

                -- 좌석 INSERT
                INSERT INTO seat (seat_number, screen_id, created_at, updated_at, created_by, updated_by)
                VALUES (seat_number, screen_index, NOW(), NOW(), 1, 1);

                -- 좌석 번호 증가
                SET seat_index = seat_index + 1;
            END WHILE;

            -- 다음 행으로 이동
            SET row_index = row_index + 1;
        END WHILE;

        -- 다음 스크린으로 이동
        SET screen_index = screen_index + 1;
    END WHILE;
END;

CALL generate_seats();


-- 상영 스케줄
INSERT INTO schedule (movie_id, screen_id, start_at, end_at, created_at, created_by, updated_at, updated_by)
SELECT
    m.id AS movie_id,
    s.id AS screen_id,
    DATE_ADD(CURRENT_DATE(), INTERVAL d DAY) + INTERVAL h HOUR + INTERVAL (m.id * 7 % 60) MINUTE AS start_time,  -- 시작 시간 계산
    DATE_ADD(CURRENT_DATE(), INTERVAL d DAY) + INTERVAL h HOUR + INTERVAL (m.id * 7 % 60) MINUTE + INTERVAL m.running_time MINUTE AS end_time,  -- 종료 시간 계산
    NOW() AS created_at,
    1 AS created_by,
    NOW() AS updated_at,
    1 AS updated_by
FROM
    (SELECT id, running_time FROM movie ORDER BY RAND() LIMIT 600) m,  -- 600개의 랜덤 영화
    screen s,  -- 모든 극장
    (SELECT 0 AS d UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13) AS days,  -- 14일
    (SELECT 9 AS h UNION SELECT 12 UNION SELECT 15 UNION SELECT 18 UNION SELECT 21) AS hours  -- 5개의 상영 시간대
ORDER BY
    m.id, s.id, d, h  -- 영화 ID, 극장 ID, 날짜, 시간 순으로 정렬
    LIMIT 3000;  -- 상영 스케줄 3000개 삽입

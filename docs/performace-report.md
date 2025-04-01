```sql
EXPLAIN
SELECT *
FROM schedule se
         JOIN movie m ON se.movie_id = m.id
         JOIN screen s ON se.screen_id = s.id
WHERE se.start_at >= CURRENT_DATE
  AND m.title LIKE 'ACTION%'
  AND m.genre = 'ACTION'
ORDER BY m.released_at DESC,
         se.start_at
```

### 인덱스 적용 전 실행 쿼리



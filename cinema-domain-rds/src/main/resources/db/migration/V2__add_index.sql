CREATE INDEX idx_schedule_startAt ON schedule(start_at);
CREATE INDEX idx_movie_releasedAt ON movie(released_at);
CREATE INDEX idx_movie_title ON movie(title);   -- equal, %가 접미사에만 존재
CREATE FULLTEXT INDEX idx_movie_title_fulltext ON movie(title); -- %_%

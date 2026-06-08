CREATE TABLE showtimes
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hall_id UUID NOT NULL REFERENCES halls(id) ON DELETE RESTRICT ,
    movie_id UUID NOT NULL REFERENCES movies(id) ON DELETE RESTRICT,
    start_time TIMESTAMPTZ NOT NULL,
    base_price NUMERIC(10,2) NOT NULL
);

CREATE TABLE showtime_seats(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    showtime_id UUID NOT NULL REFERENCES showtimes(id) ON DELETE CASCADE,
    seat_id UUID NOT NULL REFERENCES seats(id) ON DELETE RESTRICT,
    status varchar(20) NOT NULL DEFAULT 'AVAILABLE',
    price NUMERIC(10,2) NOT NULL,
    version INT NOT NULL DEFAULT 0,

    UNIQUE (showtime_id,seat_id)
);

CREATE INDEX idx_showtimes_hall_id ON showtimes(hall_id);
CREATE INDEX idx_showtimes_movie_id ON showtimes(movie_id);

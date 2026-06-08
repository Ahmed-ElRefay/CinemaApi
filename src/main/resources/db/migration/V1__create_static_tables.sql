CREATE TABLE cinemas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(50) NOT NULL,
    city varchar(50)
);

CREATE TABLE halls(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cinema_id UUID NOT NULL REFERENCES cinemas(id) ON DELETE CASCADE,
    name varchar(50) NOT NULL
);

CREATE TABLE seats(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    hall_id UUID NOT NULL REFERENCES halls(id) ON DELETE CASCADE,
    row_label varchar(20) NOT NULL,
    seat_number int NOT NULL ,
    seat_type varchar(20),

    UNIQUE (hall_id,row_label,seat_number)
);

CREATE TABLE movies(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title varchar(100) NOT NULL,
    duration_minutes int,
    genre varchar(15)
);

CREATE INDEX idx_halls_cinema_id ON halls(cinema_id);

--this can be removed because the hall-id is the left most column in the UNIQUE constraint.
--Worth knowing: a multi-column index can serve queries that filter on its leftmost columns.
CREATE INDEX idx_seats_hall_id ON seats(hall_id);
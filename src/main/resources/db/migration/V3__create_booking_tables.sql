CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(150) NOT NULL UNIQUE,
    name varchar(100) NOT NULL
);

CREATE TABLE bookings(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    showtime_id UUID NOT NULL REFERENCES showtimes(id) on DELETE RESTRICT,
    status varchar(20) NOT NULL DEFAULT 'HELD',
    hold_expires_at timestamptz,
    total_price NUMERIC(10,2) NOT NULL
);

CREATE TABLE booking_seats(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    showtime_seat_id UUID NOT NULL REFERENCES showtime_seats(id) ON DELETE RESTRICT,

    UNIQUE (showtime_seat_id)
);

CREATE INDEX idx_booking_status_hold_expires_at ON bookings(status,hold_expires_at);
CREATE INDEX idx_booking_user_id ON bookings(user_id);


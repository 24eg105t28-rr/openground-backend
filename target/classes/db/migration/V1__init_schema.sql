-- V1__init_schema.sql
-- OpenGround: Large Outdoor Cricket Ground Booking Schema

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER', -- CUSTOMER, OWNER, ADMIN
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS grounds (
    id UUID PRIMARY KEY,
    owner_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    boundary_radius_meters INT NOT NULL CHECK (boundary_radius_meters >= 55),
    pitch_type VARCHAR(50) NOT NULL, -- NATURAL_TURF, ASTROTURF_OUTDOOR, CLAY_SOIL
    floodlights BOOLEAN NOT NULL DEFAULT FALSE,
    pavilion_details TEXT,
    seating_capacity INT NOT NULL DEFAULT 0,
    hourly_base_rate NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'APPROVED', -- PENDING, APPROVED, REJECTED
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ground_images (
    id UUID PRIMARY KEY,
    ground_id UUID REFERENCES grounds(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    caption VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ground_facilities (
    id UUID PRIMARY KEY,
    ground_id UUID REFERENCES grounds(id) ON DELETE CASCADE,
    facility_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pricing_slots (
    id UUID PRIMARY KEY,
    ground_id UUID REFERENCES grounds(id) ON DELETE CASCADE,
    slot_type VARCHAR(50) NOT NULL, -- MORNING, AFTERNOON, EVENING, FULL_DAY
    start_time VARCHAR(20) NOT NULL,
    end_time VARCHAR(20) NOT NULL,
    weekday_price NUMERIC(10, 2) NOT NULL,
    weekend_price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS availability_closures (
    id UUID PRIMARY KEY,
    ground_id UUID REFERENCES grounds(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    id UUID PRIMARY KEY,
    booking_reference VARCHAR(50) UNIQUE NOT NULL,
    ground_id UUID REFERENCES grounds(id) ON DELETE RESTRICT,
    user_id UUID REFERENCES users(id) ON DELETE RESTRICT,
    booking_date DATE NOT NULL,
    slot_type VARCHAR(50) NOT NULL, -- MORNING, AFTERNOON, EVENING, FULL_DAY
    start_time VARCHAR(20) NOT NULL,
    end_time VARCHAR(20) NOT NULL,
    total_amount NUMERIC(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, PAID, REFUNDED
    payment_method VARCHAR(50) NOT NULL DEFAULT 'PAY_AT_GROUND', -- PAY_AT_GROUND, ONLINE_SANDBOX
    status VARCHAR(50) NOT NULL DEFAULT 'CONFIRMED', -- CONFIRMED, CANCELLED, COMPLETED
    notes TEXT,
    cancellation_reason TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    booking_id UUID REFERENCES bookings(id) ON DELETE CASCADE,
    transaction_reference VARCHAR(100) UNIQUE NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL, -- SUCCESS, PENDING, FAILED, REFUNDED
    gateway_response TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
    id UUID PRIMARY KEY,
    ground_id UUID REFERENCES grounds(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    booking_id UUID REFERENCES bookings(id) ON DELETE SET NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Indexes for performance & query speed
CREATE INDEX IF NOT EXISTS idx_grounds_city ON grounds(city);
CREATE INDEX IF NOT EXISTS idx_grounds_status ON grounds(status);
CREATE INDEX IF NOT EXISTS idx_bookings_ground_date ON bookings(ground_id, booking_date);
CREATE INDEX IF NOT EXISTS idx_bookings_user ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_reviews_ground ON reviews(ground_id);

-- V2__seed_cricket_grounds.sql
-- Seed demonstration users, outdoor cricket grounds, pricing slots, and facilities

-- Insert Default Users (Password for all demonstration users is 'password123')
-- BCrypt hash for 'password123': $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
INSERT INTO users (id, email, password, full_name, phone, role, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'admin@openground.cricket', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Administrator', '+91 98765 43210', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222', 'owner@lordsmeadow.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Vikramaditya Rao (Ground Owner)', '+91 98480 12345', 'OWNER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333', 'captain.raj@cricketclub.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Rajesh Sharma (Team Captain)', '+91 97000 54321', 'CUSTOMER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Large Outdoor Cricket Grounds
INSERT INTO grounds (id, owner_id, name, slug, description, address, city, state, boundary_radius_meters, pitch_type, floodlights, pavilion_details, seating_capacity, hourly_base_rate, status, created_at) VALUES
('g0000001-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222',
 'Lords Meadow Championship Oval', 'lords-meadow-championship-oval',
 'Full-size 72-meter boundary championship outdoor ground featuring a curated 22-yard natural red-soil turf pitch. Equipped with LED floodlight towers, digital live scoring displays, 4 dedicated practice turf nets, and grand pavilion dressing rooms.',
 'Survey No. 42, Outer Ring Road, Gachibowli Extension', 'Hyderabad', 'Telangana',
 72, 'NATURAL_TURF', true, 'Air-conditioned player pavilions with lockers, ice baths, commentary box, and 2,500 spectator terrace seats.',
 2500, 2800.00, 'APPROVED', CURRENT_TIMESTAMP),

('g0000002-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222',
 'Green Park International Outdoor Ground', 'green-park-international-outdoor-ground',
 'Expansive 76-meter boundary natural grass cricket stadium designed for 50-over corporate tournaments and club leagues. Features Bermuda grass outfield, motorized super sopper, heavy mechanical pitch rollers, and official BCCI-spec sightscreens.',
 'Near Electronic City Phase 2, Hosur Main Road', 'Bengaluru', 'Karnataka',
 76, 'NATURAL_TURF', false, 'Two team dressing rooms with dining area, umpire chamber, physical therapy suite, and 1,800 pavilion benches.',
 1800, 3200.00, 'APPROVED', CURRENT_TIMESTAMP),

('g0000003-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222222',
 'Willow Crest County Arena', 'willow-crest-county-arena',
 'Scenic 68-meter boundary outdoor ground equipped with an advanced all-weather artificial turf center wicket. Unhindered 360-degree outdoor outfield with high-lux tournament floodlights suitable for full 20-over day/night fixtures.',
 'East Coast Road, Kovalam Sector', 'Chennai', 'Tamil Nadu',
 68, 'ASTROTURF_OUTDOOR', true, 'Modern pavilion club lounge, elevated spectator stand with 1,200 capacity, and match broadcast tower.',
 1200, 2500.00, 'APPROVED', CURRENT_TIMESTAMP),

('g0000004-0000-0000-0000-000000000004', '22222222-2222-2222-2222-222222222222',
 'Royal Deccan Cricket Oval', 'royal-deccan-cricket-oval',
 'Standard 70-meter boundary outdoor ground with genuine black clay-soil center pitch offering true bounce and carry. Surrounded by lush eucalyptus tree line with dedicated boundary rope and electronic scoreboard.',
 'Shamshabad Aerocity Corridor', 'Hyderabad', 'Telangana',
 70, 'CLAY_SOIL', true, 'Classic brick pavilion with wooden balcony, players locker rooms, and 1,500 stepped embankment seating.',
 1500, 2400.00, 'APPROVED', CURRENT_TIMESTAMP),

('g0000005-0000-0000-0000-000000000005', '22222222-2222-2222-2222-222222222222',
 'Silver Oaks Outdoor County Park', 'silver-oaks-outdoor-county-park',
 'Picturesque 66-meter boundary outdoor facility in the Western Ghats foothills. Features dual side-by-side natural turf pitches for consecutive weekend league matches, clean mountain air, and ample team parking.',
 'Hinjawadi Phase 3, Mulshi Valley', 'Pune', 'Maharashtra',
 66, 'NATURAL_TURF', false, 'Stone-faced clubhouse with cafeteria, viewing gallery, umpire room, and 900 shaded bench seats.',
 900, 2100.00, 'APPROVED', CURRENT_TIMESTAMP);

-- Insert Images
INSERT INTO ground_images (id, ground_id, image_url, is_primary, caption) VALUES
('i0000001-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80', true, 'Full 72m boundary outfield and center pitch'),
('i0000002-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1531415074968-036ba1b575da?auto=format&fit=crop&w=1200&q=80', false, 'Tournament pavilion and dressing rooms'),
('i0000003-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'https://images.unsplash.com/photo-1587280501635-68a0e82cd5ff?auto=format&fit=crop&w=1200&q=80', true, 'Natural green turf stadium outfield'),
('i0000004-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'https://images.unsplash.com/photo-1517649763962-0c623266ddc0?auto=format&fit=crop&w=1200&q=80', true, 'Night floodlit cricket fixture under stadium lights'),
('i0000005-0000-0000-0000-000000000004', 'g0000004-0000-0000-0000-000000000004', 'https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80', true, 'Classic red-soil curated pitch'),
('i0000006-0000-0000-0000-000000000005', 'g0000005-0000-0000-0000-000000000005', 'https://images.unsplash.com/photo-1531415074968-036ba1b575da?auto=format&fit=crop&w=1200&q=80', true, 'Verdant green outfield with scenic valley perimeter');

-- Insert Facilities
INSERT INTO ground_facilities (id, ground_id, facility_name) VALUES
-- Lords Meadow
('f0000001-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'Natural Red-Soil Turf Pitch'),
('f0000002-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', '4 High-Mast LED Floodlights'),
('f0000003-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', '4 Practice Turf Nets'),
('f0000004-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'Digital Live Scoreboard'),
('f0000005-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'Player Pavilions with AC'),
('f0000006-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'Official Sightscreens'),
('f0000007-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'Team Bus & Car Parking (150+)'),

-- Green Park
('f0000008-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'Natural Bermuda Grass Turf Pitch'),
('f0000009-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'Mechanical Pitch Roller & Covers'),
('f0000010-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'Motorized Super Sopper'),
('f0000011-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', '2 Team Dressing Rooms'),
('f0000012-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', '6 Practice Turf Nets'),

-- Willow Crest
('f0000013-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'Outdoor AstroTurf Pitch'),
('f0000014-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'Night Floodlit Match Facility'),
('f0000015-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'Air-Conditioned Players Lounge'),
('f0000016-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'Live Match Streaming Setup');

-- Insert Pricing Slots for Ground 1 (Lords Meadow)
INSERT INTO pricing_slots (id, ground_id, slot_type, start_time, end_time, weekday_price, weekend_price) VALUES
('s0000001-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'MORNING', '07:00 AM', '11:30 AM', 12000.00, 15000.00),
('s0000002-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'AFTERNOON', '12:00 PM', '04:30 PM', 11000.00, 14000.00),
('s0000003-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'EVENING', '05:00 PM', '09:30 PM', 18000.00, 22000.00),
('s0000004-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', 'FULL_DAY', '07:00 AM', '05:00 PM', 28000.00, 35000.00),

-- Slots for Ground 2 (Green Park)
('s0000005-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'MORNING', '07:30 AM', '12:00 PM', 14000.00, 18000.00),
('s0000006-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'AFTERNOON', '12:30 PM', '05:00 PM', 13000.00, 16000.00),
('s0000007-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', 'FULL_DAY', '07:30 AM', '05:00 PM', 32000.00, 40000.00),

-- Slots for Ground 3 (Willow Crest)
('s0000008-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'MORNING', '06:30 AM', '11:00 AM', 10000.00, 13000.00),
('s0000009-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'AFTERNOON', '11:30 AM', '04:00 PM', 9000.00, 12000.00),
('s0000010-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'EVENING', '05:00 PM', '09:30 PM', 16000.00, 19000.00),
('s0000011-0000-0000-0000-000000000003', 'g0000003-0000-0000-0000-000000000003', 'FULL_DAY', '06:30 AM', '04:30 PM', 24000.00, 30000.00);

-- Insert Sample Reviews
INSERT INTO reviews (id, ground_id, user_id, rating, comment, created_at) VALUES
('r0000001-0000-0000-0000-000000000001', 'g0000001-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', 5, 'Superb full-size cricket ground! The 72m boundary and natural turf pitch carried true bounce for our 40-over league final. Outstanding pavilion dressing rooms.', CURRENT_TIMESTAMP),
('r0000002-0000-0000-0000-000000000002', 'g0000002-0000-0000-0000-000000000002', '33333333-3333-3333-3333-333333333333', 5, 'Bermuda grass outfield is lightning fast. True international match experience with full sight screens and motorized rollers.', CURRENT_TIMESTAMP);

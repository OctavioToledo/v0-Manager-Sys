-- Migration to allow appointments without a registered user (manual/guest appointments)
ALTER TABLE appointments ALTER COLUMN user_id DROP NOT NULL;

-- Add client contact fields to appointments table
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS client_name VARCHAR(255);
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS client_phone VARCHAR(50);
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS client_email VARCHAR(255);
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS notes TEXT;

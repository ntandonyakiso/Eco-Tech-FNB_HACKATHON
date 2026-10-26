CREATE TABLE usage_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(32) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    unit VARCHAR(32) NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE business_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    business_name VARCHAR(200) NOT NULL,
    business_type VARCHAR(100),
    average_hourly_revenue DOUBLE PRECISION DEFAULT 0,
    monthly_rent DOUBLE PRECISION DEFAULT 0,
    staff_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE stock_losses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    item VARCHAR(200) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    unit_cost DOUBLE PRECISION NOT NULL,
    loss_reason VARCHAR(100),
    recorded_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE food_points (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    added_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(64) NOT NULL,
    description TEXT,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    ward_id UUID REFERENCES wards(id),
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE surplus_posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    pickup_location TEXT NOT NULL,
    pickup_start TIMESTAMP NOT NULL,
    pickup_end TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_claimed BOOLEAN NOT NULL DEFAULT FALSE,
    claimed_by UUID REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE preparedness_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    household_type VARCHAR(64) NOT NULL,
    ward_id UUID REFERENCES wards(id),
    plan_data TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_usage_logs_user ON usage_logs(user_id);
CREATE INDEX idx_usage_logs_type ON usage_logs(type);
CREATE INDEX idx_business_profiles_user ON business_profiles(user_id);
CREATE INDEX idx_stock_losses_user ON stock_losses(user_id);
CREATE INDEX idx_food_points_approved ON food_points(is_approved);
CREATE INDEX idx_surplus_posts_expires ON surplus_posts(expires_at);
CREATE INDEX idx_surplus_posts_claimed ON surplus_posts(is_claimed);
CREATE INDEX idx_preparedness_plans_user ON preparedness_plans(user_id);

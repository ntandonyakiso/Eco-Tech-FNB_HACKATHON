CREATE TABLE resource_listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ward_id UUID REFERENCES wards(id),
    category VARCHAR(32) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    availability_start TIMESTAMP,
    availability_end TIMESTAMP,
    contact_method VARCHAR(32) NOT NULL,
    contact_value VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE resource_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES resource_listings(id) ON DELETE CASCADE,
    requester_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    message TEXT,
    responded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE vulnerable_contacts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    ward_id UUID REFERENCES wards(id),
    consent BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_resource_listings_category ON resource_listings(category);
CREATE INDEX idx_resource_listings_active ON resource_listings(is_active);
CREATE INDEX idx_resource_listings_owner ON resource_listings(owner_id);
CREATE INDEX idx_resource_requests_listing ON resource_requests(listing_id);
CREATE INDEX idx_resource_requests_requester ON resource_requests(requester_id);
CREATE INDEX idx_resource_requests_status ON resource_requests(status);
CREATE INDEX idx_vulnerable_contacts_user ON vulnerable_contacts(user_id);
CREATE INDEX idx_vulnerable_contacts_ward ON vulnerable_contacts(ward_id);

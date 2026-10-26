-- Migration V7: ward announcements / community notice board

CREATE TABLE IF NOT EXISTS ward_announcements (
    id UUID PRIMARY KEY,
    ward_id UUID NOT NULL REFERENCES wards(id),
    author_id UUID NOT NULL REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    body TEXT NOT NULL,
    type VARCHAR(32) NOT NULL DEFAULT 'GENERAL', -- GENERAL, EMERGENCY, MEETING, ALERT
    expires_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_ward_announcements_ward_id ON ward_announcements(ward_id);
CREATE INDEX IF NOT EXISTS idx_ward_announcements_created_at ON ward_announcements(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_ward_announcements_expires_at ON ward_announcements(expires_at);

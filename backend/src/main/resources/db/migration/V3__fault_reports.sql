CREATE TABLE fault_reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reporter_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ward_id UUID REFERENCES wards(id),
    category VARCHAR(32) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    photo_url TEXT,
    status VARCHAR(32) NOT NULL DEFAULT 'OPEN',
    upvote_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE report_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_id UUID NOT NULL REFERENCES fault_reports(id) ON DELETE CASCADE,
    old_status VARCHAR(32),
    new_status VARCHAR(32) NOT NULL,
    changed_by UUID REFERENCES users(id),
    note TEXT,
    changed_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE report_votes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_id UUID NOT NULL REFERENCES fault_reports(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (report_id, user_id)
);

CREATE INDEX idx_fault_reports_status ON fault_reports(status);
CREATE INDEX idx_fault_reports_category ON fault_reports(category);
CREATE INDEX idx_fault_reports_ward_id ON fault_reports(ward_id);
CREATE INDEX idx_fault_reports_created_at ON fault_reports(created_at);
CREATE INDEX idx_report_status_history_report_id ON report_status_history(report_id);
CREATE INDEX idx_report_votes_report_id ON report_votes(report_id);
CREATE INDEX idx_report_votes_user_id ON report_votes(user_id);

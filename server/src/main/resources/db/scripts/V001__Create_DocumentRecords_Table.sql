-- Create document_records table for tracking document uploads
-- Stores metadata about uploaded documents including file path, status, and timestamps

CREATE TABLE document_records (
    id BIGSERIAL PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(1024) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for common query patterns
CREATE INDEX idx_document_records_status ON document_records(status);

-- Add check constraint to enforce valid status values
ALTER TABLE document_records
ADD CONSTRAINT check_document_status CHECK (status IN ('PENDING', 'DISPATCHED', 'CONSUMED', 'FAILED'));

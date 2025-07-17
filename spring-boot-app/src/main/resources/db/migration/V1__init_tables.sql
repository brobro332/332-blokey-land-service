-- blokey
CREATE TABLE blokey (
    id UUID PRIMARY KEY,
    nickname VARCHAR(20) NOT NULL,
    bio VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- project
CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    status VARCHAR(10),
    is_private BOOLEAN NOT NULL DEFAULT FALSE,
    estimated_start_date DATE,
    estimated_end_date DATE,
    actual_start_date DATE,
    actual_end_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- member
CREATE TABLE member (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(10),
    project_id BIGINT,
    blokey_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_member_blokey FOREIGN KEY (blokey_id) REFERENCES blokey(id)
);

-- milestone
CREATE TABLE milestone (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATE,
    project_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_milestone_project FOREIGN KEY (project_id) REFERENCES project(id)
);

-- task
CREATE TABLE task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    assignee UUID,
    progress INTEGER CHECK (progress >= 0 AND progress <= 100),
    status VARCHAR(15),
    priority VARCHAR(10),
    project_id BIGINT,
    estimated_start_date DATE,
    estimated_end_date DATE,
    actual_start_date DATE,
    actual_end_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id)
);

-- offer
CREATE TABLE offer (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT,
    blokey_id UUID,
    offerer VARCHAR(10),
    status VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_offer_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_offer_blokey FOREIGN KEY (blokey_id) REFERENCES blokey(id)
);
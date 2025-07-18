-- skill
CREATE TABLE skill (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    display_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- discipline
CREATE TABLE discipline (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- blokey_skill
CREATE TABLE blokey_skill (
    id BIGSERIAL PRIMARY KEY,
    blokey_id UUID NOT NULL,
    skill_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (blokey_id) REFERENCES blokey(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE,
    CONSTRAINT uk_blokey_skill UNIQUE (blokey_id, skill_id)
);
CREATE INDEX idx_blokey_skill_blokey_id ON blokey_skill(blokey_id);
CREATE INDEX idx_blokey_skill_skill_id ON blokey_skill(skill_id);

-- blokey_discipline
CREATE TABLE blokey_discipline (
    id BIGSERIAL PRIMARY KEY,
    blokey_id UUID NOT NULL,
    discipline_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (blokey_id) REFERENCES blokey(id) ON DELETE CASCADE,
    FOREIGN KEY (discipline_id) REFERENCES discipline(id) ON DELETE CASCADE,
    CONSTRAINT uk_blokey_discipline UNIQUE (blokey_id, discipline_id)
);
CREATE INDEX idx_blokey_discipline_blokey_id ON blokey_discipline(blokey_id);
CREATE INDEX idx_blokey_discipline_discipline_id ON blokey_discipline(discipline_id);

-- project_skill
CREATE TABLE project_skill (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE,
    CONSTRAINT uk_project_skill UNIQUE (project_id, skill_id)
);
CREATE INDEX idx_project_skill_project_id ON project_skill(project_id);
CREATE INDEX idx_project_skill_skill_id ON project_skill(skill_id);

-- project_discipline
CREATE TABLE project_discipline (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    discipline_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (discipline_id) REFERENCES discipline(id) ON DELETE CASCADE,
    CONSTRAINT uk_project_discipline UNIQUE (project_id, discipline_id)
);
CREATE INDEX idx_project_discipline_project_id ON project_discipline(project_id);
CREATE INDEX idx_project_discipline_discipline_id ON project_discipline(discipline_id);
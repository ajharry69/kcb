--
CREATE TABLE IF NOT EXISTS projects
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(100) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS tasks
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    status     VARCHAR(50),
    dueDate    DATE,
    project_id UUID REFERENCES projects (id)
);

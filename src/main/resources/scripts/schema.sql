CREATE SCHEMA IF NOT EXISTS blog;

CREATE TABLE IF NOT EXISTS blog.posts (
    id BIGSERIAL PRIMARY KEY ,
    title VARCHAR(256) NOT NULL ,
    text_preview VARCHAR NOT NULL ,
    likes_count BIGINT NOT NULL DEFAULT 0,
    text VARCHAR
);

CREATE TABLE IF NOT EXISTS blog.comments (
    id BIGSERIAL PRIMARY KEY ,
    comment VARCHAR(256) NOT NULL ,
    post_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES blog.posts(id)
);

CREATE TABLE IF NOT EXISTS blog.tags (
    id BIGSERIAL PRIMARY KEY ,
    tag VARCHAR(256) NOT NULL ,
    post_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES blog.posts(id)
);

CREATE TABLE IF NOT EXISTS blog.images (
    id BIGSERIAL PRIMARY KEY ,
    path VARCHAR(256) NOT NULL ,
    post_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES blog.posts(id)
)

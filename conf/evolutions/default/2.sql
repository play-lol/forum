# Post Schema

# --- !Ups

CREATE TABLE Post (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    groupId bigint(20) NOT NULL,
    content TEXT,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Post;

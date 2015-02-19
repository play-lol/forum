# Group Schema
 
# --- !Ups
 
CREATE TABLE Group (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    description TEXT,
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE Group;

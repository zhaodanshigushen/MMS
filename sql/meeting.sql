CREATE TABLE IF NOT EXISTS meeting(
mid             INT NOT NULL auto_increment,
title           VARCHAR(100) NOT NULL,
sponsor         INT NOT NULL,
participator    VARCHAR(100) NOT NULL,
stime           TIMESTAMP,
etime           TIMESTAMP,
position        VARCHAR(100),
content         TEXT,
PRIMARY KEY (`mid`),
FOREIGN KEY (sponsor) REFERENCES user(uid)
)DEFAULT CHARSET=utf8;

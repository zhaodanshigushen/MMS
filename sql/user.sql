CREATE TABLE IF NOT EXISTS user(
uid       INT NOT NULL auto_increment,
username  VARCHAR(100) NOT NULL,
password  VARCHAR(100) NOT NULL,
email     VARCHAR(100),
phone     VARCHAR(100),
friends   TEXT,
PRIMARY KEY (`uid`)
)DEFAULT CHARSET=utf8;

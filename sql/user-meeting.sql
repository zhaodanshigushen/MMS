CREATE TABLE IF NOT EXISTS user_meeting(
uid     INT NOT NULL,
mid     INT NOT NULL, 
rel     BINARY(1) NOT NULL, #发起者为1，参与者为0
FOREIGN KEY (uid) REFERENCES user(uid),
FOREIGN KEY (mid) REFERENCES meeting(mid)
)DEFAULT CHARSET=utf8;

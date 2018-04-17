-- 权限表 --
CREATE TABLE permission (
pid int(11) not null AUTO_INCREMENT,
name VARCHAR(255) not NULL DEFAULT '',
url VARCHAR(255) DEFAULT '',
PRIMARY KEY (pid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT  INTO permission VALUES ('1','add','');
INSERT  INTO permission VALUES ('2','update','');
INSERT  INTO permission VALUES ('3','delete','');
INSERT  INTO permission VALUES ('4','edit','');

-- 用户表 --
CREATE TABLE USER (
 uid INT(11) not NULL AUTO_INCREMENT,
 username VARCHAR (255) Not NULL DEFAULT '',
 password VARCHAR (255) NOT NULL DEFAULT '',
 PRIMARY KEY (uid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO USER VALUES ('1','admin','admin');
INSERT INTO USER VALUES ('2','user','123456');

-- 角色表 --
CREATE TABLE role(
rid INT (11) not null AUTO_INCREMENT,
rname VARCHAR (255) not null DEFAULT '',
PRIMARY KEY (rid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO role VALUES ('1','admin');
INSERT INTO role VALUES ('2','user');

-- 角色权限关系表 --
CREATE TABLE permission_role(
 rid int(11) not null,
 pid int(11) not null,
 KEY idx_rid(rid),
 KEY idx_pid(pid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO permission_role VALUES ('1','1');
INSERT INTO permission_role VALUES ('1','2');
INSERT INTO permission_role VALUES ('1','3');
INSERT INTO permission_role VALUES ('1','4');
INSERT INTO permission_role VALUES ('2','1');
INSERT INTO permission_role VALUES ('2','4');

-- 用户角色关系表--
CREATE TABLE user_role(
 uid int(11) not null,
 rid int(11) not null,
 KEY idx_rid(uid),
 KEY idx_pid(rid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO user_role VALUES ('1','1');
INSERT INTO user_role VALUES ('2','2');
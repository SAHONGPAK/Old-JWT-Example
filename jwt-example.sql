DROP DATABASE IF EXISTS `jwt-example`;
CREATE DATABASE `jwt-example`;
USE `jwt-example`;

CREATE TABLE `user` (
	id VARCHAR(50) PRIMARY KEY,
	password VARCHAR(64) NOT NULL,
	name VARCHAR(20) UNIQUE NOT NULL,
	permission INT NOT NULL
);

CREATE TABLE `refresh-token` (
	user_id VARCHAR(50) PRIMARY KEY,
    token VARCHAR(500) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE `article` (
	id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(50) NOT NULL,
	contents VARCHAR(1000) NOT NULL,
	user_name VARCHAR(20) NOT NULL,
	create_date DATETIME NOT NULL,
	modify_date DATETIME NOT NULL,
	CONSTRAINT fk_user_name FOREIGN KEY(user_name) REFERENCES user(name)
);

INSERT INTO user VALUES('user', 'user', 'user', 0);
INSERT INTO user VALUES('admin', 'admin', 'admin', 1);

update user set password='user2' where password='user' and id='user';
select * from user;
select * from `refresh-token`;
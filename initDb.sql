drop user if exists 'anna'@'localhost';
create user 'anna'@'localhost' identified by 'clopotel';

DROP DATABASE IF EXISTS users_db;
CREATE DATABASE users_db;
grant all privileges on users_db.* to 'anna'@'localhost';
flush privileges;

USE users_db;

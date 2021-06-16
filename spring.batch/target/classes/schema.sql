DROP SCHEMA IF EXISTS employee_db;

CREATE SCHEMA employee_db;
USE employee_db;

DROP TABLE if exists person;

CREATE TABLE person (
	person_id BIGINT auto_increment NOT NULL PRIMARY KEY,
	first_name VARCHAR(40),
	last_name VARCHAR(40),
	email VARCHAR(100),
	age INT
);


DROP TABLE if exists client;

CREATE TABLE client (
	client_id BIGINT auto_increment NOT NULL PRIMARY KEY,
	client_name VARCHAR(40)
);
CREATE DATABASE food_organizer;
USE food_organizer;

CREATE TABLE employee(
                         employee_id VARCHAR(20)          NOT NULL
                             PRIMARY KEY,
                         name        VARCHAR(100)         NULL,
                         department  VARCHAR(25)          NULL,
                         deleted     TINYINT(1) DEFAULT 0 NULL
);

CREATE TABLE file_path(
                          id  INT AUTO_INCREMENT
                              PRIMARY KEY,
                          file_path VARCHAR(255) NUll
);

CREATE TABLE orders(
                       order_id INT AUTO_INCREMENT PRIMARY KEY ,
                       employee_id VARCHAR(20),
                       date DATE,
                       place_time TIME,
                       taken varchar(10),
                       taken_time TIME,
                       FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE custom_orders(
                      custom_order_id INT AUTO_INCREMENT PRIMARY KEY ,
                      description VARCHAR(100),
                      order_count INT(5),
                      date DATE,
                      place_time TIME
);
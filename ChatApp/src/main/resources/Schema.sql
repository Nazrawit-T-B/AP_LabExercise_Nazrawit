--- run the following from MySQL server
CREATE DATABASE chatapp;
USE chatapp;

CREATE TABLE messages (
                          id         INT AUTO_INCREMENT PRIMARY KEY,
                          sender     VARCHAR(50) NOT NULL,
                          type       ENUM('TEXT', 'IMAGE') NOT NULL,
                          content    TEXT,
                          image_data LONGBLOB,
                          sent_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
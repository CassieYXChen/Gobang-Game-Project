-- Ensure the database exists or create it if it does not
CREATE DATABASE IF NOT EXISTS `gobang`;
USE `gobang`;

-- Drop existing tables if they exist to prevent errors on creation
DROP TABLE IF EXISTS `address_user`, `chess_record`, `chess_user`, `sinfo`;

-- Create table for storing user addresses
CREATE TABLE `address_user` (
                                `id` INT(11) NOT NULL AUTO_INCREMENT,
                                `account` VARCHAR(15) NOT NULL,  -- User account identifier
                                `address` VARCHAR(15) NOT NULL,  -- User IP address, must be unique
                                `remember` TINYINT(1) NOT NULL,  -- Flag to remember password: 1 for yes, 0 for no
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `unique_address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create table for storing chessClass game records
CREATE TABLE `chess_record` (
                                `id` INT(11) NOT NULL AUTO_INCREMENT,
                                `black` VARCHAR(15) NOT NULL,  -- Account of the player with black pieces
                                `white` VARCHAR(15) NOT NULL,  -- Account of the player with white pieces
                                `chesstime` DATETIME NOT NULL,  -- Timestamp for when the game concluded
                                `result` CHAR(1) NOT NULL,      -- Game result: '1' for black wins, '2' for white wins, '3' for draw
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create table for storing user information
CREATE TABLE `chess_user` (
                              `id` INT(11) NOT NULL AUTO_INCREMENT,
                              `account` VARCHAR(15) NOT NULL,  -- User account, must be unique
                              `password` VARCHAR(32) NOT NULL, -- Hashed user password
                              `regtime` DATETIME NOT NULL,      -- Timestamp for user registration
                              `score` INT(11) NOT NULL,         -- Current score of the user
                              `totalnums` INT(11) NOT NULL,     -- Total games played
                              `winnums` INT(11) NOT NULL,       -- Games won
                              `lostnums` INT(11) NOT NULL,      -- Games lost
                              `drawnums` INT(11) NOT NULL,      -- Games drawn
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `unique_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create table for tracking session info
CREATE TABLE `sinfo` (
                         `id` INT(11) NOT NULL AUTO_INCREMENT,
                         `account` VARCHAR(15) NOT NULL,  -- User account, must be unique
                         `address` VARCHAR(15) NOT NULL,  -- User IP address
                         `status` TINYINT(1) NOT NULL,    -- Current status: 0 for offline, 1 for idle, 2 for in-game
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `unique_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema capstone_pod
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema capstone_pod
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `capstone_pod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `capstone_pod` ;

-- -----------------------------------------------------
-- Table `capstone_pod`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`category` (
                                                         `id` INT NOT NULL,
                                                         `image` VARCHAR(255) NULL DEFAULT NULL,
    `is_deleted` BIT(1) NOT NULL,
    `name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`discount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`discount` (
                                                         `id` INT NOT NULL,
                                                         `discount_percent` FLOAT NOT NULL,
                                                         `name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`product` (
                                                        `id` INT NOT NULL,
                                                        `create_date` DATE NULL DEFAULT NULL,
                                                        `last_modified_date` DATE NULL DEFAULT NULL,
                                                        `description` VARCHAR(255) NULL DEFAULT NULL,
    `is_deleted` BIT(1) NOT NULL,
    `is_public` BIT(1) NOT NULL,
    `name` VARCHAR(255) NULL DEFAULT NULL,
    `price` DOUBLE NOT NULL,
    `category_id` INT NULL DEFAULT NULL,
    `discount_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FK1mtsbur82frn64de7balymq9s` (`category_id` ASC) VISIBLE,
    INDEX `FKps2e0q9jpd0i9kj83je4rsmf1` (`discount_id` ASC) VISIBLE,
    CONSTRAINT `FK1mtsbur82frn64de7balymq9s`
    FOREIGN KEY (`category_id`)
    REFERENCES `capstone_pod`.`category` (`id`),
    CONSTRAINT `FKps2e0q9jpd0i9kj83je4rsmf1`
    FOREIGN KEY (`discount_id`)
    REFERENCES `capstone_pod`.`discount` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`blue_print`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`blue_print` (
                                                           `id` INT NOT NULL,
                                                           `frame_image` VARCHAR(255) NULL DEFAULT NULL,
    `position` VARCHAR(255) NULL DEFAULT NULL,
    `product_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKk11qlwkftqe4qnes7ig2yafvh` (`product_id` ASC) VISIBLE,
    CONSTRAINT `FKk11qlwkftqe4qnes7ig2yafvh`
    FOREIGN KEY (`product_id`)
    REFERENCES `capstone_pod`.`product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`color`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`color` (
                                                      `id` INT NOT NULL,
                                                      `name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`designed_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`designed_product` (
                                                                 `id` INT NOT NULL,
                                                                 `create_date` DATE NULL DEFAULT NULL,
                                                                 `last_modified_date` DATE NULL DEFAULT NULL,
                                                                 `designed_price` DOUBLE NOT NULL,
                                                                 `image` VARCHAR(255) NULL DEFAULT NULL,
    `name` VARCHAR(255) NULL DEFAULT NULL,
    `product_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKeor46gb2vt6k4p41wurmeo450` (`product_id` ASC) VISIBLE,
    CONSTRAINT `FKeor46gb2vt6k4p41wurmeo450`
    FOREIGN KEY (`product_id`)
    REFERENCES `capstone_pod`.`product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`design_blue_print`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`design_blue_print` (
                                                                  `id` INT NOT NULL,
                                                                  `blue_print_id` INT NULL DEFAULT NULL,
                                                                  `designed_product_id` INT NULL DEFAULT NULL,
                                                                  PRIMARY KEY (`id`),
    INDEX `FKpmhqh1olubjvncddf9o04x04g` (`blue_print_id` ASC) VISIBLE,
    INDEX `FKbdd9k753wat7lthm3fx6o8l4h` (`designed_product_id` ASC) VISIBLE,
    CONSTRAINT `FKbdd9k753wat7lthm3fx6o8l4h`
    FOREIGN KEY (`designed_product_id`)
    REFERENCES `capstone_pod`.`designed_product` (`id`),
    CONSTRAINT `FKpmhqh1olubjvncddf9o04x04g`
    FOREIGN KEY (`blue_print_id`)
    REFERENCES `capstone_pod`.`blue_print` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`tag` (
                                                    `id` INT NOT NULL,
                                                    `is_exist` BIT(1) NOT NULL,
    `name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`designed_product_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`designed_product_tag` (
                                                                     `id` INT NOT NULL,
                                                                     `designed_product_id` INT NULL DEFAULT NULL,
                                                                     `tag_id` INT NULL DEFAULT NULL,
                                                                     PRIMARY KEY (`id`),
    INDEX `FKeivmw9wmsoqjhix1fr8ndv557` (`designed_product_id` ASC) VISIBLE,
    INDEX `FK825nqjm9fis6l1m5tk87vpgoi` (`tag_id` ASC) VISIBLE,
    CONSTRAINT `FK825nqjm9fis6l1m5tk87vpgoi`
    FOREIGN KEY (`tag_id`)
    REFERENCES `capstone_pod`.`tag` (`id`),
    CONSTRAINT `FKeivmw9wmsoqjhix1fr8ndv557`
    FOREIGN KEY (`designed_product_id`)
    REFERENCES `capstone_pod`.`designed_product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`discount_time`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`discount_time` (
                                                              `id` INT NOT NULL,
                                                              `create_date` DATE NULL DEFAULT NULL,
                                                              `last_modified_date` DATE NULL DEFAULT NULL,
                                                              `end_date` DATE NULL DEFAULT NULL,
                                                              `is_expired` BIT(1) NOT NULL,
    `start_date` DATE NULL DEFAULT NULL,
    `discount_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKkcd898uu8dxsfvfgclqcjtpfg` (`discount_id` ASC) VISIBLE,
    CONSTRAINT `FKkcd898uu8dxsfvfgclqcjtpfg`
    FOREIGN KEY (`discount_id`)
    REFERENCES `capstone_pod`.`discount` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`hibernate_sequence` (
    `next_val` BIGINT NULL DEFAULT NULL)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`order_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`order_status` (
                                                             `id` INT NOT NULL,
                                                             `create_date` DATE NULL DEFAULT NULL,
                                                             `last_modified_date` DATE NULL DEFAULT NULL,
                                                             `status_name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`role` (
                                                     `id` INT NOT NULL,
                                                     `name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`user` (
                                                     `id` INT NOT NULL,
                                                     `create_date` DATE NULL DEFAULT NULL,
                                                     `last_modified_date` DATE NULL DEFAULT NULL,
                                                     `address` VARCHAR(255) NULL DEFAULT NULL,
    `avatar` VARCHAR(255) NULL DEFAULT NULL,
    `email` VARCHAR(255) NULL DEFAULT NULL,
    `first_name` VARCHAR(255) NULL DEFAULT NULL,
    `is_active` BIT(1) NOT NULL,
    `is_mail_verified` BIT(1) NOT NULL,
    `last_name` VARCHAR(255) NULL DEFAULT NULL,
    `password` VARCHAR(255) NULL DEFAULT NULL,
    `phone` VARCHAR(255) NULL DEFAULT NULL,
    `status` VARCHAR(255) NULL DEFAULT NULL,
    `username` VARCHAR(255) NULL DEFAULT NULL,
    `role_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id` ASC) VISIBLE,
    CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy`
    FOREIGN KEY (`role_id`)
    REFERENCES `capstone_pod`.`role` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`orders` (
                                                       `id` INT NOT NULL,
                                                       `create_date` DATE NULL DEFAULT NULL,
                                                       `last_modified_date` DATE NULL DEFAULT NULL,
                                                       `address` VARCHAR(255) NULL DEFAULT NULL,
    `customer_name` VARCHAR(255) NULL DEFAULT NULL,
    `phone` VARCHAR(255) NULL DEFAULT NULL,
    `price` DOUBLE NOT NULL,
    `order_status_id` INT NULL DEFAULT NULL,
    `user_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FK2n7p8t83wo7x0lep1q06a6cvy` (`order_status_id` ASC) VISIBLE,
    INDEX `FKel9kyl84ego2otj2accfd8mr7` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK2n7p8t83wo7x0lep1q06a6cvy`
    FOREIGN KEY (`order_status_id`)
    REFERENCES `capstone_pod`.`order_status` (`id`),
    CONSTRAINT `FKel9kyl84ego2otj2accfd8mr7`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_pod`.`user` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`order_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`order_detail` (
    `id` VARCHAR(255) NOT NULL,
    `quantity` VARCHAR(255) NULL DEFAULT NULL,
    `designed_product_id` INT NULL DEFAULT NULL,
    `orders_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKrm4hw5w55k4634tlhnhyiciiv` (`designed_product_id` ASC) VISIBLE,
    INDEX `FK7xf2gmq3yok90kilflnu8aa7e` (`orders_id` ASC) VISIBLE,
    CONSTRAINT `FK7xf2gmq3yok90kilflnu8aa7e`
    FOREIGN KEY (`orders_id`)
    REFERENCES `capstone_pod`.`orders` (`id`),
    CONSTRAINT `FKrm4hw5w55k4634tlhnhyiciiv`
    FOREIGN KEY (`designed_product_id`)
    REFERENCES `capstone_pod`.`designed_product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`placeholder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`placeholder` (
                                                            `id` INT NOT NULL,
                                                            `height` DOUBLE NOT NULL,
                                                            `left_coordinate` DOUBLE NOT NULL,
                                                            `top_coordinate` DOUBLE NOT NULL,
                                                            `width` DOUBLE NOT NULL,
                                                            `blue_print_id` INT NULL DEFAULT NULL,
                                                            PRIMARY KEY (`id`),
    INDEX `FKd3nbe4gk5ewrogucgqfby4qo6` (`blue_print_id` ASC) VISIBLE,
    CONSTRAINT `FKd3nbe4gk5ewrogucgqfby4qo6`
    FOREIGN KEY (`blue_print_id`)
    REFERENCES `capstone_pod`.`blue_print` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`product_images`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`product_images` (
                                                               `id` INT NOT NULL,
                                                               `image` VARCHAR(255) NULL DEFAULT NULL,
    `product_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKi8jnqq05sk5nkma3pfp3ylqrt` (`product_id` ASC) VISIBLE,
    CONSTRAINT `FKi8jnqq05sk5nkma3pfp3ylqrt`
    FOREIGN KEY (`product_id`)
    REFERENCES `capstone_pod`.`product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`product_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`product_tag` (
                                                            `id` INT NOT NULL,
                                                            `product_id` INT NULL DEFAULT NULL,
                                                            `tag_id` INT NULL DEFAULT NULL,
                                                            PRIMARY KEY (`id`),
    INDEX `FK2rf7w3d88x20p7vuc2m9mvv91` (`product_id` ASC) VISIBLE,
    INDEX `FK3b3a7hu5g2kh24wf0cwv3lgsm` (`tag_id` ASC) VISIBLE,
    CONSTRAINT `FK2rf7w3d88x20p7vuc2m9mvv91`
    FOREIGN KEY (`product_id`)
    REFERENCES `capstone_pod`.`product` (`id`),
    CONSTRAINT `FK3b3a7hu5g2kh24wf0cwv3lgsm`
    FOREIGN KEY (`tag_id`)
    REFERENCES `capstone_pod`.`tag` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`rating` (
                                                       `id` INT NOT NULL,
                                                       `comment` VARCHAR(255) NULL DEFAULT NULL,
    `rating_date` DATE NULL DEFAULT NULL,
    `rating_star` FLOAT NOT NULL,
    `designed_product_id` INT NULL DEFAULT NULL,
    `user_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKalwao11jcmob3wpik6raaxq0o` (`designed_product_id` ASC) VISIBLE,
    INDEX `FKpn05vbx6usw0c65tcyuce4dw5` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FKalwao11jcmob3wpik6raaxq0o`
    FOREIGN KEY (`designed_product_id`)
    REFERENCES `capstone_pod`.`designed_product` (`id`),
    CONSTRAINT `FKpn05vbx6usw0c65tcyuce4dw5`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_pod`.`user` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`shipping_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`shipping_info` (
                                                              `id` INT NOT NULL,
                                                              `email_address` VARCHAR(255) NULL DEFAULT NULL,
    `name` VARCHAR(255) NULL DEFAULT NULL,
    `phone_number` VARCHAR(255) NULL DEFAULT NULL,
    `shipping_address` VARCHAR(255) NULL DEFAULT NULL,
    `user_id` INT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FK6xc5qwgr4w4n5lssdfwug0ya4` (`user_id` ASC) VISIBLE,
    CONSTRAINT `FK6xc5qwgr4w4n5lssdfwug0ya4`
    FOREIGN KEY (`user_id`)
    REFERENCES `capstone_pod`.`user` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`size`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`size` (
                                                     `id` INT NOT NULL,
                                                     `name` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`size_color`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`size_color` (
                                                           `id` INT NOT NULL,
                                                           `color_id` INT NULL DEFAULT NULL,
                                                           `product_id` INT NULL DEFAULT NULL,
                                                           `size_id` INT NULL DEFAULT NULL,
                                                           PRIMARY KEY (`id`),
    INDEX `FKr20og5xkuy57sboi0k251qe` (`color_id` ASC) VISIBLE,
    INDEX `FKphjge8u8tektxx5dve1d2av76` (`product_id` ASC) VISIBLE,
    INDEX `FKasxyq899uwsqkd9yaxjybv1pf` (`size_id` ASC) VISIBLE,
    CONSTRAINT `FKasxyq899uwsqkd9yaxjybv1pf`
    FOREIGN KEY (`size_id`)
    REFERENCES `capstone_pod`.`size` (`id`),
    CONSTRAINT `FKphjge8u8tektxx5dve1d2av76`
    FOREIGN KEY (`product_id`)
    REFERENCES `capstone_pod`.`product` (`id`),
    CONSTRAINT `FKr20og5xkuy57sboi0k251qe`
    FOREIGN KEY (`color_id`)
    REFERENCES `capstone_pod`.`color` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

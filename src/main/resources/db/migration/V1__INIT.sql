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
-- Table `capstone_pod`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`user` (
                                                     `id` VARCHAR(36) NOT NULL,
                                                     `first_name` VARCHAR(255) NULL DEFAULT NULL,
                                                     `last_name` VARCHAR(255) NULL DEFAULT NULL,
                                                     `status` VARCHAR(255) NULL DEFAULT NULL,
                                                     PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`category` (
                                                         `id` VARCHAR(36) NOT NULL,
                                                         `image` VARCHAR(3000) NULL DEFAULT NULL,
                                                         `is_deleted` BIT(1) NOT NULL,
                                                         `name` VARCHAR(255) NULL DEFAULT NULL,
                                                         PRIMARY KEY (`id`),
                                                         UNIQUE INDEX `UK46ccwnsi9409t36lurvtyljak` (`name` ASC) VISIBLE,
                                                         INDEX `IDX46ccwnsi9409t36lurvtyljak` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`product` (
                                                        `id` VARCHAR(36) NOT NULL,
                                                        `create_date` DATETIME NULL DEFAULT NULL,
                                                        `last_modified_date` DATETIME NULL DEFAULT NULL,
                                                        `description` VARCHAR(255) NULL DEFAULT NULL,
                                                        `is_deleted` BIT(1) NOT NULL,
                                                        `is_public` BIT(1) NOT NULL,
                                                        `name` VARCHAR(255) NULL DEFAULT NULL,
                                                        `category_id` VARCHAR(36) NULL DEFAULT NULL,
                                                        PRIMARY KEY (`id`),
                                                        UNIQUE INDEX `UKjmivyxk9rmgysrmsqw15lqr5b` (`name` ASC) VISIBLE,
                                                        INDEX `IDXjlu2orma9568avd6hy2665fbd` (`name` ASC, `is_deleted` ASC, `is_public` ASC) VISIBLE,
                                                        INDEX `FK1mtsbur82frn64de7balymq9s` (`category_id` ASC) VISIBLE,
                                                        CONSTRAINT `FK1mtsbur82frn64de7balymq9s`
                                                            FOREIGN KEY (`category_id`)
                                                                REFERENCES `capstone_pod`.`category` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`factory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`factory` (
                                                        `id` VARCHAR(36) NOT NULL,
                                                        `is_collaborating` BIT(1) NOT NULL,
                                                        `location` VARCHAR(255) NULL DEFAULT NULL,
                                                        `name` VARCHAR(255) NULL DEFAULT NULL,
                                                        `trade_discount` DOUBLE NOT NULL,
                                                        PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`price_by_factory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`price_by_factory` (
                                                                 `id` VARCHAR(36) NOT NULL,
                                                                 `material` VARCHAR(255) NULL DEFAULT NULL,
                                                                 `price` DOUBLE NOT NULL,
                                                                 `factory_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                 `product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                 PRIMARY KEY (`id`),
                                                                 INDEX `FK62dp9k9lh0rq3a5aa5ww2ju6t` (`factory_id` ASC) VISIBLE,
                                                                 INDEX `FKlkmg7jg5eu9e9le6270j0wm4j` (`product_id` ASC) VISIBLE,
                                                                 CONSTRAINT `FK62dp9k9lh0rq3a5aa5ww2ju6t`
                                                                     FOREIGN KEY (`factory_id`)
                                                                         REFERENCES `capstone_pod`.`factory` (`id`),
                                                                 CONSTRAINT `FKlkmg7jg5eu9e9le6270j0wm4j`
                                                                     FOREIGN KEY (`product_id`)
                                                                         REFERENCES `capstone_pod`.`product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`designed_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`designed_product` (
                                                                 `id` VARCHAR(36) NOT NULL,
                                                                 `create_date` DATETIME NULL DEFAULT NULL,
                                                                 `last_modified_date` DATETIME NULL DEFAULT NULL,
                                                                 `description` VARCHAR(255) NULL DEFAULT NULL,
                                                                 `designed_price` DOUBLE NOT NULL,
                                                                 `name` VARCHAR(255) NULL DEFAULT NULL,
                                                                 `publish` BIT(1) NOT NULL,
                                                                 `publish1St` BIT(1) NOT NULL,
                                                                 `price_by_factory_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                 `product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                 `user_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                 PRIMARY KEY (`id`),
                                                                 INDEX `FKy63f7jxm32cewsttin3a7evg` (`price_by_factory_id` ASC) VISIBLE,
                                                                 INDEX `FKeor46gb2vt6k4p41wurmeo450` (`product_id` ASC) VISIBLE,
                                                                 INDEX `FK6i1c3o92skq48djifdy0ocre0` (`user_id` ASC) VISIBLE,
                                                                 CONSTRAINT `FK6i1c3o92skq48djifdy0ocre0`
                                                                     FOREIGN KEY (`user_id`)
                                                                         REFERENCES `capstone_pod`.`user` (`id`),
                                                                 CONSTRAINT `FKeor46gb2vt6k4p41wurmeo450`
                                                                     FOREIGN KEY (`product_id`)
                                                                         REFERENCES `capstone_pod`.`product` (`id`),
                                                                 CONSTRAINT `FKy63f7jxm32cewsttin3a7evg`
                                                                     FOREIGN KEY (`price_by_factory_id`)
                                                                         REFERENCES `capstone_pod`.`price_by_factory` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`blue_print`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`blue_print` (
                                                           `id` VARCHAR(36) NOT NULL,
                                                           `frame_image` VARCHAR(255) NULL DEFAULT NULL,
                                                           `position` VARCHAR(255) NULL DEFAULT NULL,
                                                           `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                           PRIMARY KEY (`id`),
                                                           INDEX `FKf3yfxp808fpnm4gtbbxmh6fc7` (`designed_product_id` ASC) VISIBLE,
                                                           CONSTRAINT `FKf3yfxp808fpnm4gtbbxmh6fc7`
                                                               FOREIGN KEY (`designed_product_id`)
                                                                   REFERENCES `capstone_pod`.`designed_product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`cart` (
                                                     `id` VARCHAR(36) NOT NULL,
                                                     `user_id` VARCHAR(36) NULL DEFAULT NULL,
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `unique_user` (`user_id` ASC) VISIBLE,
                                                     CONSTRAINT `FKl70asp4l4w0jmbm1tqyofho4o`
                                                         FOREIGN KEY (`user_id`)
                                                             REFERENCES `capstone_pod`.`user` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`cart_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`cart_detail` (
                                                            `id` VARCHAR(36) NOT NULL,
                                                            `color` VARCHAR(255) NULL DEFAULT NULL,
                                                            `quantity` INT NOT NULL,
                                                            `size` VARCHAR(255) NULL DEFAULT NULL,
                                                            `cart_id` VARCHAR(36) NULL DEFAULT NULL,
                                                            `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                            PRIMARY KEY (`id`),
                                                            INDEX `FKrg4yopd2252nwj8bfcgq5f4jp` (`cart_id` ASC) VISIBLE,
                                                            INDEX `FKk3vd994ail3ndk6uitppwrhu5` (`designed_product_id` ASC) VISIBLE,
                                                            CONSTRAINT `FKk3vd994ail3ndk6uitppwrhu5`
                                                                FOREIGN KEY (`designed_product_id`)
                                                                    REFERENCES `capstone_pod`.`designed_product` (`id`),
                                                            CONSTRAINT `FKrg4yopd2252nwj8bfcgq5f4jp`
                                                                FOREIGN KEY (`cart_id`)
                                                                    REFERENCES `capstone_pod`.`cart` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`color`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`color` (
                                                      `id` VARCHAR(36) NOT NULL,
                                                      `image_color` VARCHAR(255) NULL DEFAULT NULL,
                                                      `name` VARCHAR(255) NULL DEFAULT NULL,
                                                      PRIMARY KEY (`id`),
                                                      UNIQUE INDEX `UKn3axgangk6yuxhrb2o7fk9oa7` (`name` ASC) VISIBLE,
                                                      INDEX `IDXn3axgangk6yuxhrb2o7fk9oa7` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`role` (
                                                     `id` VARCHAR(36) NOT NULL,
                                                     `name` VARCHAR(255) NULL DEFAULT NULL,
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `UK8sewwnpamngi6b1dwaa88askk` (`name` ASC) VISIBLE,
                                                     INDEX `IDX8sewwnpamngi6b1dwaa88askk` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`credential`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`credential` (
                                                           `id` VARCHAR(36) NOT NULL,
                                                           `create_date` DATE NULL DEFAULT NULL,
                                                           `last_modified_date` DATE NULL DEFAULT NULL,
                                                           `address` VARCHAR(255) NULL DEFAULT NULL,
                                                           `email` VARCHAR(255) NULL DEFAULT NULL,
                                                           `image` VARCHAR(3000) NULL DEFAULT NULL,
                                                           `is_mail_verified` BIT(1) NOT NULL,
                                                           `password` VARCHAR(255) NULL DEFAULT NULL,
                                                           `phone` VARCHAR(255) NULL DEFAULT NULL,
                                                           `factory_id` VARCHAR(36) NULL DEFAULT NULL,
                                                           `role_id` VARCHAR(36) NULL DEFAULT NULL,
                                                           `user_id` VARCHAR(36) NULL DEFAULT NULL,
                                                           PRIMARY KEY (`id`),
                                                           UNIQUE INDEX `UK87sfas6wh5j3lvj191bnt1s2y` (`email` ASC) VISIBLE,
                                                           INDEX `IDXim72bqfq0ipw8sa7k9odt4sfj` (`email` ASC, `phone` ASC) VISIBLE,
                                                           INDEX `FK1x6hukbi79vc4gcki0hf3hkkj` (`factory_id` ASC) VISIBLE,
                                                           INDEX `FKn0841dps0wpkedi6keaqh92kp` (`role_id` ASC) VISIBLE,
                                                           INDEX `FKpg7bdnqxpyhrt7f8soul9y7ne` (`user_id` ASC) VISIBLE,
                                                           CONSTRAINT `FK1x6hukbi79vc4gcki0hf3hkkj`
                                                               FOREIGN KEY (`factory_id`)
                                                                   REFERENCES `capstone_pod`.`factory` (`id`),
                                                           CONSTRAINT `FKn0841dps0wpkedi6keaqh92kp`
                                                               FOREIGN KEY (`role_id`)
                                                                   REFERENCES `capstone_pod`.`role` (`id`),
                                                           CONSTRAINT `FKpg7bdnqxpyhrt7f8soul9y7ne`
                                                               FOREIGN KEY (`user_id`)
                                                                   REFERENCES `capstone_pod`.`user` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`design_color`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`design_color` (
                                                             `id` VARCHAR(36) NOT NULL,
                                                             `color_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             PRIMARY KEY (`id`),
                                                             INDEX `FKr3abfh8wr83cwh2squnq6btcp` (`color_id` ASC) VISIBLE,
                                                             INDEX `FK8t1np3l29jjjscdvxy85m5a9y` (`designed_product_id` ASC) VISIBLE,
                                                             CONSTRAINT `FK8t1np3l29jjjscdvxy85m5a9y`
                                                                 FOREIGN KEY (`designed_product_id`)
                                                                     REFERENCES `capstone_pod`.`designed_product` (`id`),
                                                             CONSTRAINT `FKr3abfh8wr83cwh2squnq6btcp`
                                                                 FOREIGN KEY (`color_id`)
                                                                     REFERENCES `capstone_pod`.`color` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`design_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`design_info` (
                                                            `id` VARCHAR(36) NOT NULL,
                                                            `font` VARCHAR(255) NULL DEFAULT NULL,
                                                            `height` DOUBLE NOT NULL,
                                                            `left_position` DOUBLE NOT NULL,
                                                            `name` VARCHAR(255) NULL DEFAULT NULL,
                                                            `rotate` DOUBLE NOT NULL,
                                                            `scales` DOUBLE NOT NULL,
                                                            `src` VARCHAR(3000) NULL DEFAULT NULL,
                                                            `text_color` VARCHAR(255) NULL DEFAULT NULL,
                                                            `top_position` DOUBLE NOT NULL,
                                                            `types` VARCHAR(255) NULL DEFAULT NULL,
                                                            `width` DOUBLE NOT NULL,
                                                            `blue_print_id` VARCHAR(36) NULL DEFAULT NULL,
                                                            PRIMARY KEY (`id`),
                                                            INDEX `FKh5uyam68u3624g7gqf7ysnbuh` (`blue_print_id` ASC) VISIBLE,
                                                            CONSTRAINT `FKh5uyam68u3624g7gqf7ysnbuh`
                                                                FOREIGN KEY (`blue_print_id`)
                                                                    REFERENCES `capstone_pod`.`blue_print` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`tag` (
                                                    `id` VARCHAR(36) NOT NULL,
                                                    `is_exist` BIT(1) NOT NULL,
                                                    `name` VARCHAR(255) NULL DEFAULT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `UK1wdpsed5kna2y38hnbgrnhi5b` (`name` ASC) VISIBLE,
                                                    INDEX `IDX1wdpsed5kna2y38hnbgrnhi5b` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`designed_product_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`designed_product_tag` (
                                                                     `id` VARCHAR(36) NOT NULL,
                                                                     `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                     `tag_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                    `createdDate` DATETIME NULL DEFAULT NULL,
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
-- Table `capstone_pod`.`flyway_schema_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`flyway_schema_history` (
                                                                      `installed_rank` INT NOT NULL,
                                                                      `version` VARCHAR(50) NULL DEFAULT NULL,
                                                                      `description` VARCHAR(200) NOT NULL,
                                                                      `type` VARCHAR(20) NOT NULL,
                                                                      `script` VARCHAR(1000) NOT NULL,
                                                                      `checksum` INT NULL DEFAULT NULL,
                                                                      `installed_by` VARCHAR(100) NOT NULL,
                                                                      `installed_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                                      `execution_time` INT NOT NULL,
                                                                      `success` TINYINT(1) NOT NULL,
                                                                      PRIMARY KEY (`installed_rank`),
                                                                      INDEX `flyway_schema_history_s_idx` (`success` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`image_preview`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`image_preview` (
                                                              `id` VARCHAR(36) NOT NULL,
                                                              `color` VARCHAR(255) NULL DEFAULT NULL,
                                                              `image` VARCHAR(3000) NULL DEFAULT NULL,
                                                              `position` VARCHAR(255) NULL DEFAULT NULL,
                                                              `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                              PRIMARY KEY (`id`),
                                                              INDEX `FKkhpt0kvab2o7vns3ikvne5fk9` (`designed_product_id` ASC) VISIBLE,
                                                              CONSTRAINT `FKkhpt0kvab2o7vns3ikvne5fk9`
                                                                  FOREIGN KEY (`designed_product_id`)
                                                                      REFERENCES `capstone_pod`.`designed_product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`material`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`material` (
                                                         `id` VARCHAR(36) NOT NULL,
                                                         `create_date` DATETIME NULL DEFAULT NULL,
                                                         `last_modified_date` DATETIME NULL DEFAULT NULL,
                                                         `name` VARCHAR(255) NULL DEFAULT NULL,
                                                         PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`orders` (
                                                       `id` VARCHAR(36) NOT NULL,
                                                       `create_date` DATETIME NULL DEFAULT NULL,
                                                       `last_modified_date` DATETIME NULL DEFAULT NULL,
                                                       `address` VARCHAR(255) NULL DEFAULT NULL,
                                                       `app_trans_id` VARCHAR(255) NULL DEFAULT NULL,
                                                       `cancel_reason` VARCHAR(255) NULL DEFAULT NULL,
                                                       `canceled` BIT(1) NOT NULL,
                                                       `customer_name` VARCHAR(255) NULL DEFAULT NULL,
                                                       `is_paid` BIT(1) NOT NULL,
                                                       `refunded` BIT(1) NOT NULL,
                                                       `phone` VARCHAR(255) NULL DEFAULT NULL,
                                                       `price` DOUBLE NOT NULL,
                                                       `transaction_id` VARCHAR(255) NULL DEFAULT NULL,
                                                       `user_id` VARCHAR(36) NULL DEFAULT NULL,
                                                       PRIMARY KEY (`id`),
                                                       INDEX `FKel9kyl84ego2otj2accfd8mr7` (`user_id` ASC) VISIBLE,
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
                                                             `id` VARCHAR(36) NOT NULL,
                                                             `canceled` BIT(1) NOT NULL,
                                                             `color` VARCHAR(255) NULL DEFAULT NULL,
                                                             `is_rate` BIT(1) NOT NULL,
                                                             `quantity` INT NOT NULL,
                                                             `reason` VARCHAR(255) NULL DEFAULT NULL,
                                                             `size` VARCHAR(255) NULL DEFAULT NULL,
                                                             `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             `factory_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             `orders_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             `reason_by_factory` VARCHAR(255) NULL DEFAULT NULL,
                                                             `reason_by_user` VARCHAR(255) NULL DEFAULT NULL,
                                                             PRIMARY KEY (`id`),
                                                             INDEX `FKrm4hw5w55k4634tlhnhyiciiv` (`designed_product_id` ASC) VISIBLE,
                                                             INDEX `FKqj83wbodxud24dmcp8q7cftem` (`factory_id` ASC) VISIBLE,
                                                             INDEX `FK7xf2gmq3yok90kilflnu8aa7e` (`orders_id` ASC) VISIBLE,
                                                             CONSTRAINT `FK7xf2gmq3yok90kilflnu8aa7e`
                                                                 FOREIGN KEY (`orders_id`)
                                                                     REFERENCES `capstone_pod`.`orders` (`id`),
                                                             CONSTRAINT `FKqj83wbodxud24dmcp8q7cftem`
                                                                 FOREIGN KEY (`factory_id`)
                                                                     REFERENCES `capstone_pod`.`factory` (`id`),
                                                             CONSTRAINT `FKrm4hw5w55k4634tlhnhyiciiv`
                                                                 FOREIGN KEY (`designed_product_id`)
                                                                     REFERENCES `capstone_pod`.`designed_product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`order_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`order_status` (
                                                             `id` VARCHAR(36) NOT NULL,
                                                             `create_date` DATETIME NULL DEFAULT NULL,
                                                             `last_modified_date` DATETIME NULL DEFAULT NULL,
                                                             `name` VARCHAR(255) NULL DEFAULT NULL,
                                                             `order_detail_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             PRIMARY KEY (`id`),
                                                             INDEX `FKm1pbooh547jg5x2s01lv37i12` (`order_detail_id` ASC) VISIBLE,
                                                             CONSTRAINT `FKm1pbooh547jg5x2s01lv37i12`
                                                                 FOREIGN KEY (`order_detail_id`)
                                                                     REFERENCES `capstone_pod`.`order_detail` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`placeholder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`placeholder` (
                                                            `id` VARCHAR(36) NOT NULL,
                                                            `height` DOUBLE NOT NULL,
                                                            `height_rate` DOUBLE NOT NULL,
                                                            `top` DOUBLE NOT NULL,
                                                            `width` DOUBLE NOT NULL,
                                                            `width_rate` DOUBLE NOT NULL,
                                                            `blue_print_id` VARCHAR(36) NULL DEFAULT NULL,
                                                            PRIMARY KEY (`id`),
                                                            INDEX `FKd3nbe4gk5ewrogucgqfby4qo6` (`blue_print_id` ASC) VISIBLE,
                                                            CONSTRAINT `FKd3nbe4gk5ewrogucgqfby4qo6`
                                                                FOREIGN KEY (`blue_print_id`)
                                                                    REFERENCES `capstone_pod`.`blue_print` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`printing_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`printing_info` (
                                                              `id` VARCHAR(36) NOT NULL,
                                                              `order_detail_id` VARCHAR(36) NULL DEFAULT NULL,
                                                              PRIMARY KEY (`id`),
                                                              INDEX `FK3fsw8cuhymi04iefc8lc4yplr` (`order_detail_id` ASC) VISIBLE,
                                                              CONSTRAINT `FK3fsw8cuhymi04iefc8lc4yplr`
                                                                  FOREIGN KEY (`order_detail_id`)
                                                                      REFERENCES `capstone_pod`.`order_detail` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`printing_blue_print`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`printing_blue_print` (
                                                                    `id` VARCHAR(36) NOT NULL,
                                                                    `frame_image` VARCHAR(255) NULL DEFAULT NULL,
                                                                    `position` VARCHAR(255) NULL DEFAULT NULL,
                                                                    `printing_info_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                    PRIMARY KEY (`id`),
                                                                    INDEX `FK4khsfm7v35pp978wsl29w6cw1` (`printing_info_id` ASC) VISIBLE,
                                                                    CONSTRAINT `FK4khsfm7v35pp978wsl29w6cw1`
                                                                        FOREIGN KEY (`printing_info_id`)
                                                                            REFERENCES `capstone_pod`.`printing_info` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`printing_design_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`printing_design_info` (
                                                                     `id` VARCHAR(36) NOT NULL,
                                                                     `font` VARCHAR(255) NULL DEFAULT NULL,
                                                                     `height` DOUBLE NOT NULL,
                                                                     `left_position` DOUBLE NOT NULL,
                                                                     `name` VARCHAR(255) NULL DEFAULT NULL,
                                                                     `rotate` DOUBLE NOT NULL,
                                                                     `scales` DOUBLE NOT NULL,
                                                                     `src` VARCHAR(3000) NULL DEFAULT NULL,
                                                                     `text_color` VARCHAR(255) NULL DEFAULT NULL,
                                                                     `top_position` DOUBLE NOT NULL,
                                                                     `types` VARCHAR(255) NULL DEFAULT NULL,
                                                                     `width` DOUBLE NOT NULL,
                                                                     `printing_blue_print_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                     PRIMARY KEY (`id`),
                                                                     INDEX `FK7kfb2kuvbk6ss3an2ewi6qb7e` (`printing_blue_print_id` ASC) VISIBLE,
                                                                     CONSTRAINT `FK7kfb2kuvbk6ss3an2ewi6qb7e`
                                                                         FOREIGN KEY (`printing_blue_print_id`)
                                                                             REFERENCES `capstone_pod`.`printing_blue_print` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`printing_image_preview`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`printing_image_preview` (
                                                                       `id` VARCHAR(36) NOT NULL,
                                                                       `color` VARCHAR(255) NULL DEFAULT NULL,
                                                                       `image` VARCHAR(3000) NULL DEFAULT NULL,
                                                                       `position` VARCHAR(255) NULL DEFAULT NULL,
                                                                       `printing_info_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                       PRIMARY KEY (`id`),
                                                                       INDEX `FKhgyem84ts7oorhikae1pvcd3m` (`printing_info_id` ASC) VISIBLE,
                                                                       CONSTRAINT `FKhgyem84ts7oorhikae1pvcd3m`
                                                                           FOREIGN KEY (`printing_info_id`)
                                                                               REFERENCES `capstone_pod`.`printing_info` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`printing_placeholder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`printing_placeholder` (
                                                                     `id` VARCHAR(36) NOT NULL,
                                                                     `height` DOUBLE NOT NULL,
                                                                     `height_rate` DOUBLE NOT NULL,
                                                                     `top` DOUBLE NOT NULL,
                                                                     `width` DOUBLE NOT NULL,
                                                                     `width_rate` DOUBLE NOT NULL,
                                                                     `printing_blue_print_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                     PRIMARY KEY (`id`),
                                                                     INDEX `FKisa7vq2mntguoqpdho0gpbqby` (`printing_blue_print_id` ASC) VISIBLE,
                                                                     CONSTRAINT `FKisa7vq2mntguoqpdho0gpbqby`
                                                                         FOREIGN KEY (`printing_blue_print_id`)
                                                                             REFERENCES `capstone_pod`.`printing_blue_print` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`product_blue_print`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`product_blue_print` (
                                                                   `id` VARCHAR(36) NOT NULL,
                                                                   `frame_image` VARCHAR(255) NULL DEFAULT NULL,
                                                                   `height_rate` DOUBLE NOT NULL,
                                                                   `place_holder_height` DOUBLE NOT NULL,
                                                                   `place_holder_top` DOUBLE NOT NULL,
                                                                   `place_holder_width` DOUBLE NOT NULL,
                                                                   `position` VARCHAR(255) NULL DEFAULT NULL,
                                                                   `width_rate` DOUBLE NOT NULL,
                                                                   `product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                   PRIMARY KEY (`id`),
                                                                   INDEX `FK5xjg0h2wljlori7vvag6oh24t` (`product_id` ASC) VISIBLE,
                                                                   CONSTRAINT `FK5xjg0h2wljlori7vvag6oh24t`
                                                                       FOREIGN KEY (`product_id`)
                                                                           REFERENCES `capstone_pod`.`product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`product_images`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`product_images` (
                                                               `id` VARCHAR(36) NOT NULL,
                                                               `image` VARCHAR(3000) NULL DEFAULT NULL,
                                                               `product_id` VARCHAR(36) NULL DEFAULT NULL,
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
                                                            `id` VARCHAR(36) NOT NULL,
                                                            `product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                            `tag_id` VARCHAR(36) NULL DEFAULT NULL,
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
                                                       `id` VARCHAR(36) NOT NULL,
                                                       `comment` VARCHAR(255) NULL DEFAULT NULL,
                                                       `rating_date` DATE NULL DEFAULT NULL,
                                                       `rating_star` FLOAT NOT NULL,
                                                       `designed_product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                       `user_id` VARCHAR(36) NULL DEFAULT NULL,
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
                                                              `id` VARCHAR(36) NOT NULL,
                                                              `email_address` VARCHAR(255) NULL DEFAULT NULL,
                                                              `name` VARCHAR(255) NULL DEFAULT NULL,
                                                              `phone_number` VARCHAR(255) NULL DEFAULT NULL,
                                                              `shipping_address` VARCHAR(255) NULL DEFAULT NULL,
                                                              `user_id` VARCHAR(36) NULL DEFAULT NULL,
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
                                                     `id` VARCHAR(36) NOT NULL,
                                                     `name` VARCHAR(255) NULL DEFAULT NULL,
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `UK8mx44qvbn71lwrou3igoc1nwm` (`name` ASC) VISIBLE,
                                                     INDEX `IDX8mx44qvbn71lwrou3igoc1nwm` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`size_color`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`size_color` (
                                                           `id` VARCHAR(36) NOT NULL,
                                                           `color_id` VARCHAR(36) NULL DEFAULT NULL,
                                                           `product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                           `size_id` VARCHAR(36) NULL DEFAULT NULL,
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


-- -----------------------------------------------------
-- Table `capstone_pod`.`size_color_by_factory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`size_color_by_factory` (
                                                                      `id` VARCHAR(36) NOT NULL,
                                                                      `quantity` INT NOT NULL,
                                                                      `factory_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                      `size_color_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                      PRIMARY KEY (`id`),
                                                                      INDEX `FKpb3ltk705b1sm8sxrixsilin4` (`factory_id` ASC) VISIBLE,
                                                                      INDEX `FK1j4j2vs9ack4yuqqolb02l5ow` (`size_color_id` ASC) VISIBLE,
                                                                      CONSTRAINT `FK1j4j2vs9ack4yuqqolb02l5ow`
                                                                          FOREIGN KEY (`size_color_id`)
                                                                              REFERENCES `capstone_pod`.`size_color` (`id`),
                                                                      CONSTRAINT `FKpb3ltk705b1sm8sxrixsilin4`
                                                                          FOREIGN KEY (`factory_id`)
                                                                              REFERENCES `capstone_pod`.`factory` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`size_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`size_product` (
                                                             `id` VARCHAR(36) NOT NULL,
                                                             `height` DOUBLE NOT NULL,
                                                             `size` VARCHAR(255) NULL DEFAULT NULL,
                                                             `width` DOUBLE NOT NULL,
                                                             `product_id` VARCHAR(36) NULL DEFAULT NULL,
                                                             PRIMARY KEY (`id`),
                                                             INDEX `FKmnesxf69qwvk7og5n8klp4q9m` (`product_id` ASC) VISIBLE,
                                                             CONSTRAINT `FKmnesxf69qwvk7og5n8klp4q9m`
                                                                 FOREIGN KEY (`product_id`)
                                                                     REFERENCES `capstone_pod`.`product` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `capstone_pod`.`verification_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `capstone_pod`.`verification_token` (
                                                                   `id` VARCHAR(36) NOT NULL,
                                                                   `expiry_date` DATETIME NULL DEFAULT NULL,
                                                                   `token` VARCHAR(255) NULL DEFAULT NULL,
                                                                   `credential_id` VARCHAR(36) NULL DEFAULT NULL,
                                                                   PRIMARY KEY (`id`),
                                                                   INDEX `FKmsnuyxtj0395n1s1y9qaw6pr5` (`credential_id` ASC) VISIBLE,
                                                                   CONSTRAINT `FKmsnuyxtj0395n1s1y9qaw6pr5`
                                                                       FOREIGN KEY (`credential_id`)
                                                                           REFERENCES `capstone_pod`.`credential` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

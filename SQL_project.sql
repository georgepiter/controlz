-- MySQL Workbench Synchronization
-- Generated: 2023-03-17 19:49
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: georg

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `controlz` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS `controlz`.`register` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `registration_date` DATE NULL DEFAULT NULL,
  `cell` VARCHAR(15) NULL DEFAULT NULL,
  `others` DECIMAL(10,2) NULL DEFAULT NULL,
  `salary` DECIMAL(10,2) NULL DEFAULT NULL,
  `photo` BLOB(3145728) NULL DEFAULT NULL,
  `update_date` DATE NULL DEFAULT NULL,
  `user_id` BIGINT(0) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_register_user2_idx` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  CONSTRAINT `fk_register_user2`
    FOREIGN KEY (`user_id`)
    REFERENCES `controlz`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`debt_control` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `input_date` DATE NULL DEFAULT NULL,
  `debt_description` VARCHAR(80) NULL DEFAULT NULL,
  `value` DECIMAL(10,2) NULL DEFAULT NULL,
  `register_id` BIGINT(0) NOT NULL,
  `status` INT(11) NOT NULL,
  `payment_date` DATE NULL DEFAULT NULL,
  `receipt_payment` BLOB(3145728) NULL DEFAULT NULL,
  `due_date` DATE NOT NULL,
  `category_id` BIGINT(0) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_debt_control_register_idx` (`register_id` ASC) VISIBLE,
  INDEX `fk_debt_control_category1_idx` (`category_id` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  CONSTRAINT `fk_debt_control_register`
    FOREIGN KEY (`register_id`)
    REFERENCES `controlz`.`register` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_debt_control_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `controlz`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`user` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(80) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `role_id` BIGINT(0) NOT NULL,
  `status` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_user_role1_idx` (`role_id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `controlz`.`role` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`role` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`email` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `email_status` INT(11) NULL DEFAULT NULL,
  `id_template` BIGINT(0) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `subject` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`email_property` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email_property_key` VARCHAR(45) NULL DEFAULT NULL,
  `email_property_value` VARCHAR(45) NULL DEFAULT NULL,
  `email_id` BIGINT(0) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_email_property_email1_idx` (`email_id` ASC) VISIBLE,
  CONSTRAINT `fk_email_property_email1`
    FOREIGN KEY (`email_id`)
    REFERENCES `controlz`.`email` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`category` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`financial_history` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `total_credit` DECIMAL(10,2) NOT NULL,
  `total_debt` DECIMAL(10,2) NOT NULL,
  `register_id` BIGINT(0) NOT NULL,
  `period` VARCHAR(9) NOT NULL,
  `balance_credit` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_historic_register1_idx` (`register_id` ASC) VISIBLE,
  CONSTRAINT `fk_historic_register1`
    FOREIGN KEY (`register_id`)
    REFERENCES `controlz`.`register` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

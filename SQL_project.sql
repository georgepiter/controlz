-- MySQL Workbench Synchronization
-- Generated: 2023-01-15 10:45
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: george piter

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER SCHEMA `controlz`  DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `controlz`.`register` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `registration_date` DATE NULL DEFAULT NULL,
  `email` VARCHAR(50) NULL DEFAULT NULL,
  `cell` VARCHAR(15) NULL DEFAULT NULL,
  `others` DOUBLE NULL DEFAULT NULL,
  `salary` DOUBLE NULL DEFAULT NULL,
  `photo` BLOB NULL DEFAULT NULL,
  `update_date` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `controlz`.`debt_control` (
  `id` BIGINT(0) NOT NULL AUTO_INCREMENT,
  `input_date` DATE NULL DEFAULT NULL,
  `debt_description` VARCHAR(80) NULL DEFAULT NULL,
  `value` DOUBLE NULL DEFAULT NULL,
  `register_id` BIGINT(0) NOT NULL,
  `status` INT(11) NOT NULL,
  `payment_date` DATE NULL DEFAULT NULL,
  `receipt_payment` BLOB NULL DEFAULT NULL,
  `due_date` DATE NOT NULL,
  `category_id` BIGINT(0) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_debt_control_register_idx` (`register_id` ASC) VISIBLE,
  UNIQUE INDEX `register_id_UNIQUE` (`register_id` ASC) VISIBLE,
  INDEX `fk_debt_control_category1_idx` (`category_id` ASC) VISIBLE,
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
    ON DELETE NO ACTION
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

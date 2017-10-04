-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema agendadb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema agendadb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `agendadb` DEFAULT CHARACTER SET utf8 ;
USE `agendadb` ;

-- -----------------------------------------------------
-- Table `agendadb`.`Agenda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agendadb`.`Agenda` (
  `nome` VARCHAR(26) NOT NULL,
  `telefone` VARCHAR(11) NOT NULL,
  PRIMARY KEY (`nome`),
  UNIQUE INDEX `telefone_UNIQUE` (`telefone` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

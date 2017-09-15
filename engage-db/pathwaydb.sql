/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.27-log : Database - pathwaydb
*********************************************************************
*/ 
/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`pathwaydb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `pathwaydb`;

/*Table structure for table `qc_pathway` */

DROP TABLE IF EXISTS `qc_pathway`;

CREATE TABLE `qc_pathway` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `org_id` INT(10) NOT NULL,
  `team_id` INT(10) NOT NULL,
  `Pathway_name` VARCHAR(250) NOT NULL,
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` ENUM('Y','N') DEFAULT 'Y',
  `gap_between_messages` INT(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=244 DEFAULT CHARSET=latin1;




/*Table structure for table `qc_pathway_event` */

DROP TABLE IF EXISTS `qc_pathway_event`;

CREATE TABLE `qc_pathway_event` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `pathway_id` INT(10) DEFAULT NULL,
  `event_name` VARCHAR(250) DEFAULT NULL,
  `event_pos_row` INT(10) NOT NULL,
  `event_pos_col` INT(10) NOT NULL DEFAULT '0',
  `status` ENUM('Y','N') DEFAULT 'Y',
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_pathway_id` (`pathway_id`),
  CONSTRAINT `fk_pathway_id` FOREIGN KEY (`pathway_id`) REFERENCES `qc_pathway` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=493 DEFAULT CHARSET=latin1;



/*Table structure for table `qc_pathway_block` */

DROP TABLE IF EXISTS `qc_pathway_block`;

CREATE TABLE `qc_pathway_block` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `pathway_id` INT(10) NOT NULL DEFAULT '0',
  `block_name` VARCHAR(250) NOT NULL,
  `block_type` ENUM('M','A') NOT NULL DEFAULT 'M',
  `phi_secured` VARCHAR(100) NOT NULL,
  `block_appointment_parent` INT(10) NOT NULL,
  `block_pos_row` INT(10) NOT NULL DEFAULT '0',
  `block_pos_col` INT(10) NOT NULL DEFAULT '0',
  `trigger_id` INT(10) NOT NULL DEFAULT '0',
  `delivery_days_after_trigger` INT(10) NOT NULL DEFAULT '0',
  `repeat_for_number_of_days` INT(10) NOT NULL DEFAULT '0',
  `subject_of_message` VARCHAR(250) DEFAULT NULL,
  `body_of_message` TEXT,
  `remainder_of_message` TEXT NOT NULL,
  `followup_of_message` TEXT NOT NULL,
  `status` ENUM('Y','N') NOT NULL DEFAULT 'Y',
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `event_id` INT(10) DEFAULT NULL,
  `no_of_occurence` INT(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_event_id_idx` (`event_id`),
  CONSTRAINT `fk_event_id` FOREIGN KEY (`event_id`) REFERENCES `qc_pathway_event` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=1337 DEFAULT CHARSET=latin1;

 


/*Table structure for table `qc_pathway_patient_blocks` */


DROP TABLE IF EXISTS `qc_pathway_patient_blocks`;

CREATE TABLE `qc_pathway_patient_blocks` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `pathway_id` INT(10) NOT NULL DEFAULT '0',
  `patient_id` INT(10) NOT NULL,
  `block_id` INT(10) NOT NULL,
  `block_appointment_date` DATE NOT NULL,
  `block_parent_id` INT(10) NOT NULL,
  `block_name` VARCHAR(250) NOT NULL,
  `block_type` ENUM('M','A') NOT NULL DEFAULT 'M',
  `block_pos_row` INT(10) NOT NULL DEFAULT '0',
  `message_send_at` DATE NOT NULL,
  `message_status` VARCHAR(150) NOT NULL,
  `patient_accepted_date` DATE NOT NULL,
  `block_pos_col` INT(10) NOT NULL DEFAULT '0',
  `trigger_id` INT(10) NOT NULL DEFAULT '0',
  `delivery_days_after_trigger` INT(10) NOT NULL DEFAULT '0',
  `repeat_for_number_of_days` INT(10) NOT NULL DEFAULT '0',
  `subject_of_message` VARCHAR(250) DEFAULT NULL,
  `body_of_message` TEXT,
  `remainder_of_message` TEXT NOT NULL,
  `followup_of_message` TEXT NOT NULL,
  `status` ENUM('Y','N') NOT NULL DEFAULT 'Y',
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `event_id` INT(10) DEFAULT NULL,
  `phi_secured` VARCHAR(45) DEFAULT 'no',
  `msenttime` BIGINT(50) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=217 DEFAULT CHARSET=latin1;

/*Data for the table `qc_pathway_patient_blocks` */



/*Table structure for table `qc_patient_pathway_info` */

DROP TABLE IF EXISTS `qc_patient_pathway_info`;

CREATE TABLE `qc_patient_pathway_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `patient_id` INT(11) DEFAULT NULL,
  `pathway_id` INT(11) DEFAULT NULL,
  `status` VARCHAR(45) DEFAULT 'N',
  `accepteddate` DATE DEFAULT NULL,
  `accept` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

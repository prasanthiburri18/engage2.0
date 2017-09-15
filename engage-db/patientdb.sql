/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.27-log : Database - patientdb
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`patientdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `patientdb`;

/*Table structure for table `qc_pathway` */

DROP TABLE IF EXISTS `qc_pathway`;

CREATE TABLE `qc_pathway` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clinician_id` bigint(20) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `pathway_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `qc_pathway` */

/*Table structure for table `qc_pathway_event` */

DROP TABLE IF EXISTS `qc_pathway_event`;

CREATE TABLE `qc_pathway_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` date DEFAULT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `event_pos_col` bigint(20) DEFAULT NULL,
  `event_pos_row` bigint(20) DEFAULT NULL,
  `pathway_id` bigint(20) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `qc_pathway_event` */

/*Table structure for table `qc_pathway_patient_message` */

DROP TABLE IF EXISTS `qc_pathway_patient_message`;

CREATE TABLE `qc_pathway_patient_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `block_id` bigint(20) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `frequency` bigint(20) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `message_type` varchar(255) DEFAULT NULL,
  `patient_id` bigint(20) DEFAULT NULL,
  `phisecured` varchar(255) DEFAULT NULL,
  `resource_link` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  `trigger_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `qc_pathway_patient_message` */

/*Table structure for table `qc_patient` */

DROP TABLE IF EXISTS `qc_patient`;

CREATE TABLE `qc_patient` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_token` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created_by` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=latin1;



/*Table structure for table `qc_patient_pathway` */

DROP TABLE IF EXISTS `qc_patient_pathway`;

CREATE TABLE `qc_patient_pathway` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) DEFAULT NULL,
  `pathway_id` bigint(20) DEFAULT NULL,
  `patient_id` bigint(20) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=831 DEFAULT CHARSET=latin1;

/*Data for the table `qc_patient_pathway` */


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

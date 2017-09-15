/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.27-log : Database - userdb
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`userdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `userdb`;

/*Table structure for table `dt_tokens` */

DROP TABLE IF EXISTS `dt_tokens`;

CREATE TABLE `dt_tokens` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `acess_key` varchar(200) DEFAULT NULL,
  `secret_key` varchar(200) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `dt_tokens` */

/*Table structure for table `dt_user_details` */

DROP TABLE IF EXISTS `dt_user_details`;

CREATE TABLE `dt_user_details` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `patient_id` bigint(20) DEFAULT NULL,
  `speciality` varchar(200) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `age` varchar(3) DEFAULT NULL,
  `blood_group` varchar(3) DEFAULT NULL,
  `gender` enum('M','F') DEFAULT NULL,
  `height` varchar(6) DEFAULT NULL,
  `weight` varchar(6) DEFAULT NULL,
  `address` text,
  `state_id` int(11) DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `zip_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `dt_user_details` */

/*Table structure for table `dt_user_roles` */

DROP TABLE IF EXISTS `dt_user_roles`;

CREATE TABLE `dt_user_roles` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `createdat` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS `dt_users`;

CREATE TABLE `dt_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` enum('Y','N') DEFAULT 'N',
  `full_name` varchar(255) DEFAULT NULL,
  `pratice_name` varchar(255) DEFAULT NULL,
  `orgid` int(11) NOT NULL,
  `user_type` enum('A','U') DEFAULT 'A',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=220 DEFAULT CHARSET=latin1;

/*Table structure for table `qc_organization` */

DROP TABLE IF EXISTS `qc_organization`;

CREATE TABLE `qc_organization` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=latin1;


/*Table structure for table `qc_user_roles` */

DROP TABLE IF EXISTS `qc_user_roles`;

CREATE TABLE `qc_user_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdat` datetime DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `user_id` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `qc_user_roles` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

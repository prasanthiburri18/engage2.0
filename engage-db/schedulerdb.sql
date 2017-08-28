/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.27-log : Database - schedulerdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`schedulerdb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `schedulerdb`;

/*Table structure for table `qc_scheduled_queue` */

DROP TABLE IF EXISTS `qc_scheduled_queue`;

CREATE TABLE `qc_scheduled_queue` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `scheduled_message_id` varchar(50) DEFAULT NULL,
  `app_id` int(10) DEFAULT NULL,
  `to_be_sent_at` timestamp NULL DEFAULT NULL,
  `status` enum('SENT','FAILED','SCHEDULED') DEFAULT NULL,
  `extra` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `qc_scheduled_queue` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

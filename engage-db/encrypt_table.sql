
DROP TABLE IF EXISTS `patientdb`.`qc_encrypt_key`;
CREATE TABLE `patientdb`.`qc_encrypt_key` (
  `id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `is_active` char NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY  (`id`, `key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into `patientdb`.qc_encrypt_key(id, `key`,`is_active`) values(1,"Secure@helthapi1", 'Y');
commit;


DROP TABLE IF EXISTS `schedulerdb`.`qc_encrypt_key`;
CREATE TABLE `schedulerdb`.`qc_encrypt_key` (
  `id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `is_active` char NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY  (`id`, `key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into `schedulerdb`.qc_encrypt_key(id, `key`,`is_active`) values(1,"Secure@helthapi1", 'Y');
commit;

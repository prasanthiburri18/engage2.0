
DROP TABLE IF EXISTS `qc_encrypt_key`;
CREATE TABLE `qc_encrypt_key` (
  `id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `is_active` char NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY  (`id`, `key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into qc_encrypt_key(id, `key`,`is_active`) values(1,"Secure@helthapi1", 'Y');
commit;

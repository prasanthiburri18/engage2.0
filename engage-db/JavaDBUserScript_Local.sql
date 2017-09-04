create user 'java_client'@'localhost' identified by 'P@ssw0rd';

grant insert, delete, update, select on *.*to 'java_client'@'localhost';

flush privileges;
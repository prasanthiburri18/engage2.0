create user 'java_client'@'localhost' identified by 'P@ssw0rd';

grant insert,create, alter, delete, update, select on *.*to 'java_client'@'localhost';

flush privileges;

create user 'java_client'@'%' identified by 'P@ssw0rd';

grant insert,create, alter, references, delete, update, select on *.*to 'java_client'@'%';

flush privileges;

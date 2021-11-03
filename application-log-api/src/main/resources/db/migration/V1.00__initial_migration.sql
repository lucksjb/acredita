create table log (id bigint not null auto_increment, 
active varchar(3), 
creationDateTime datetime(6),
 lastUpdate datetime(6), 
 uuid varchar(36), 
 dateandtime DATETIME not null,
 message varchar(255) not null, 
 primary key (id)) 
 engine=InnoDB;





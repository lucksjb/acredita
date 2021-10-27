create table perfil (id bigint not null auto_increment, active varchar(1), creationDateTime datetime(6), lastUpdate datetime(6), uuid varchar(36), descricao varchar(50), primary key (id)) engine=InnoDB
create table perfilPrograma (perfil_id bigint not null, programa_id varchar(250) not null) engine=InnoDB
create table programa (id varchar(250) not null, active varchar(1), creationDateTime datetime(6), descricao varchar(100), endPoint varchar(150), icone varchar(50), lastUpdate datetime(6), route varchar(100), seqMenu bigint, tipoItemMenu varchar(1), uuid varchar(36), programaPai_id varchar(250), primary key (id)) engine=InnoDB
create table usuario (id bigint not null auto_increment, active varchar(1), creationDateTime datetime(6), lastUpdate datetime(6), uuid varchar(36), alterarSenhaProxLogin varchar(1), dataHoraSolicitouTrocaSenha datetime(6), email varchar(100) not null, fone varchar(14), login varchar(40) not null, nascimento date not null, nome varchar(100), senha varchar(60), uuidTrocaSenha varchar(100), validadeDaSenha bigint, primary key (id)) engine=InnoDB
create table usuarioPerfil (usuario_id bigint not null, perfil_id bigint not null) engine=InnoDB
alter table perfilPrograma add constraint FK898vtplak9nrk7aywq2l50qq9 foreign key (programa_id) references programa (id)
alter table perfilPrograma add constraint FK10fpneudve6o9tq3loil1j6si foreign key (perfil_id) references perfil (id)
alter table programa add constraint FKmjc9ojo2gnwrwqb0qu1ivdlfl foreign key (programaPai_id) references programa (id)
alter table usuarioPerfil add constraint FKkh26us44x7mvbk2ri2gmjdywp foreign key (perfil_id) references perfil (id)
alter table usuarioPerfil add constraint FKlx47aqbc23fbbbnxgs8a6k2h4 foreign key (usuario_id) references usuario (id)
create table perfil (id bigint not null auto_increment, active varchar(1), creationDateTime datetime(6), lastUpdate datetime(6), uuid varchar(36), descricao varchar(50), primary key (id)) engine=InnoDB
create table perfilPrograma (perfil_id bigint not null, programa_id varchar(250) not null) engine=InnoDB
create table programa (id varchar(250) not null, active varchar(1), creationDateTime datetime(6), descricao varchar(100), endPoint varchar(150), icone varchar(50), lastUpdate datetime(6), route varchar(100), seqMenu bigint, tipoItemMenu varchar(1), uuid varchar(36), programaPai_id varchar(250), primary key (id)) engine=InnoDB
create table usuario (id bigint not null auto_increment, active varchar(1), creationDateTime datetime(6), lastUpdate datetime(6), uuid varchar(36), alterarSenhaProxLogin varchar(1), dataHoraSolicitouTrocaSenha datetime(6), email varchar(100) not null, fone varchar(14), login varchar(40) not null, nascimento date not null, nome varchar(100), senha varchar(60), uuidTrocaSenha varchar(100), validadeDaSenha bigint, primary key (id)) engine=InnoDB
create table usuarioPerfil (usuario_id bigint not null, perfil_id bigint not null) engine=InnoDB
alter table perfilPrograma add constraint FK898vtplak9nrk7aywq2l50qq9 foreign key (programa_id) references programa (id)
alter table perfilPrograma add constraint FK10fpneudve6o9tq3loil1j6si foreign key (perfil_id) references perfil (id)
alter table programa add constraint FKmjc9ojo2gnwrwqb0qu1ivdlfl foreign key (programaPai_id) references programa (id)
alter table usuarioPerfil add constraint FKkh26us44x7mvbk2ri2gmjdywp foreign key (perfil_id) references perfil (id)
alter table usuarioPerfil add constraint FKlx47aqbc23fbbbnxgs8a6k2h4 foreign key (usuario_id) references usuario (id)

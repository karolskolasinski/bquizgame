create table account
(
  id                     bigint auto_increment
    primary key,
  email                  varchar(255) null,
  password               varchar(255) null,
  registration_date_time datetime(6)  null,
  username               varchar(255) null
);

create table account_role
(
  id   bigint auto_increment
    primary key,
  name varchar(255) null
);

create table account_account_roles
(
  account_id       bigint not null,
  account_roles_id bigint not null,
  primary key (account_id, account_roles_id),
  constraint FKh1rrs2tgcs6daq3w18j6tj019
    foreign key (account_id) references account (id),
  constraint FKp96ige4jpqm01p1lvoybmcr73
    foreign key (account_roles_id) references account_role (id)
);

create table quiz
(
  id bigint auto_increment
    primary key
);

create table question
(
  id         bigint auto_increment
    primary key,
  category   varchar(255) null,
  content    varchar(255) null,
  difficulty int          not null,
  quiz_id    bigint       null,
  constraint FKb0yh0c1qaxfwlcnwo9dms2txf
    foreign key (quiz_id) references quiz (id)
);

create table answer
(
  id             bigint auto_increment
    primary key,
  answer_content varchar(255) null,
  correct        bit          not null,
  reference      varchar(255) null,
  question_id    bigint       null,
  constraint FK8frr4bcabmmeyyu60qt7iiblo
    foreign key (question_id) references question (id)
);

create table user_quiz
(
  id                   bigint auto_increment
    primary key,
  quiz_start_date_time datetime(6) null,
  account_id           bigint      null,
  quiz_id              bigint      null,
  constraint FKamnut7xcu11shpwa926626y87
    foreign key (quiz_id) references quiz (id),
  constraint FKqq8lg8s2idpctnnxfxffdxglt
    foreign key (account_id) references account (id)
);

create table user_answer
(
  id           bigint auto_increment
    primary key,
  answer_id    bigint null,
  question_id  bigint null,
  user_quiz_id bigint null,
  constraint FK2saap9xe6vqrjo2tdclq120ii
    foreign key (user_quiz_id) references user_quiz (id),
  constraint FKm321eamc0drwpxfkvyl9giypt
    foreign key (answer_id) references answer (id),
  constraint FKpsk90eok3ounaet92hku3gny1
    foreign key (question_id) references question (id)
);



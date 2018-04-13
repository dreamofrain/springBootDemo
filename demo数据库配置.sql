--����DEMO�ռ�
CREATE TABLESPACE DEMO
DATAFILE 'D:\OracleApp\DEMO_0001.ORA' SIZE 50M AUTOEXTEND ON NEXT 50M MAXSIZE 1G EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;

--�����û�demo
create user demo
identified by demo
default tablespace demo
temporary tablespace TEMP; 
grant connect to demo;
grant dba to demo;
grant resource to demo; 
grant unlimited tablespace to demo;

--����ҵ���
create table DEMO.DEMO
(
  id     NUMBER(10) not null,
  card   VARCHAR2(255 CHAR),
  name   VARCHAR2(255 CHAR),
  sex    VARCHAR2(255 CHAR),
  status VARCHAR2(255 CHAR),
  type   VARCHAR2(255 CHAR)
)
tablespace DEMO
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DEMO.DEMO
  add primary key (ID)
  using index 
  tablespace DEMO
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


create table DEMO.MASSAGE
(
  id   NUMBER(10) not null,
  name VARCHAR2(255 CHAR),
  path VARCHAR2(255 CHAR)
)
tablespace DEMO
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DEMO.MASSAGE
  add primary key (ID)
  using index 
  tablespace DEMO
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


create table DEMO.PERMISSIONS
(
  id       NUMBER(10) not null,
  per_code VARCHAR2(255),
  per_name VARCHAR2(255)
)
tablespace DEMO
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DEMO.PERMISSIONS
  add primary key (ID)
  using index 
  tablespace DEMO
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


create table DEMO.PERMISSIONS_SETTINGS
(
  id      NUMBER(10) not null,
  perid   NUMBER(10) not null,
  user_id NUMBER(10) not null
)
tablespace DEMO
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DEMO.PERMISSIONS_SETTINGS
  add primary key (ID)
  using index 
  tablespace DEMO
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


create table DEMO.USERS
(
  id        NUMBER(10) not null,
  id_card   VARCHAR2(255 CHAR),
  login_id  VARCHAR2(255 CHAR),
  pass_word VARCHAR2(255 CHAR),
  user_name VARCHAR2(255 CHAR),
  status    VARCHAR2(255 CHAR)
)
tablespace DEMO
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DEMO.USERS
  add primary key (ID)
  using index 
  tablespace DEMO
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

--Ȩ������
insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (5, 'DATA_IMPORT', '���ݵ���');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (4, 'DATA_EXPORT', '���ݵ���');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (2, 'DATA_UPDATE', '�����޸�');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (3, 'DATA_REMOVE', '����ɾ��');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (1, 'DATA_ADD', '�������');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (6, 'MASSAGE_GENERATE', '��������');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (10, 'MASSAGE_SHOW', '���Ĳ鿴');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (8, 'MASSAGE_DOWNLOAD', '��������');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (9, 'MASSAGE_RETURN', '���Ĵ��');

insert into PERMISSIONS (ID, PER_CODE, PER_NAME)
values (7, 'DATA_SHOW', '���ݲ鿴');

--����Ա�˻�
insert into USERS (ID, ID_CARD, LOGIN_ID, PASS_WORD, USER_NAME, STATUS)
values (1, '666', 'admin', '123456', '����Ա', '����');

commit;

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS PAIS (
  ID_PAIS CHAR(2) NOT NULL,
  PRIMARY KEY (ID_PAIS));

CREATE TABLE IF NOT EXISTS ESTADO (
  ID_ESTADO CHAR(2) NOT NULL,
  ID_PAIS CHAR(2) NOT NULL,
  NOME VARCHAR(120) NOT NULL,
  IBGE VARCHAR(10) NOT NULL,
  PRIMARY KEY (ID_ESTADO, ID_PAIS),
  CONSTRAINT ESTADO_ID_PAIS
    FOREIGN KEY (ID_PAIS)
    REFERENCES PAIS (ID_PAIS)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX ESTADO_ID_PAIS_IDX ON ESTADO (ID_PAIS);

CREATE TABLE IF NOT EXISTS CIDADE (
  ID_CIDADE BIGINT NOT NULL,
  ID_ESTADO CHAR(2) NOT NULL,
  ID_PAIS CHAR(2) NOT NULL,
  NOME VARCHAR(120) NOT NULL,
  IBGE VARCHAR(10) NOT NULL,
  AREA FLOAT NOT NULL,
  PRIMARY KEY (ID_CIDADE),
  UNIQUE (ID_ESTADO, ID_PAIS, NOME),
  CONSTRAINT CIDADE_ID_ESTADO_ID_PAIS_FK
    FOREIGN KEY (ID_ESTADO , ID_PAIS)
    REFERENCES ESTADO (ID_ESTADO, ID_PAIS)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX CIDADE_ID_ESTADO_ID_PAIS_FK_IDX ON CIDADE (ID_ESTADO, ID_PAIS);

CREATE TABLE IF NOT EXISTS ENDERECO (
  ID_ENDERECO BIGSERIAL NOT NULL,
  ID_CIDADE BIGINT NOT NULL,
  BAIRRO VARCHAR(120) NOT NULL,
  LOGRADOURO VARCHAR(120) NOT NULL,
  COMPLEMENTO VARCHAR(45) NULL,
  NUMERO VARCHAR(20) NOT NULL,
  CEP CHAR(8) NOT NULL,
  PRIMARY KEY (ID_ENDERECO),
  CONSTRAINT ENDERECO_ID_CIDADE_FK
    FOREIGN KEY (ID_CIDADE)
    REFERENCES public.CIDADE (ID_CIDADE)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX ENDERECO_ID_CIDADE_FK_IDX ON ENDERECO (ID_CIDADE);

CREATE TABLE IF NOT EXISTS PESSOA (
  ID_PESSOA BIGSERIAL NOT NULL,
  CNPJ CHAR(14) NULL,
  CPF CHAR(11) NULL,
  ID_ESTRANGEIRO VARCHAR(20) NULL,
  TELEFONE_1 VARCHAR(15) NOT NULL,
  TELEFONE_2 VARCHAR(15) NULL,
  ID_ENDERECO BIGINT NOT NULL,
  EMAIL VARCHAR(320) NOT NULL,
  PRIMARY KEY (ID_PESSOA),
  CONSTRAINT PESSOA_ID_ENDERECO_FK
    FOREIGN KEY (ID_ENDERECO)
    REFERENCES ENDERECO (ID_ENDERECO)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX PESSOA_ID_ENDERECO_FK_IDX ON PESSOA (ID_ENDERECO);

CREATE TABLE IF NOT EXISTS USUARIO (
  ID_USUARIO BIGSERIAL NOT NULL,
  ID_PESSOA BIGINT NULL,
  APELIDO VARCHAR(45) NOT NULL,
  SENHA VARCHAR(128) NOT NULL,
  STATUS VARCHAR(45) NOT NULL,
  PRIMARY KEY (ID_USUARIO),
  UNIQUE (APELIDO),
  CONSTRAINT USUARIO_ID_PESSOA_FK
    FOREIGN KEY (ID_PESSOA)
    REFERENCES PESSOA (ID_PESSOA)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX USUARIO_ID_PESSOA_FK_IDX ON USUARIO (ID_PESSOA);

CREATE TABLE IF NOT EXISTS ESTABELECIMENTO (
  ID_PESSOA BIGSERIAL NOT NULL,
  ID_PESSOA_PAI BIGINT NULL,
  RAZAO_SOCIAL VARCHAR(120) NULL,
  NOME_FANTASIA VARCHAR(120) NOT NULL,
  DATA TIMESTAMP NULL,
  PRIMARY KEY (ID_PESSOA),
  CONSTRAINT ESTABELECIMENTO_ID_PESSOA_FK
    FOREIGN KEY (ID_PESSOA)
    REFERENCES PESSOA (ID_PESSOA)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT ESTABELECIMENTO_ID_PESSOA_PAI_FK
    FOREIGN KEY (ID_PESSOA_PAI)
    REFERENCES PESSOA (ID_PESSOA)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX ESTABELECIMENTO_ID_PESSOA_FK_IDX ON ESTABELECIMENTO (ID_PESSOA);
CREATE INDEX ESTABELECIMENTO_ID_PESSOA_PAI_FK_IDX ON ESTABELECIMENTO (ID_PESSOA_PAI);

CREATE TABLE IF NOT EXISTS USUARIO_USUARIO_PERFIL (
  ID_USUARIO BIGINT NOT NULL,
  PERFIL VARCHAR(45) NOT NULL,
  PRIMARY KEY (ID_USUARIO, PERFIL),
  CONSTRAINT USUARIO_USUARIO_PERFIL_ID_USUARIO_FK
    FOREIGN KEY (ID_USUARIO)
    REFERENCES USUARIO (ID_USUARIO)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS META_BAIRRO (
  ID_BAIRRO BIGINT NOT NULL,
  ID_CIDADE BIGINT NOT NULL,
  NOME VARCHAR(50) NOT NULL,
  PRIMARY KEY (ID_BAIRRO));

CREATE TABLE IF NOT EXISTS META_LOGRADOURO (
  CEP VARCHAR(10) NOT NULL,
  ID_BAIRRO BIGINT NOT NULL,
  LOGRADOURO VARCHAR(200) NOT NULL,
  TIPO_LOGRADOURO VARCHAR(80) NULL,
  COMPLEMENTO VARCHAR(100) NULL,
  LOCAL VARCHAR(120) NULL,
  PRIMARY KEY (CEP));
  
CREATE TABLE IF NOT EXISTS ESTABELECIMENTO_LICENCA (
  ID_PESSOA BIGINT NOT NULL,
  LICENCA VARCHAR(255) NOT NULL,
  PRIMARY KEY (ID_PESSOA, LICENCA),
  CONSTRAINT ESTABELECIMENTO_LICENCA_ID_PESSOA_FK
    FOREIGN KEY (ID_PESSOA)
    REFERENCES ESTABELECIMENTO (ID_PESSOA)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE INDEX ESTABELECIMENTO_LICENCA_ID_PESSOA_FK_IDX ON ESTABELECIMENTO (ID_PESSOA);

CREATE TABLE IF NOT EXISTS TAXA (
  ID_TAXA BIGSERIAL NOT NULL,
  ID_PESSOA BIGINT NOT NULL,
  VALOR_ESPECIE DECIMAL(10, 5) NULL,
  VALOR_CARTAO DECIMAL(10, 5) NULL,
  MOEDA CHAR(3) NOT NULL,
  DATA TIMESTAMP NOT NULL,
  STATUS VARCHAR(45) NOT NULL,
  FINALIDADE VARCHAR(45) NOT NULL,
  PRIMARY KEY (ID_TAXA),
  CONSTRAINT ESTABELECIMENTO_ID_PESSOA_FK
    FOREIGN KEY (ID_PESSOA)
    REFERENCES ESTABELECIMENTO (ID_PESSOA)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE INDEX TAXA_ID_PESSOA_FK_IDX ON ESTABELECIMENTO (ID_PESSOA);

CREATE TABLE IF NOT EXISTS IOF (
  ID_IOF BIGSERIAL NOT NULL,
  VALOR_MOEDA DECIMAL(3, 2) NULL,
  VALOR_CARTAO DECIMAL(3, 2) NULL,
  DATA TIMESTAMP NOT NULL,
  STATUS VARCHAR(45) NOT NULL,
  PRIMARY KEY (ID_IOF)
);

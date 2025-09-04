-- Create table
create table VOUCHER
 (
  ID              BIGINT(12)     NOT NULL COMMENT '表序号',
  TRAN_NO         VARCHAR(12)    NULL COMMENT '传票凭证号，格式: ^[A-Za-z]{4}\\d{8}$',
  TRAN_TYPE       CHAR(2)        NOT NULL COMMENT '科目: 01-转账汇款, 02-结息, 03-手续费, 04-保证金',
  PAYER_ACC_NO    VARCHAR(32)       NOT NULL COMMENT '付款账号，32位数字',
  PAYER_ACC_NAME  VARCHAR(100)   NOT NULL COMMENT '付款账户名',
  PAYEE_ACC_NO    VARCHAR(32)       NOT NULL COMMENT '收款账号，32位数字',
  PAYEE_ACC_NAME  VARCHAR(100)   NOT NULL COMMENT '收款账户名',
  AMT             DECIMAL(14,2)  NOT NULL COMMENT '交易金额，两位小数',
  REMARKS         VARCHAR(200)   NULL COMMENT '摘要',
  TRAN_DATE       VARCHAR(8)        NOT NULL COMMENT '交易日期，格式yyyyMMdd',
  TELLER_NO       VARCHAR(7)     NULL COMMENT '柜员号，格式EHR\\d{7}',
  TRAN_TIME       VARCHAR(19)      NOT NULL COMMENT '经办时间，格式yyyy-MM-dd HH:mm:ss',
  PRIMARY KEY (ID)
);

-- 给 voucher 表的 tran_no 字段加唯一约束
ALTER TABLE voucher ADD CONSTRAINT uq_tran_no UNIQUE (tran_no);

-- Create sequence
create sequence SEQ_MSA
minvalue 1
maxvalue 10000
start with 1
increment by 1
cache 20;

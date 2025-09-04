package com.boc.voucherjavademo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Voucher {
    @Id
    @SequenceGenerator(name="MSA_SERVICE_ID_GENERATOR", sequenceName="SEQ_MSA",allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MSA_SERVICE_ID_GENERATOR")
    private long id;

    private String tranNo;
    private TranType tranType;
    private String payerAccNo;
    private String payerAccName;
    private String payeeAccNo;
    private String payeeAccName;
    private Double amt;
    private String remarks;
    private String tranDate;

    private String tellerNo;
    private String tranTime;


}

package com.boc.voucherjavademo.domain;

public enum TranType{
    TRANSFER("01","转账汇款"),
    INTEREST("02","结息"),
    FEE("03","手续费"),
    MARGIN("04","保证金");

    private String code;
    private String desc;
    TranType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    public static TranType findByCode(String code) {
        for (TranType tranType : TranType.values()) {
            if (tranType.getCode().equals(code)) {
                return tranType;
            }

        }
        throw new IllegalArgumentException("非法参数类型");
    }
}

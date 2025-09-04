package com.boc.voucherjavademo.dto;

import com.boc.voucherjavademo.domain.TranType;
import com.boc.voucherjavademo.tools.TranTypeConverter;
import com.train.base.validation.constraints.ExtLength;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 根据字段模糊查询dto
 */
@Data
@Schema(description = "传票查询对象")
public class VoucherQueryDto {
    @Schema(description = "交易类型")
    @Convert(converter = TranTypeConverter.class)
    private TranType tranType;

    @Schema(description = "付款账号",maxLength = 32)
    @ExtLength(max = 32)
    private String payerAccNo;

    @Schema(description = "付款账户名")
    private String payerAccName;

    @Schema(description = "收款账号",maxLength = 32)
    @ExtLength(max = 32)
    private String payeeAccNo;

    @Schema(description = "收款账户名")
    private String payeeAccName;

    @Schema(description = "最低交易金额")
    private Double minAmount;

    @Schema(description = "最高交易金额")
    private Double maxAmount;


    @Schema(description = "起始交易日期",required = true, format = "yyMMdd")
    @Pattern(regexp = "^\\d{8}$", message = "交易日期格式必须为yyyyMMdd")
    private String startTranDate;

    @Schema(description = "结束交易日期",required = true, format = "yyMMdd")
    @Pattern(regexp = "^\\d{8}$", message = "交易日期格式必须为yyyyMMdd")
    private String endTranDate;

//    @AssertTrue(message = "起始交易日期和结束交易日期至少填一个")
//    public boolean isValidDateRange() {
//        return (startTranDate != null && !startTranDate.isEmpty()) ||
//                (endTranDate != null && !endTranDate.isEmpty());
//    }

}

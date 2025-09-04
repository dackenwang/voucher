package com.boc.voucherjavademo.dto;

import com.boc.voucherjavademo.domain.TranType;
import com.boc.voucherjavademo.tools.TranTypeConverter;
import com.boc.voucherjavademo.validation.UniqueTranNo;
import com.train.base.validation.constraints.ExtLength;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Convert;
import javax.validation.constraints.*;

@Data
@Schema(description = "传票定义对象")
public class VoucherParserDto {
    @Schema(description = "传票凭证号",example = "BOCS11119999",required = true,maxLength = 12)
    @NotBlank
    @UniqueTranNo
    @Pattern(regexp = "^[A-Za-z]{4}\\d{8}$", message = "凭证号格式错误，应为4位字母+8位数字")
    private String tranNo;

    @Schema(description = "科目",example = "TRANSFER",required = true,maxLength = 2)
    @Convert(converter = TranTypeConverter.class)
    @NotNull
    private TranType tranType;

    @Schema(description = "付款账号",example = "99998888777766665555444433332222",required = true,maxLength = 32)
    @NotBlank
    @Pattern(regexp = "^\\d{32}$", message = "付款账号必须为32位数字")
    private String payerAccNo;

    @Schema(description = "付款账户名",example = "张三",required = true,maxLength = 100)
    @ExtLength(max = 100)
    @NotBlank
    private String payerAccName;

    @Schema(description = "收款账号",example = "99998888777766665555444433332222",required = true,maxLength = 32)
    @NotBlank
    @Pattern(regexp = "^\\d{32}$", message = "收款账号必须为32位数字")
    private String payeeAccNo;

    @Schema(description = "收款账户名",example = "李斯",required = true,maxLength = 100)
    @ExtLength(max = 100)
    @NotBlank
    private String payeeAccName;

    @Schema(description = "交易金额",example = "9999.98",required = true)
    @NotNull
    @Digits(integer = 14, fraction = 2, message = "金额整数部分最长14位，小数2位")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private Double amt;

    @Schema(description = "交易日期",example = "20250825",format = "yyMMdd",required = true,maxLength = 8)
    @ExtLength(max = 8)
    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "交易日期格式必须为yyyyMMdd")
    private String tranDate;

    @Schema(description = "摘要",example = "交易附言",required = false,maxLength = 200)
    @ExtLength(max = 200)
    private String remarks;

    @Schema(description = "交易柜员号", example = "2352651",required = true,maxLength = 7)
    @ExtLength(max = 7)
    @NotBlank
    @Pattern(regexp = "^\\d{7}$", message = "柜员号必须为7位数字")
    private String tellerNo;


}

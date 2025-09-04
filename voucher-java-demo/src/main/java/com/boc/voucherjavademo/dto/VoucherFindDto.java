package com.boc.voucherjavademo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 返回结果对象
 */
@Data
@Schema(description = "传票查询结果返回对象")
public class VoucherFindDto extends VoucherSaveDto{

    @Schema(description = "id")
    private long id;
    @Schema(description = "状态")
    private boolean status;
    @Schema(description = "经办时间")
    private String tranTime;
    @Schema(description = "交易柜员号")
    private String tellerNo;



}

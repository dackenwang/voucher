package com.boc.voucherjavademo.tools;

import com.boc.voucherjavademo.domain.TranType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TranTypeConverter implements AttributeConverter<TranType,String> {
    @Override
    public String convertToDatabaseColumn(TranType tranType) {
        return tranType==null?null:tranType.getCode();
    }
    @Override
    public TranType convertToEntityAttribute(String code) {
        return code==null?null:TranType.findByCode(code);
    }
}

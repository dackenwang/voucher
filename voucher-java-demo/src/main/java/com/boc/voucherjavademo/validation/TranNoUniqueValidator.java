package com.boc.voucherjavademo.validation;

import com.boc.voucherjavademo.domain.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class TranNoUniqueValidator implements ConstraintValidator<UniqueTranNo,String> {

    @Autowired
    VoucherRepository voucherRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //空字符串交给@NotBlank处理
        if(s==null || s.isEmpty()){
            return true;
        }
        return !voucherRepository.existsByTranNo(s);
    }
}

package com.boc.voucherjavademo.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;


import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class SpringValidatorUtil {

    @Autowired
    private Validator validator; // Spring 会注入 LocalValidatorFactoryBean

    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> v : violations) {
                sb.append(v.getPropertyPath())
                  .append("(").append(v.getInvalidValue()).append(")")
                  .append(v.getMessage()).append(",");
            }
            throw new IllegalArgumentException("数据校验错误: " + sb.substring(0, sb.length() - 1));
        }
    }
}

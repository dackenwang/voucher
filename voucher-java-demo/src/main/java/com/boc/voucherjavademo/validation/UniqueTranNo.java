package com.boc.voucherjavademo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TranNoUniqueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTranNo {
    String message() default "传票凭证号不可重复";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload()  default {};
}

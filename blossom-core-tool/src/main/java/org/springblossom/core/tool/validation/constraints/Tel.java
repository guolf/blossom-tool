package org.springblossom.core.tool.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 手机号码校验
 *
 * @author guolf
 */
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@ReportAsSingleViolation
public @interface Tel {

	String message() default "手机号码不合法";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String regexp() default  "^(13\\d|14[5-9]|15[0-35-9]|166|17[0-8]|18\\d|19[8-9])\\d{8}$";
}

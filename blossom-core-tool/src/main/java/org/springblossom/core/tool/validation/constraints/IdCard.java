package org.springblossom.core.tool.validation.constraints;

import org.springblossom.core.tool.validation.IdCardValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 身份证号码校验
 */
@Documented
@Constraint(validatedBy = {IdCardValidation.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@ReportAsSingleViolation
public @interface IdCard {

	String message() default "身份证号码不合法";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

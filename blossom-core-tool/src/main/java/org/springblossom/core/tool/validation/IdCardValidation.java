package org.springblossom.core.tool.validation;

import org.springblossom.core.tool.utils.Func;
import org.springblossom.core.tool.utils.ValidateIDCard;
import org.springblossom.core.tool.validation.constraints.IdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdCardValidation implements ConstraintValidator<IdCard,String> {

	/**
	 *
	 * @param value
	 * @param constraintValidatorContext
	 * @return true校验通过，false校验不通过
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if(Func.isBlank(value)) {
			return true;
		}
		return ValidateIDCard.validateCard(value);
	}
}

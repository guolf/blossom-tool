package org.springblossom.core.secure.twofactor.defaults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springblossom.core.secure.twofactor.TwoFactorToken;
import org.springblossom.core.secure.twofactor.TwoFactorValidator;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author guolf
 */
@AllArgsConstructor
public class DefaultTwoFactorValidator implements TwoFactorValidator {

    @Getter
    private String provider;

    private Function<String, Boolean> validator;

    private Supplier<TwoFactorToken> tokenSupplier;

    @Override
    public boolean verify(String code, long timeout) {
        boolean success = validator.apply(code);
        if (success) {
            tokenSupplier.get().generate(timeout);
        }
        return success;
    }

    @Override
    public boolean expired() {
        return tokenSupplier.get().expired();
    }
}

package org.springblossom.core.secure.twofactor;

import java.io.Serializable;

public interface TwoFactorToken extends Serializable {
	void generate(long timeout);

	boolean expired();
}

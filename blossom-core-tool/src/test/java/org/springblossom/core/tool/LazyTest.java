package org.springblossom.core.tool;

import org.springblossom.core.tool.utils.Lazy;
import org.springframework.util.function.SupplierUtils;

import java.util.function.Supplier;

public class LazyTest {

	public static void main(String[] args) {
		Supplier<String> supplier = String::new;
		Lazy<String> lazy = Lazy.of(supplier);
		String result = lazy.get();
		System.out.println("result = " + result);
	}
}

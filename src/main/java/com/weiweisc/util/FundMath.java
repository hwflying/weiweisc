package com.weiweisc.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class FundMath {
	public static double doubleAdd(double a, double b) {
		BigDecimal decimal = new BigDecimal(String.valueOf(a));
		decimal = decimal.add(new BigDecimal(String.valueOf(b)));
		return decimal.doubleValue();
	}

	public static double doubleSubtract(double a, double b) {
		BigDecimal decimal = new BigDecimal(String.valueOf(a));
		decimal = decimal.subtract(new BigDecimal(String.valueOf(b)));
		return decimal.doubleValue();
	}

	public static double doubleMultiply(double a, double b) {
		BigDecimal decimal = new BigDecimal(String.valueOf(a));
		decimal = decimal.multiply(new BigDecimal(String.valueOf(b)),
				new MathContext(3, RoundingMode.HALF_UP));
		return decimal.doubleValue();
	}

	public static double doubleDivide(double a, double b) {
		BigDecimal decimal = new BigDecimal(String.valueOf(a));
		decimal = decimal.divide(new BigDecimal(String.valueOf(b)),
				new MathContext(3, RoundingMode.HALF_UP));
		return decimal.doubleValue();
	}

}

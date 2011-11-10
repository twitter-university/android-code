package com.marakana.android.currencyconverter;

public interface CurrencyConverter {
	public double getConversionRate(String fromCurrencyCode,
			String toCurrencyCode) throws CurrencyConverterException;
}

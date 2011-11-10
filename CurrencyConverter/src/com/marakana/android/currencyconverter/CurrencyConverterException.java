package com.marakana.android.currencyconverter;

public class CurrencyConverterException extends RuntimeException {

	private static final long serialVersionUID = 7606608827802815021L;

	public CurrencyConverterException(String detailMessage) {
		super(detailMessage);
	}

	public CurrencyConverterException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}

package com.marakana.android.currencyconverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GoogleCurrencyConverter implements CurrencyConverter {

	public static final String DEFAULT_URL_FORMAT = "http://www.google.com/finance/converter?a=1&from=%s&to=%s";
	public static final Pattern DEFAULT_RESULT_PATTERN = Pattern
			.compile("<div id=currency_converter_result>1 [A-Z]{3} = <span class=bld>([0-9\\.]+) [A-Z]{3}</span>");

	private final String urlFormat;

	private final Pattern resultPattern;
	private final DefaultHttpClient httpclient;

	public GoogleCurrencyConverter() {
		this(DEFAULT_URL_FORMAT, DEFAULT_RESULT_PATTERN);
	}

	public GoogleCurrencyConverter(String urlFormat, Pattern resultPattern) {
		this.urlFormat = urlFormat;
		this.resultPattern = resultPattern;
		this.httpclient = new DefaultHttpClient();
	}

	public synchronized double getConversionRate(String fromCurrencyCode,
			String toCurrencyCode) throws CurrencyConverterException {
		try {
			String url = String.format(this.urlFormat, fromCurrencyCode,
					toCurrencyCode);
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = this.httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(),
								EntityUtils.getContentCharSet(entity)));
				String line = null;
				Matcher matcher = null;
				while ((line = reader.readLine()) != null) {
					matcher = matcher == null ? this.resultPattern
							.matcher(line) : matcher.reset(line);
					if (matcher.matches()) {
						httpget.abort();
						return Double.parseDouble(matcher.group(1));
					}
				}
				httpget.abort();
				throw new CurrencyConverterException("Could not find ["
						+ resultPattern.toString() + "] in response to [" + url
						+ "]");
			} else {
				if (entity != null) {
					entity.consumeContent();
				}
				throw new CurrencyConverterException("Got [" + statusCode
						+ "] in response to [" + url + "]");
			}
		} catch (Exception e) {
			throw new CurrencyConverterException(
					"Failed to get conversion rate from " + fromCurrencyCode
							+ " to " + toCurrencyCode, e);
		}
	}

	public void close() {
		this.httpclient.getConnectionManager().shutdown();
	}
}

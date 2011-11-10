package com.marakana.android.currencyconverter.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.marakana.android.currencyconverter.CurrencyConverter;
import com.marakana.android.currencyconverter.CurrencyConverterActivity;
import com.marakana.android.currencyconverter.GoogleCurrencyConverter;
import com.marakana.android.currencyconverter.R;

public class CurrencyConverterActivityTest extends
		ActivityInstrumentationTestCase2<CurrencyConverterActivity> {

	private CurrencyConverterActivity activity;
	private Spinner fromCurrency;
	private Spinner toCurrency;
	private EditText inputAmount;
	private TextView outputAmount;
	private View convert;
	private View reverseCurrencies;
	private View clearInput;
	private View copyResult;
	private CurrencyConverter currencyConverter;
	private String[] currencies;

	public CurrencyConverterActivityTest() {
		super("com.marakana.android.currencyconverter",
				CurrencyConverterActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		super.setActivityInitialTouchMode(false);
		this.loadActivity();
	}

	private void loadActivity() {
		this.activity = super.getActivity();
		this.fromCurrency = (Spinner) this.activity
				.findViewById(R.id.from_currency);
		this.toCurrency = (Spinner) this.activity
				.findViewById(R.id.to_currency);
		this.inputAmount = (EditText) this.activity
				.findViewById(R.id.input_amount);
		this.outputAmount = (TextView) this.activity
				.findViewById(R.id.output_amount);
		this.convert = this.activity.findViewById(R.id.convert);
		this.reverseCurrencies = this.activity
				.findViewById(R.id.reverse_currencies);
		this.clearInput = this.activity.findViewById(R.id.clear_input);
		this.copyResult = this.activity.findViewById(R.id.copy_result);
		this.currencies = this.activity.getResources().getStringArray(
				R.array.currencies);
		this.currencyConverter = new GoogleCurrencyConverter();
	}

	public void testPreConditions() {
		super.assertNotNull(this.fromCurrency);
		super.assertNotNull(this.fromCurrency.getOnItemSelectedListener());
		super.assertEquals(currencies.length, this.fromCurrency.getAdapter()
				.getCount());

		super.assertNotNull(this.toCurrency);
		super.assertNotNull(this.toCurrency.getOnItemSelectedListener());
		super.assertEquals(currencies.length, this.toCurrency.getAdapter()
				.getCount());

		super.assertNotNull(this.inputAmount);
		super.assertEquals("1", this.inputAmount.getText().toString());

		super.assertNotNull(this.outputAmount);

		super.assertNotNull(this.convert);
		super.assertNotNull(this.reverseCurrencies);
		super.assertNotNull(this.clearInput);
		super.assertNotNull(this.copyResult);
	}

	public void testViewsVisible() {
		View rootView = this.activity.getCurrentFocus().getRootView();
		ViewAsserts.assertOnScreen(rootView, this.fromCurrency);
		ViewAsserts.assertOnScreen(rootView, this.toCurrency);
		ViewAsserts.assertOnScreen(rootView, this.inputAmount);
		ViewAsserts.assertOnScreen(rootView, this.outputAmount);
		ViewAsserts.assertOnScreen(rootView, this.convert);
		ViewAsserts.assertOnScreen(rootView, this.reverseCurrencies);
		ViewAsserts.assertOnScreen(rootView, this.clearInput);
		ViewAsserts.assertOnScreen(rootView, this.copyResult);
	}

	@UiThreadTest
	public void testSaveInstanceState() {
		this.fromCurrency.setSelection(1);
		this.toCurrency.setSelection(2);
		this.inputAmount.setText("123");
		this.activity.finish();
		this.loadActivity();
		super.assertEquals(1, this.fromCurrency.getSelectedItemPosition());
		super.assertEquals(2, this.toCurrency.getSelectedItemPosition());
		super.assertEquals("123", this.inputAmount.getText().toString());
	}

	public void testCopyResult() {
		ClipboardManager clipboard = (ClipboardManager) activity
				.getSystemService(Context.CLIPBOARD_SERVICE);
		CharSequence current = clipboard.getText();
		try {
			clipboard.setText("12345");
			TouchUtils.clickView(this, this.clearInput);
			super.getInstrumentation().waitForIdleSync();
			TouchUtils.tapView(this, this.inputAmount);
			super.getInstrumentation().waitForIdleSync();
			super.sendKeys(KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7,
					KeyEvent.KEYCODE_8);
			super.getInstrumentation().waitForIdleSync();
			TouchUtils.clickView(this, this.convert);
			super.getInstrumentation().waitForIdleSync();
			TouchUtils.clickView(this, this.copyResult);
			super.getInstrumentation().waitForIdleSync();
			super.assertEquals("678", clipboard.getText().toString());
		} finally {
			clipboard.setText(current);
		}
	}

	public void testConversion() {
		int fromCurrencyPosition = this.currencies.length / 2;
		int toCurrencyPosition = this.currencies.length / 4;
		this.makeSelection(fromCurrency, fromCurrencyPosition);
		this.makeSelection(toCurrency, toCurrencyPosition);
		TouchUtils.clickView(this, this.clearInput);
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.tapView(this, this.inputAmount);
		super.sendKeys(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2,
				KeyEvent.KEYCODE_3);
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.convert);
		super.getInstrumentation().waitForIdleSync();
		double rate = this.currencyConverter.getConversionRate(
				this.currencies[fromCurrencyPosition],
				this.currencies[toCurrencyPosition]);
		assertEquals(rate * 123,
				Double.parseDouble(this.outputAmount.getText().toString()), 5);
	}

	public void testReverseCurrencies() {
		int fromCurrencyPosition = this.currencies.length / 2;
		int toCurrencyPosition = this.currencies.length / 3;
		this.makeSelection(fromCurrency, fromCurrencyPosition);
		this.makeSelection(toCurrency, toCurrencyPosition);
		TouchUtils.clickView(this, this.reverseCurrencies);
		assertEquals(toCurrencyPosition, this.fromCurrency.getSelectedItemPosition());
		assertEquals(fromCurrencyPosition, this.toCurrency.getSelectedItemPosition());
	}
	
	private void makeSelection(final Spinner spinner, int position) {
		this.activity.runOnUiThread(new Runnable() {
			public void run() {
				spinner.requestFocus();
				spinner.setSelection(0);
			}
		});
		super.getInstrumentation().waitForIdleSync();
		super.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		for (int i = 1; i <= position; i++) {
			super.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		super.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	}
}

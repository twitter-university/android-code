package com.marakana.android.currencyconverter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyConverterActivity extends Activity implements
		OnClickListener, OnItemSelectedListener, TextWatcher {
	private static final String TAG = "UnitConverterActivity";

	private Spinner fromCurrency;
	private Spinner toCurrency;
	private EditText inputAmount;
	private TextView outputAmount;
	private View convert;
	private View reverseCurrencies;
	private View clearInput;
	private View copyResult;
	private CurrencyConverter currencyConverter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currency_converter);

		this.fromCurrency = (Spinner) super.findViewById(R.id.from_currency);
		this.fromCurrency.setOnItemSelectedListener(this);

		this.toCurrency = (Spinner) super.findViewById(R.id.to_currency);
		this.toCurrency.setOnItemSelectedListener(this);

		this.inputAmount = (EditText) super.findViewById(R.id.input_amount);
		this.inputAmount.setText("1");
		this.inputAmount.addTextChangedListener(this);

		this.outputAmount = (TextView) super.findViewById(R.id.output_amount);

		this.convert = super.findViewById(R.id.convert);
		this.convert.setOnClickListener(this);

		this.reverseCurrencies = super.findViewById(R.id.reverse_currencies);
		this.reverseCurrencies.setOnClickListener(this);

		this.clearInput = super.findViewById(R.id.clear_input);
		this.clearInput.setOnClickListener(this);

		this.copyResult = super.findViewById(R.id.copy_result);
		this.copyResult.setOnClickListener(this);

		this.currencyConverter = new GoogleCurrencyConverter();
	}

	public void onClick(View view) {
		if (view == this.convert) {
			this.doConversion();
		} else if (view == this.reverseCurrencies) {
			this.doReverseCurrencies();
		} else if (view == this.clearInput) {
			this.doClearInput();
		} else if (view == this.copyResult) {
			this.doCopyResult();
		} else {
			throw new AssertionError("Unexpected click on view: " + view);
		}
	}

	public void afterTextChanged(Editable s) {
		this.clearOutput();
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// ignored
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// ignored
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		this.clearOutput();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		this.clearOutput();
	}

	private void doReverseCurrencies() {
		int position = this.fromCurrency.getSelectedItemPosition();
		this.fromCurrency.setSelection(this.toCurrency
				.getSelectedItemPosition());
		this.toCurrency.setSelection(position);
	}

	private void doCopyResult() {
		CharSequence result = this.outputAmount.getText();
		if (result.length() > 0) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(result);
			Toast.makeText(this,
					super.getString(R.string.copy_result_message, result),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void doClearInput() {
		this.inputAmount.setText("");
		this.clearOutput();
	}

	private void doConversion() {
		final String fromCurrencyCode = this.fromCurrency.getSelectedItem()
				.toString();
		final String toCurrencyCode = this.toCurrency.getSelectedItem()
				.toString();
		if (fromCurrencyCode.equals(toCurrencyCode)) {
			this.setOutput(this.inputAmount.getText());
		} else {
			try {
				final double amount = Double.parseDouble(this.inputAmount
						.getText().toString());
				new AsyncTask<Void, Void, Double>() {
					@Override
					protected Double doInBackground(Void... params) {
						long t = System.currentTimeMillis();
						try {
							double rate = CurrencyConverterActivity.this.currencyConverter
									.getConversionRate(fromCurrencyCode,
											toCurrencyCode);
							t = System.currentTimeMillis() - t;
							Log.d(TAG, "Got " + fromCurrencyCode + "->"
									+ toCurrencyCode + " rate " + rate + " in "
									+ t + " ms");
							return rate;
						} catch (CurrencyConverterException e) {
							Log.e(TAG, "Failed to convert " + fromCurrencyCode
									+ "->" + toCurrencyCode, e);
							return null;
						}
					}

					@Override
					protected void onPostExecute(Double rate) {
						if (rate == null) {
							CurrencyConverterActivity.this.clearOutput();
							Toast.makeText(CurrencyConverterActivity.this,
									R.string.convert_failure,
									Toast.LENGTH_SHORT).show();
						} else {
							CurrencyConverterActivity.this.setOutput(String
									.valueOf(rate * amount));
						}
					}
				}.execute();
			} catch (NumberFormatException e) {
				this.clearOutput();
			}
		}
	}

	private void setOutput(CharSequence output) {
		this.outputAmount.setText(output);
		this.copyResult.setVisibility(View.VISIBLE);
	}

	private void clearOutput() {
		this.outputAmount.setText("");
		this.copyResult.setVisibility(View.INVISIBLE);
	}

}
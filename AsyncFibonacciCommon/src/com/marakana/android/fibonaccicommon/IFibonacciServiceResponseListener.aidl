package com.marakana.android.fibonaccicommon;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;

interface IFibonacciServiceResponseListener {
    void onResponse(in FibonacciResponse response);
}
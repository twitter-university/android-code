package com.marakana.android.fibonaccicommon;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;
import com.marakana.android.fibonaccicommon.IFibonacciServiceResponseListener;

oneway interface IFibonacciService {
    void fib(in FibonacciRequest request, in IFibonacciServiceResponseListener listener);
}
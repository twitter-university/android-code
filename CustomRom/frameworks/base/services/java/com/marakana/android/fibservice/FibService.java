package com.marakana.android.fibservice;

public class FibService extends IFibService.Stub {
    public long fib(long n) {
        long previous = -1;
        long result = 1;
        for (long i = 0; i <= n; i++) {
            long sum = result + previous;
            previous = result;
            result = sum;
        }
        return result;
    }
}

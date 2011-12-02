package com.example.fibcommon;

import com.example.fibcommon.FibResponse;

interface IFibService {
  long fib(long n);
  FibResponse fibResponse(long n);
}
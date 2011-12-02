package com.example.fibservice;

public class FibLib {

  static {
    System.loadLibrary("fib");
  }
  
  public static native long fib(long n);
}

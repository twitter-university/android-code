package com.marakana;

import android.util.Log;

public class FibLib {

  /*
   * fib(0) = 0; fib(1) = 1; fib(n) = fib(n-1) + fib(n-2)
   */

  // Java implementation
  public static int fibJ(int n) { // <1>
    if (n == 0)
      return 0;
    if (n == 1)
      return 1;
    return fibJ(n - 1) + fibJ(n - 2);
  }

  static {
    System.loadLibrary("fib"); // <2>
  }

  // Native version
  public static native int fibN(int n); // <3>
  
  public static void sayHello() {
    Log.d("KitchenSink", "Hello!");
  }
}

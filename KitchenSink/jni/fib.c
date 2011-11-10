#include "com_marakana_FibLib.h"  /* <1> */

/* Fibonacci algorithm, C implementation <2> */
jint fib(jint n) {  
  if(n==0) return 0;
  if(n==1) return 1;
  return fib(n-1) + fib(n-2);
}

/* JNI Call <3> */
JNIEXPORT jint JNICALL Java_com_marakana_FibLib_fibN
  (JNIEnv *env, jclass obj, jint n) {
  return fib(n); 
  }

#include "com_example_fibservice_FibLib.h"

jlong fib(jlong n) {
	if(n==0) return 0;
	if(n==1) return 1;
	return fib(n-1)+fib(n-2);
}

JNIEXPORT jlong JNICALL Java_com_example_fibservice_FibLib_fib
  (JNIEnv *env, jclass clazz, jlong n) {
	return fib(n);
}

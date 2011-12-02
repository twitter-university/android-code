#include "com_marakana_android_fibonacci_FibLib.h"
#include <android/log.h>

#define TAG "FibLib.c"

static jlong fib(jlong n) {
  return n <= 0 ? 0 : n == 1 ? 1 : fib(n - 1) + fib(n - 2);
}

JNIEXPORT jlong JNICALL Java_com_marakana_android_fibonacci_FibLib_fibNR
  (JNIEnv *env, jclass clazz, jlong n) {
  __android_log_print(ANDROID_LOG_DEBUG, TAG, "Running fibNR(%lld)", n);
  return fib(n);
}

JNIEXPORT jlong JNICALL Java_com_marakana_android_fibonacci_FibLib_fibNI
  (JNIEnv *env, jclass clazz, jlong n) {
  __android_log_print(ANDROID_LOG_DEBUG, TAG, "Running fibNI(%lld)", n);
  jlong previous = -1;
  jlong result = 1;
  jlong i;
  jlong sum;
  for (i = 0; i <= n; i++) {
    sum = result + previous;
    previous = result;
    result = sum;
  }
  return result;
}

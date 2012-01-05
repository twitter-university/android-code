#include <libmrknlog.h>
#include "com_marakana_android_lib_log_LibLog.h"

static void ThrowLibLogException(JNIEnv *env, const char *message) {
  jclass class = (*env)->FindClass(env, "com/marakana/android/lib/log/LibLogException");
  if (class) {
    (*env)->ThrowNew(env, class, message);
    (*env)->DeleteLocalRef(env, class);
  }
}

JNIEXPORT void JNICALL Java_com_marakana_android_lib_log_LibLog_flushLog
  (JNIEnv *env, jclass clazz) {
  if (mrkn_flush_log() != 0) {
    ThrowLibLogException(env, "Failed to flush log");
  }
}

JNIEXPORT jint JNICALL Java_com_marakana_android_lib_log_LibLog_getTotalLogSize
  (JNIEnv *env, jclass clazz) {
  jint result = mrkn_get_total_log_size();
  if (result < 0) {
    ThrowLibLogException(env, "Failed to get total log size");
  }
  return result;
}

JNIEXPORT jint JNICALL Java_com_marakana_android_lib_log_LibLog_getUsedLogSize
  (JNIEnv *env, jclass clazz) {
  jint result = mrkn_get_used_log_size();
  if (result < 0) {
    ThrowLibLogException(env, "Failed to get used log size");
  }
  return result;
}

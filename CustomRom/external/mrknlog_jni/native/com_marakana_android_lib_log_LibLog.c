#include <mrknlog.h>
#include "com_marakana_android_lib_log_LibLog.h"

JNIEXPORT void JNICALL Java_com_marakana_android_lib_log_LibLog_flushLog
  (JNIEnv *env, jclass clazz) {
  mrkn_flush_log();  
}

JNIEXPORT jint JNICALL Java_com_marakana_android_lib_log_LibLog_getTotalLogSize
  (JNIEnv *env, jclass clazz) {
  return mrkn_get_total_log_size();
}

JNIEXPORT jint JNICALL Java_com_marakana_android_lib_log_LibLog_getUsedLogSize
  (JNIEnv *env, jclass clazz) {
  return mrkn_get_used_log_size();
}

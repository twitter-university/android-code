LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := com_marakana_android_lib_log_LibLog.c
LOCAL_C_INCLUDES += $(JNI_H_INCLUDE) $(LOCAL_PATH)/../../../lib/libmrknlog
LOCAL_SHARED_LIBRARIES := libmrknlog
LOCAL_MODULE := libmrknlog_jni
include $(BUILD_SHARED_LIBRARY)

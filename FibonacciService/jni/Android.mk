LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := com_marakana_android_fibonacci_FibLib.c
LOCAL_LDLIBS += -llog
LOCAL_MODULE    := com_marakana_android_fibonacci_FibLib
include $(BUILD_SHARED_LIBRARY)

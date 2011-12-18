# <1>
LOCAL_PATH := $(call my-dir)

# <2>
include $(CLEAR_VARS)

# <3>
LOCAL_SRC_FILES := com_marakana_android_fibonaccinative_FibLib.c

# <4>
LOCAL_MODULE    := com_marakana_android_fibonaccinative_FibLib

# <5>
include $(BUILD_SHARED_LIBRARY)



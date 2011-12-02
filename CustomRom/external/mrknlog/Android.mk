LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := mrknlog.c
LOCAL_SHARED_LIBRARIES := libcutils libutils libc
LOCAL_MODULE := libmrknlog

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := main.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_SHARED_LIBRARIES := libmrknlog libc
LOCAL_MODULE := mrknlog

include $(BUILD_EXECUTABLE)


LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := libmrknlog.c
LOCAL_SHARED_LIBRARIES := libcutils libutils libc
LOCAL_MODULE := libmrknlog
LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)
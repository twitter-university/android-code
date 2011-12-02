LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES:= mrkn_alive.c
LOCAL_MODULE := mrkn_alive
LOCAL_SHARED_LIBRARIES := \
    libcutils \
    libutils
include $(BUILD_EXECUTABLE)

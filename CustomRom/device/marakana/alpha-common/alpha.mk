MY_PATH := $(LOCAL_PATH)/../alpha-common

# Enable our custom kernel
LOCAL_KERNEL := $(MY_PATH)/kernel
PRODUCT_COPY_FILES += $(LOCAL_KERNEL):kernel

# Copy our init.rc file over the existing one (since our device contains extra changes)
PRODUCT_COPY_FILES += $(MY_PATH)/init.alpha.rc:root/init.goldfish.rc

PRODUCT_PACKAGES += \
	mrkn_alive \
	libmrknlog	\
	mrknlog \
	com.marakana.android.lib.log \
	com.marakana.android.lib.log.xml \
	libmrknlog_jni \
	com.marakana.android.service.log \
	com.marakana.android.service.log.xml \
	MrknLogService

include $(call all-subdir-makefiles)
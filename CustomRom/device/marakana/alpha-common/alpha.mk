MY_PATH := $(LOCAL_PATH)/../alpha-common

include $(call all-subdir-makefiles)

# Enable our custom kernel
LOCAL_KERNEL := $(MY_PATH)/kernel
PRODUCT_COPY_FILES += $(LOCAL_KERNEL):kernel

# Copy our init.goldfish.rc file over the existing one (since ours contains extra changes)
PRODUCT_COPY_FILES += $(MY_PATH)/init.goldfish.rc:root/init.goldfish.rc

PRODUCT_PACKAGES += libmrknlog
PRODUCT_PACKAGES += mrknlog
PRODUCT_PACKAGES += mrknlogd

PRODUCT_PACKAGES += \
	com.marakana.android.lib.log \
	com.marakana.android.lib.log.xml \
	libmrknlog_jni

PRODUCT_PACKAGES += \
	com.marakana.android.service.log \
	com.marakana.android.service.log.xml

PRODUCT_PACKAGES += MrknLogService


# Include the common stuff
include device/marakana/alpha-common/alpha.mk

# List of modules to include in the the add-on system image
#PRODUCT_PACKAGES += 

# The name of this add-on (for the SDK)
PRODUCT_SDK_ADDON_NAME := marakana_alpha_addon

# Copy the following files for this add-on's SDK
PRODUCT_SDK_ADDON_COPY_FILES := \
	$(LOCAL_PATH)/manifest.ini:manifest.ini \
	$(LOCAL_PATH)/hardware.ini:hardware.ini \
	$(call find-copy-subdir-files,*,$(LOCAL_PATH)/skins/MrknHvgaMdpi,skins/MrknHvgaMdpi)


# Copy the jar files for the libraries (APIs) exposed in this add-on's SDK
PRODUCT_SDK_ADDON_COPY_MODULES := \
	com.marakana.android.lib.log:libs/com.marakana.android.lib.log.jar \
	com.marakana.android.service.log:libs/com.marakana.android.service.log.jar

PRODUCT_SDK_ADDON_STUB_DEFS := $(LOCAL_PATH)/alpha_sdk_addon_stub_defs.txt

# Define the name of the documentation to generate for this add-on's SDK
PRODUCT_SDK_ADDON_DOC_MODULES := \
	com.marakana.android.service.log_doc 

# Since the add-on is an emulator, we also need to explicitly copy the kernel to images
PRODUCT_SDK_ADDON_COPY_FILES += $(LOCAL_KERNEL):images/armeabi-v7a/kernel-qemu

# This add-on extends the default sdk product.
$(call inherit-product, $(SRC_TARGET_DIR)/product/sdk.mk)

# The name of this add-on (for the build system)
# Use 'make PRODUCT-<PRODUCT_NAME>-sdk_addon' to build the an add-on, 
# so in this case, we would run 'make PRODUCT-marakana_alpha_addon-sdk_addon'
PRODUCT_NAME := marakana_alpha_addon
PRODUCT_DEVICE := alpha
PRODUCT_MODEL := Marakana Alpha SDK Addon Image for Emulator

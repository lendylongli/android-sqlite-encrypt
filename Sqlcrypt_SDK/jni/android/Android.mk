LOCAL_PATH:= $(call my-dir)

libsqlite3_android_local_src_files := \
	sqlite3_android.cpp

libsqlite3_android_c_includes := \
        $(LOCAL_PATH)/../sqlite \

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES:= $(libsqlite3_android_local_src_files)
LOCAL_C_INCLUDES := $(libsqlite3_android_c_includes)
LOCAL_MODULE:= libsqlite3_sqlcrypt_android
include $(BUILD_STATIC_LIBRARY)

#ifeq ($(WITH_HOST_DALVIK),true)
#    include $(CLEAR_VARS)
#    LOCAL_SRC_FILES:= $(libsqlite3_android_local_src_files)
#    LOCAL_C_INCLUDES := $(libsqlite3_android_c_includes)
#    LOCAL_MODULE:= libsqlite3_android_mmc
#    include $(BUILD_HOST_STATIC_LIBRARY)
#endif

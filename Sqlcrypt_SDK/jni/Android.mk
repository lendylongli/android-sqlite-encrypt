LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES:= \
	com_sqlcrypt_database_SQLiteCompiledSql.cpp \
	com_sqlcrypt_database_SQLiteDebug.cpp \
	com_sqlcrypt_database_SQLiteDatabase.cpp \
	com_sqlcrypt_database_SQLiteProgram.cpp \
	com_sqlcrypt_database_SQLiteQuery.cpp \
	com_sqlcrypt_database_SQLiteStatement.cpp \
	CursorWindow.cpp \
	com_sqlcrypt_sqlite.cpp \
	com_sqlcrypt_database_CursorWindow.cpp \
	Unicode.cpp

common_sqlite_flags := \
			-DSQLITE_HAS_CODEC \
			-DSQLITE_CORE \
			-DTHREADSAFE \
			-DSQLITE_SECURE_DELETE \
			-DSQLITE_SOUNDEX \
			-DSQLITE_ENABLE_COLUMN_METADATA \
			-DTHREADSAFE=1 \
    			-DSQLITE_ENABLE_FTS3 \
    			-DSQLITE_ENABLE_FTS3_PARENTHESIS \
    			-DSQLITE_ENABLE_RTREE \
    			-DSQLITE_USE_URI \
    			-D_CRT_SECURE_NO_WARNINGS \
    			-D_CRT_SECURE_NO_DEPRECATE \
    			-D_CRT_NONSTDC_NO_DEPRECATE \
    			-DCODEC_TYPE=CODEC_TYPE_AES128 \
      			-DNDEBUG \
			-DUSE_PREAD

LOCAL_C_INCLUDES += \
	$(LOCAL_PATH)/sqlite \
	$(LOCAL_PATH)/android

LOCAL_SHARED_LIBRARIES := \
	libnativehelper \
	libandroid_runtime \
	libcutils \
	libutils \
	libbinder \
	libdl

LOCAL_STATIC_LIBRARIES := libsqlite3_sqlcrypt libsqlite3_sqlcrypt_android
LOCAL_LDLIBS += -lpthread -ldl -log
LOCAL_CFLAGS += $(common_sqlite_flags)
LOCAL_PRELINK_MODULE := false
LOCAL_MODULE:= libsqlcrypt_jni

include $(BUILD_SHARED_LIBRARY)
include $(call all-makefiles-under,$(LOCAL_PATH))

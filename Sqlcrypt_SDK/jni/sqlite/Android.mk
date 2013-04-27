##
##
## Build the library
##
##

LOCAL_PATH:= $(call my-dir)

# NOTE the following flags,
#   SQLITE_TEMP_STORE=3 causes all TEMP files to go into RAM. and thats the behavior we want
#   SQLITE_ENABLE_FTS3   enables usage of FTS3 - NOT FTS1 or 2.
#   SQLITE_DEFAULT_AUTOVACUUM=1  causes the databases to be subject to auto-vacuum

common_src_files := sqlite3secure.c \
		   sqlite3.def

# the device library
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(common_src_files)

ifneq ($(TARGET_ARCH),arm)
LOCAL_LDLIBS += -lpthread -ldl
endif

LOCAL_CFLAGS += $(common_sqlite_flags)

#LOCAL_SHARED_LIBRARIES := libdl

LOCAL_MODULE:= libsqlite3_sqlcrypt
#LOCAL_C_INCLUDES += $(call include-path-for, system-core)/cutils
#LOCAL_SHARED_LIBRARIES += liblog \
#            libutils
LOCAL_LDLIBS += -llog
# include android specific methods
#LOCAL_WHOLE_STATIC_LIBRARIES := libsqlite3_mmc

## Choose only one of the allocator systems below
# new sqlite 3.5.6 no longer support external allocator 
#LOCAL_SRC_FILES += mem_malloc.c
#LOCAL_SRC_FILES += mem_mspace.c

#include $(BUILD_SHARED_LIBRARY)
include $(BUILD_STATIC_LIBRARY)
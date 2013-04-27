#include "jni.h"
#include "JNIHelp.h"

#include <android_runtime/AndroidRuntime.h>
#include <utils/Log.h>

namespace sqlcrypt {

extern int register_com_sqlcrypt_database_CursorWindow(JNIEnv* env);
extern int register_com_sqlcrypt_database_SQLiteCompiledSql(JNIEnv* env);
extern int register_com_sqlcrypt_database_SQLiteDatabase(JNIEnv* env);
extern int register_com_sqlcrypt_database_SQLiteDebug(JNIEnv* env);
extern int register_com_sqlcrypt_database_SQLiteProgram(JNIEnv* env);
extern int register_com_sqlcrypt_database_SQLiteQuery(JNIEnv* env);
extern int register_com_sqlcrypt_database_SQLiteStatement(JNIEnv* env);
}
const char* TAG = "MMCSQLITE";

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
		JNIEnv* env = NULL;
    	jint result = JNI_ERR;

    	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) 
	{
        	__android_log_print(ANDROID_LOG_ERROR, TAG, "GetEnv failed!");
        	return result;
    	}
	sqlcrypt::register_com_sqlcrypt_database_CursorWindow(env);
	sqlcrypt::register_com_sqlcrypt_database_SQLiteCompiledSql(env);
	sqlcrypt::register_com_sqlcrypt_database_SQLiteDatabase(env);
	sqlcrypt::register_com_sqlcrypt_database_SQLiteDebug(env);
	sqlcrypt::register_com_sqlcrypt_database_SQLiteProgram(env);
	sqlcrypt::register_com_sqlcrypt_database_SQLiteQuery(env);
	sqlcrypt::register_com_sqlcrypt_database_SQLiteStatement(env);
	  
	return JNI_VERSION_1_4;
}

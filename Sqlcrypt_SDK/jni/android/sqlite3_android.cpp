/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "sqlite3_android"

#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <cutils/log.h>

#include "sqlite3_android.h"


extern "C" int register_localized_collators(sqlite3* handle, const char* systemLocale, int utf16Storage)
{
    return SQLITE_OK;
}


extern "C" int register_android_functions(sqlite3 * handle, int utf16Storage)
{
    return SQLITE_OK;
}

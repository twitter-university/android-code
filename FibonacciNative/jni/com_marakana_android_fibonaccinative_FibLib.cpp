#include "jni.h"

/***********************************************************
 * This is an alternative implementation that demonstrates *
 * how to pre-link native funcitons to the managed JNI     *
 * environment.                                            *
 * To test, switch to this file in Android.mk              *
 ***********************************************************/

#ifndef NELEM
# define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#endif

namespace com_marakana_android_fibonaccinative {

    static jlong fib(jlong n) {
        return n <= 0 ? 0 : n == 1 ? 1 : fib(n - 1) + fib(n - 2);
    }

    static jlong fibNR(JNIEnv *env, jclass clazz, jlong n) {
        return fib(n);
    }

    static jlong fibNI(JNIEnv *env, jclass clazz, jlong n) {
        jlong previous = -1;
        jlong result = 1;
        jlong i;
        for (i = 0; i <= n; i++) {
            jlong sum = result + previous;
            previous = result;
            result = sum;
        }
        return result;
    }

    // see http://docs.oracle.com/javase/6/docs/technotes/guides/jni/spec/types.html
    static JNINativeMethod method_table[] = {
        { "fibNR", "(J)J", (void*)fibNR },
        { "fibNI", "(J)J", (void*)fibNI }
    };
}

using namespace com_marakana_android_fibonaccinative;

// See dalvik/libnativehelper/JNIHelp.cpp
static int jniRegisterNativeMethods(JNIEnv* env, const char* className,
        const JNINativeMethod* gMethods, int numMethods) {
    jclass clazz = env->FindClass(className);
    if (clazz) {
        env->RegisterNatives(clazz, gMethods, numMethods);
        env->DeleteLocalRef(clazz);
        return 0;
    } else {
        return -1;
    }
}

extern "C" jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    } else {
        // see http://java.sun.com/docs/books/jni/html/other.html#29535
        if (jniRegisterNativeMethods(env, "com/marakana/android/fibonaccinative/FibLib",
                method_table, NELEM(method_table)) != 0) {
            return -2;
        }
    }
    return JNI_VERSION_1_6;
}


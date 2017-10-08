#include <jni.h>
#include<android/bitmap.h>
#include <string>
using namespace std;
extern "C"
JNIEXPORT jint JNICALL Java_com_example_test_nativeprocess_processBitmap(JNIEnv *env, jclass,jobject bmpObj) {
    AndroidBitmapInfo bmpInfo = {0};
    if(AndroidBitmap_getInfo(env, bmpObj, &bmpInfo)<0) {
        return -1;
    }
    int* dataFromBmp = NULL;
    if(AndroidBitmap_lockPixels(env, bmpObj, (void**)&dataFromBmp)) {
        return -1;
    }
    
}
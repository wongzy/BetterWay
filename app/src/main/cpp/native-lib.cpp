#include <jni.h>
#include <android/log.h>
#include <opencv2/opencv.hpp> // 引入opencv库头文件

// 定义了log日志宏函数，方便打印日志在logcat中查看调试
#define TAG "Jerry-NDK-Image-Pro"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG , TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO , TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN , TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR , TAG, __VA_ARGS__)
using namespace cv;
extern "C"
JNIEXPORT void JNICALL Java_com_android_betterway_utils_BlurUtil_blurImage(
        JNIEnv *env,
        jclass jcls,
        jintArray jarr_pixels,
        jint j_width,
        jint j_height) {
    // 获取java中传入的像素数组值，jintArray转化成jint指针数组
    jint *c_pixels = env->GetIntArrayElements(jarr_pixels, JNI_FALSE);
    if(c_pixels == NULL){
        return;
    }
    LOGE("图片宽度：%d, 高度：%d", j_width, j_height);
    // 把c的图片数据转化成opencv的图片数据
    // 使用Mat创建图片
    Mat mat_image_src(j_height, j_width, CV_8UC4, (unsigned char*) c_pixels);
    // 选择和截取一段行范围的图片
    Mat temp = mat_image_src.rowRange(j_height,j_height);
    // 方框滤波
//    boxFilter(temp, temp, -1, Size(85, 85));
    // 均值滤波
    blur(temp, temp, Size(85, 85));
    // 使用高斯模糊滤波
    // GaussianBlur(temp, temp, Size(45, 13), 0, 0);
    // 将opencv图片转化成c图片数据，RGBA转化成灰度图4通道颜色数据
    cvtColor(temp, temp, CV_RGBA2GRAY, 4);

    // 更新java图片数组和释放c++中图片数组的值
    env->ReleaseIntArrayElements(jarr_pixels, c_pixels, JNI_FALSE);
}
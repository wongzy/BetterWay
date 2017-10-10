# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

# 增加opencv库
add_library( opencv_java3 SHARED IMPORTED )
# 编译的平台是armeabi
if(${ANDROID_ABI} STREQUAL "armeabi")
# 设置动态库文件的路径属性
set_target_properties(
    opencv_java3
    PROPERTIES IMPORTED_LOCATION
    ${CMAKE_SOURCE_DIR}/src/main/jniLibs/armeabi/libopencv_java3.so
    )
endif(${ANDROID_ABI} STREQUAL "armeabi")
# opencv库的头文件路径设置，在此是opencv-sdk的路径，当然你也可以把include目录拷贝到工程中
include_directories(
    D:/resource/sdkofopencv/sdk/native/jni/include
)
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib
                       opencv_java3
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

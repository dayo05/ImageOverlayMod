#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
using namespace cv;

#define IMAGE 1
#define VIDEO 2
static Mat image;
static VideoCapture video;
static void* videoFrame;
static int vw;
static int vh;
static double vfps;
static int mod = 0;

extern "C" {
JNIEXPORT jobject Java_me_ddayo_imageoverlaymod_client_util_ResourceUtil_getImage(JNIEnv* env, jobject obj, jstring jname) {
    const char* image_name = env->GetStringUTFChars(jname, 0);

    image.release();
    image = imread(image_name, IMREAD_UNCHANGED);
    cvtColor(image, image, COLOR_BGRA2RGBA);
    vw = image.size().width;
    vh = image.size().height;
    env->ReleaseStringUTFChars(jname, image_name);
    mod = IMAGE;
    return env->NewDirectByteBuffer(image.data, vw * vh * 4);
}

JNIEXPORT jobject Java_me_ddayo_imageoverlaymod_client_util_ResourceUtil_getVideo(JNIEnv* env, jobject obj, jstring jname) {
    const char* video_name = env->GetStringUTFChars(jname, 0);

    video = VideoCapture(video_name);
    vfps = video.get(CAP_PROP_FPS);
    vw = video.get(CAP_PROP_FRAME_WIDTH);
    vh = video.get(CAP_PROP_FRAME_HEIGHT);
    env->ReleaseStringUTFChars(jname, video_name);
    videoFrame = malloc(vw * vh * 3);
    mod = VIDEO;
    return env->NewDirectByteBuffer(videoFrame, vw * vh * 3);
}

JNIEXPORT jint Java_me_ddayo_imageoverlaymod_client_util_ResourceUtil_getMediaWidth(JNIEnv* env, jobject obj) {
    if(mod == 0) return -1;
    return vw;
}

JNIEXPORT jint Java_me_ddayo_imageoverlaymod_client_util_ResourceUtil_getMediaHeight(JNIEnv* env, jobject obj) {
    if(mod == 0) return -1;
    return vh;
}

JNIEXPORT jboolean Java_me_ddayo_imageoverlaymod_client_util_ResourceUtil_nextFrame(JNIEnv* env, jobject obj) {
    if(mod != VIDEO) return JNI_FALSE;
    //if(!image.empty()) image.release();
    video >> image;
    if(image.empty()) return JNI_FALSE;
    cvtColor(image, image, COLOR_BGR2RGB);
    memcpy(videoFrame, image.data, vw * vh * 3);
    return JNI_TRUE;
}

JNIEXPORT jdouble Java_me_ddayo_imageoverlaymod_client_util_ResourceUtil_getFrameRate(JNIEnv* env, jobject obj) {
    if(mod != VIDEO) return -1.0;
    return vfps;
}
}
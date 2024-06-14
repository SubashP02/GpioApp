#include <jni.h>
#include <string>
#include <android/log.h>
#include <linux/gpio.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>


#define LOG_TAG "NativeLib"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ledscroll_MainActivity_sendToJNI(JNIEnv* env, jobject /* this */, jstring selectedItem, int inputText,
                                                  int value) {
    const char *gpioPath = env->GetStringUTFChars(selectedItem, 0);
    int gpioFd = open(gpioPath, O_RDWR | O_CLOEXEC);
    env->ReleaseStringUTFChars(selectedItem, gpioPath);

    if (gpioFd == -1) {
        std::string errorMsg = "Unable to open gpio device: ";
        errorMsg += std::strerror(errno);
        return env->NewStringUTF(errorMsg.c_str());
    } else {
        struct gpiohandle_request req;
        struct gpiohandle_data data;

        req.lineoffsets[0] = inputText;

        req.flags = GPIOHANDLE_REQUEST_OUTPUT;

        req.lines = 1;

        if (ioctl(gpioFd, GPIO_GET_LINEHANDLE_IOCTL, &req) < 0) {
            close(gpioFd);
            std::string errorMsg = "Error requesting GPIO lines: ";
            errorMsg += std::strerror(errno);
            return env->NewStringUTF(errorMsg.c_str());
        }


        data.values[0] = value;


        if (ioctl(req.fd, GPIOHANDLE_SET_LINE_VALUES_IOCTL, &data) < 0) {
            close(req.fd);
            close(gpioFd);
            std::string errorMsg = "Error setting GPIO line values: ";
            errorMsg += std::strerror(errno); // Convert errno to a readable string
            return env->NewStringUTF(errorMsg.c_str());
        }


        close(req.fd);


        close(gpioFd);
        if(data.values[0] == 1){
            std::string successMsg = "Turned ON";
            return env->NewStringUTF(successMsg.c_str());
        }else{
            std::string successMsg = "Turned OFF";
            return env->NewStringUTF(successMsg.c_str());
        }
    }
}




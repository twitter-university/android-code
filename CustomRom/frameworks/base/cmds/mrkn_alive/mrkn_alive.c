#define LOG_TAG "MRKN Alive Service"
#define FREQUENCY 10
#include <utils/Log.h>
#include <unistd.h>
int main(int argc, char **argv) {
    LOGI("Started");
    int elapsed = 0;
    while(1) {
         sleep(FREQUENCY);
         elapsed += FREQUENCY;
         LOGI("I've been alive for %d seconds", elapsed);
    }
}

#define LOG_FILE "/dev/log/main"
#define LOG_TAG "MrknLog"

#include "libmrknlog.h"

static int ioctl_log(int mode, int request) {
    int logfd = open(LOG_FILE, mode);
    if (logfd < 0) {
        LOGE("Failed to open %s: %s", LOG_FILE, strerror(errno));
        return -1;
    } else {
        int ret = ioctl(logfd, request);
        close(logfd);
        return ret;
    }
}

extern int mrkn_flush_log() {
    return ioctl_log(O_WRONLY, LOGGER_FLUSH_LOG);
}

extern int mrkn_get_total_log_size() {
    return ioctl_log(O_RDONLY, LOGGER_GET_LOG_BUF_SIZE);
}

extern int mrkn_get_used_log_size() {
    return ioctl_log(O_RDONLY, LOGGER_GET_LOG_LEN);
}


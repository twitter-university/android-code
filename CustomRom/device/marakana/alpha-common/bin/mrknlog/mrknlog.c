#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <libmrknlog.h>

int main (int argc, char* argv[]) {
    int usedSize = mrkn_get_used_log_size();
    int totalSize = mrkn_get_total_log_size();
    if (totalSize >= 0 && usedSize >= 0) {
        if (mrkn_flush_log() == 0) {
            printf("Flushed log. Previously it was consuming %d of %d bytes\n",
                usedSize, totalSize);
            return 0;
        } else {
            fprintf(stderr, "Failed to flush log: %s", strerror(errno));
        }
    } else {
        fprintf(stderr, "Failed to get log size: %s", strerror(errno));
    }
    return -1;
}

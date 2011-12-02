#define LOG_TAG "MRKN Log Daemon"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <utils/Log.h>
#include <libmrknlog.h>

int main (int argc, char* argv[]) {
  if (argc != 2) {
    fprintf(stderr, "Usage: %s <flush-frequency-in-seconds>\n", argv[0]);
    exit(2);
  } else {
    int frequency = atoi(argv[1]);
    int totalSize = mrkn_get_total_log_size();
    int usedSize;
    int count = 1;
    while(1) {
      usedSize = mrkn_get_used_log_size();
      if (mrkn_flush_log() == 0) {
        LOGI("Flushed log (%d, %d of %d bytes). Waiting %d seconds before the next flush.", 
          count, usedSize, totalSize, frequency);
        count++;
      } else {
        LOGE("Failed to flush log. Waiting %d seconds before the next attempt",
          frequency);
      }
      sleep(frequency);
    }
  }
}

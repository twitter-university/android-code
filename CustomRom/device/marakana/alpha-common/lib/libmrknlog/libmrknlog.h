#ifndef _MRKNLOG_H_
#define _MRKNLOG_H_

#include <cutils/logger.h>
#include <cutils/log.h>
#include <string.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/stat.h>
#include <sys/types.h>

#ifdef __cplusplus
extern "C" {
#endif

extern int mrkn_flush_log();
extern int mrkn_get_total_log_size();
extern int mrkn_get_used_log_size();

#ifdef __cplusplus
}  /* End of the 'extern "C"' block */
#endif
#endif /* End of the _MRKNLOG_H_ block */

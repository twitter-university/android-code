package com.marakana.android.service.log;

/**
 * System-private API for talking to the LogService.
 *
 * {@hide}
 */
interface ILogService {
  void flushLog();
  int getTotalLogSize();
  int getUsedLogSize();
}

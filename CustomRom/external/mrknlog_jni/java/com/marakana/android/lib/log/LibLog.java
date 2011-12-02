package com.marakana.android.lib.log;

public class LibLog {
  public native static void flushLog();
  public native static int getTotalLogSize();
  public native static int getUsedLogSize();

  static {
     System.loadLibrary("mrknlog_jni");
  }
}

package com.marakana.android.lib.log;

public class Main {
  public static void main (String[] args) {
    int usedSize = LibLog.getUsedLogSize();
    int totalSize = LibLog.getTotalLogSize();
    LibLog.flushLog();
    System.out.printf("Flushed log. Previously it was consuming %d of %d bytes\n",
        usedSize, totalSize);
  }
}

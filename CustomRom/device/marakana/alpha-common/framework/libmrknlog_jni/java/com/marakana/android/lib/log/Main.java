package com.marakana.android.lib.log;

/** @hide */
public class Main {
  public static void main (String[] args) {
	try {
      int usedSize = LibLog.getUsedLogSize();
      int totalSize = LibLog.getTotalLogSize();
      LibLog.flushLog();
      System.out.printf("Flushed log. Previously it was consuming %d of %d bytes\n",
          usedSize, totalSize);
    } catch (LibLogException e) {
	  System.err.println("Failed to flush the log");
	  e.printStackTrace();
    }
  }
}

package com.marakana.logservice; // <1>
 
import com.marakana.logservice.Message; // <2>

interface ILogService { //<3>
  void log_d(String tag, String message); // <4>
  void log(in Message msg); // <5>
}
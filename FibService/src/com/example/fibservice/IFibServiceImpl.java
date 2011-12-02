package com.example.fibservice;
import android.os.RemoteException;
import android.util.Log;

import com.example.fibcommon.FibResponse;
import com.example.fibcommon.IFibService;

public class IFibServiceImpl extends IFibService.Stub {

  @Override
  public long fib(long n) throws RemoteException {
    Log.d("IFibServiceImpl", "fib(n) calling FibLib.fib(n)");
    return FibLib.fib(n);
  }

  @Override
  public FibResponse fibResponse(long n) throws RemoteException {
    return new FibResponse(n);
  }

}

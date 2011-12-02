package com.example.fibcommon;

import android.os.Parcel;
import android.os.Parcelable;

public class FibResponse implements Parcelable {
  long n;

  public FibResponse(long n) {
    this.n = n;
  }
  
  public FibResponse(Parcel parcel) {
    n = parcel.readLong();
  }

  @Override
  public void writeToParcel(Parcel parcel, int flag) {
    parcel.writeLong(n);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<FibResponse> CREATOR = new Parcelable.Creator<FibResponse>() {

    @Override
    public FibResponse createFromParcel(Parcel parcel) {
      return new FibResponse(parcel);
    }

    @Override
    public FibResponse[] newArray(int size) {
      return new FibResponse[size];
    }
  };
}

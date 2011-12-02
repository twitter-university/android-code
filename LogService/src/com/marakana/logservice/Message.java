package com.marakana.logservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable { // <1>
  private String tag;
  private String text;

  public Message(Parcel in) { // <2>
    tag = in.readString();
    text = in.readString();
  }

  public void writeToParcel(Parcel out, int flags) { // <3>
    out.writeString(tag);
    out.writeString(text);
  }

  public int describeContents() { // <4>
    return 0;
  }

  public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() { // <5>

    public Message createFromParcel(Parcel source) {
      return new Message(source);
    }

    public Message[] newArray(int size) {
      return new Message[size];
    }

  };

  // Setters and Getters <6>
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}

package me.tysheng.xishi.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Sty
 * Date: 16/8/22 22:11.
 */
public class Album implements Parcelable{
    public String id;
    public String title;
    public String url;
    public String addtime;
    public String adshow;
    public String fabu;
    public String encoded;
    public String amd5;
    public String sort;
    public String ds;
    public String timing;
    public String timingpublish;

    public Album() {
    }

    protected Album(Parcel in) {
        id = in.readString();
        title = in.readString();
        url = in.readString();
        addtime = in.readString();
        adshow = in.readString();
        fabu = in.readString();
        encoded = in.readString();
        amd5 = in.readString();
        sort = in.readString();
        ds = in.readString();
        timing = in.readString();
        timingpublish = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(addtime);
        dest.writeString(adshow);
        dest.writeString(fabu);
        dest.writeString(encoded);
        dest.writeString(amd5);
        dest.writeString(sort);
        dest.writeString(ds);
        dest.writeString(timing);
        dest.writeString(timingpublish);
    }
}

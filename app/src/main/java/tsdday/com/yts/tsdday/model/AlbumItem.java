package tsdday.com.yts.tsdday.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlbumItem extends RealmObject implements Parcelable {
    public byte[] imageData;
    public String imageDataPath;

    public String content;


    public String getImageDataPath() {
        return imageDataPath;
    }

    public void setImageDataPath(String imageDataPath) {
        this.imageDataPath = imageDataPath;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public AlbumItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.imageData);
        dest.writeString(this.imageDataPath);
        dest.writeString(this.content);
    }

    protected AlbumItem(Parcel in) {
        this.imageData = in.createByteArray();
        this.imageDataPath = in.readString();
        this.content = in.readString();
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
        @Override
        public AlbumItem createFromParcel(Parcel source) {
            return new AlbumItem(source);
        }

        @Override
        public AlbumItem[] newArray(int size) {
            return new AlbumItem[size];
        }
    };
}

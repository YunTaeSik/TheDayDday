package tsdday.com.yts.tsdday.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    public String name;
    public String birth;
    public byte[] imageData;
    public String imageDataPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageDataPath() {
        return imageDataPath;
    }

    public void setImageDataPath(String imageDataPath) {
        this.imageDataPath = imageDataPath;
    }
}

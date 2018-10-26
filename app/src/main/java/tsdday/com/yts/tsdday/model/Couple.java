package tsdday.com.yts.tsdday.model;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import tsdday.com.yts.tsdday.util.DateFormat;

public class Couple extends RealmObject {
    @PrimaryKey
    public long id;

    public String startDate;

    public User oneUser;
    public User twoUser;
    public byte[] background;
    public String backgroundPath;
    public String ment;
    public Boolean isStartOne;
    public String number;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public User getOneUser() {
        return oneUser;
    }

    public void setOneUser(User oneUser) {
        this.oneUser = oneUser;
    }

    public User getTwoUser() {
        return twoUser;
    }

    public void setTwoUser(User twoUser) {
        this.twoUser = twoUser;
    }

    public byte[] getBackground() {
        return background;
    }

    public void setBackground(byte[] background) {
        this.background = background;
    }

    public String getMent() {
        return ment;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }

    public boolean getStartOne() {
        return isStartOne != null ? isStartOne : false;
    }

    public void setStartOne(Boolean startOne) {
        isStartOne = startOne;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }
}

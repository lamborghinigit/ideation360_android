package vadevelopment.ideation360.Skeleton;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vibrantappz on 7/12/2017.
 */

public class Ratings_Skeleton implements Parcelable{


    public Ratings_Skeleton(){}

    protected Ratings_Skeleton(Parcel in) {
        name = in.readString();
        ideatorid = in.readString();
        value = in.readString();
    }

    public static final Creator<Ratings_Skeleton> CREATOR = new Creator<Ratings_Skeleton>() {
        @Override
        public Ratings_Skeleton createFromParcel(Parcel in) {
            return new Ratings_Skeleton(in);
        }

        @Override
        public Ratings_Skeleton[] newArray(int size) {
            return new Ratings_Skeleton[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdeatorid() {
        return ideatorid;
    }

    public void setIdeatorid(String ideatorid) {
        this.ideatorid = ideatorid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String name, ideatorid, value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(ideatorid);
        parcel.writeString(value);
    }
}

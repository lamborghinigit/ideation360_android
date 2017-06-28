package vadevelopment.ideation360.Skeleton;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vibrantappz on 6/20/2017.
 */

public class SavedIdeas_Skeleton implements Parcelable{

    String campaign;

    public String getIdeationsaved_name() {
        return ideationsaved_name;
    }

    public void setIdeationsaved_name(String ideationsaved_name) {
        this.ideationsaved_name = ideationsaved_name;
    }

    String ideationsaved_name;
    String idea_name;
    String idea_description;

    public SavedIdeas_Skeleton(){}

    public SavedIdeas_Skeleton(Parcel in) {
        campaign = in.readString();
        idea_name = in.readString();
        idea_description = in.readString();
        image_path = in.readString();
        audio_path = in.readString();
        ideationsaved_name = in.readString();
    }

    public static final Creator<SavedIdeas_Skeleton> CREATOR = new Creator<SavedIdeas_Skeleton>() {
        @Override
        public SavedIdeas_Skeleton createFromParcel(Parcel in) {
            return new SavedIdeas_Skeleton(in);
        }

        @Override
        public SavedIdeas_Skeleton[] newArray(int size) {
            return new SavedIdeas_Skeleton[size];
        }
    };

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getIdea_name() {
        return idea_name;
    }

    public void setIdea_name(String idea_name) {
        this.idea_name = idea_name;
    }

    public String getIdea_description() {
        return idea_description;
    }

    public void setIdea_description(String idea_description) {
        this.idea_description = idea_description;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }

    String image_path;
    String audio_path;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(campaign);
        parcel.writeString(idea_name);
        parcel.writeString(idea_description);
        parcel.writeString(image_path);
        parcel.writeString(audio_path);
        parcel.writeString(ideationsaved_name);
    }
}

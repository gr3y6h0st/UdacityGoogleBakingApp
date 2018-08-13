package com.nanodegree.android.bakingapp.BakingData;

import java.io.Serializable;

public class RecipeSteps implements Serializable {


    private int step_id;
    private String step_shortDescription;
    private String step_description;
    private String step_videoURL;
    private String step_thumnailURL;


    //No Args constructor, use in serialization
    public RecipeSteps() {
    }

    public RecipeSteps(
            int step_id,
            String step_shortDescription,
            String step_description,
            String step_videoURL,
            String step_thumnailURL) {

        this.step_id = step_id;
        this.step_shortDescription = step_shortDescription;
        this.step_description = step_description;
        this.step_videoURL = step_videoURL;
        this.step_thumnailURL = step_thumnailURL;

    }

    public int getStep_id() {
        return step_id;
    }

    public void setStep_id(int step_id) {
        this.step_id = step_id;
    }

    public String getStep_shortdescription() {
        return step_shortDescription;
    }

    public void setStep_shortdescription(String step_shortdescription) {
        this.step_shortDescription = step_shortdescription;
    }

    public String getStep_description() {
        return step_description;
    }

    public void setStep_description(String step_description) {
        this.step_description = step_description;
    }

    public String getStep_videoURL() {
        return step_videoURL;
    }

    public void setStep_videoURL(String step_videoURL) {
        this.step_videoURL = step_videoURL;
    }

    public String getStep_thumbnailURL() {
        return step_thumnailURL;
    }

    public void setStep_thumbnailURL(String step_thumnailURL) {
        this.step_thumnailURL = step_thumnailURL;
    }
}

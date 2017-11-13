package com.example.nizam.bakingapp;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class Baking {
    private String BakingId;
    private String bakingName;
    private BakingIngredients BakingIngredients;
    private BakingSteps BakingSteps;
    private String servings;
    private String BakingImage;

    public String getBakingId() {
        return BakingId;
    }

    public void setBakingId(String bakingId) {
        BakingId = bakingId;
    }

    public String getBakingName() {
        return bakingName;
    }

    public void setBakingName(String bakingName) {
        this.bakingName = bakingName;
    }

    public com.example.nizam.bakingapp.BakingIngredients getBakingIngredients() {
        return BakingIngredients;
    }

    public void setBakingIngredients(com.example.nizam.bakingapp.BakingIngredients bakingIngredients) {
        BakingIngredients = bakingIngredients;
    }

    public com.example.nizam.bakingapp.BakingSteps getBakingSteps() {
        return BakingSteps;
    }

    public void setBakingSteps(com.example.nizam.bakingapp.BakingSteps bakingSteps) {
        BakingSteps = bakingSteps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getBakingImage() {
        return BakingImage;
    }

    public void setBakingImage(String bakingImage) {
        BakingImage = bakingImage;
    }
}

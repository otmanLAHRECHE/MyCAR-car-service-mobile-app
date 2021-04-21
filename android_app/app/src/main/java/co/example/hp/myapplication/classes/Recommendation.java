package co.example.hp.myapplication.classes;

import java.util.ArrayList;

public class Recommendation {
    private ArrayList<RecommendationItem> recommendationItems;

    public Recommendation() {
    }

    public ArrayList<RecommendationItem> getRecommendationItems() {
        return recommendationItems;
    }

    public void setRecommendationItems(ArrayList<RecommendationItem> recommendationItems) {
        this.recommendationItems = recommendationItems;
    }
}

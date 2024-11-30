package Media.models;

import java.util.Objects;

public abstract class Media {
    private String title;
    private String category;
    private float cost;
    protected int id;
    public Media(String title, String category, float cost) {
        this.title = title;
        this.category = category;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getCost() {
        return cost;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return String.format("%s [title=%s, category=%s, cost=%.2f]", 
            this.getClass().getSimpleName(), title, category, cost);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Media media = (Media) obj;
        return Objects.equals(title, media.title) && 
               Objects.equals(category, media.category);
    }
}

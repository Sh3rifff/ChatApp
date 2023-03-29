package az.sharif.chatapp.models;

public class CategoriesModel {

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public CategoriesModel(int image, String title) {
        this.image = image;
        this.title = title;
    }

    int image;
    String title;
}

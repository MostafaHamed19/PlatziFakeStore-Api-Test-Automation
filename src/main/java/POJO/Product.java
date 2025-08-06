package POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private int id;
    private String title;
    private String slug;
    private int price;
    private String description;
    private int categoryId;
    private Category category;
    private List<String> images;

    public Product() {
    }
    public void partialUpdateProduct(String title, int price) {
        this.title = title;
        this.price = price;
    }
    public void fullyUpdateProduct(String title, int price, String description, int categoryId, List<String> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.images = images;
    }

    public Product(String title, int price, String description, int categoryId, List<String> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
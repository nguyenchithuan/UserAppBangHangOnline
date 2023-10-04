package edu.wkd.userappbanghangonline.model;

public class ProductType {
    private int id;
    private String typeName;
    private String image;

    public ProductType(int id, String typeName, String image) {
        this.id = id;
        this.typeName = typeName;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

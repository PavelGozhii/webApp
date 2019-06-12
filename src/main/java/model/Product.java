package model;

import model.annotation.Column;
import model.annotation.Id;
import model.annotation.Table;

@Table(name = "Product")
public class Product {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private Integer category;

    @Column(name = "price")
    private double price;


    public Product(Integer id, String name, Integer category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Product(String name, Integer category, double price) {
        this.id = null;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

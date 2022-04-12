package com.deemo.hard.entity;

public class Game {

    private Integer id;
    private Double price;
    private String name;
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}

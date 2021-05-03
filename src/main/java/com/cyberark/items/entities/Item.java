package com.cyberark.items.entities;

public class Item {
    private long id;
    private ItemType type;
    private int daysToExpire;
    private int price;

    public Item() {}

    public Item(long id, ItemType type, int daysToExpire, int price) {
        this.id = id;
        this.setType(type);
        this.setDaysToExpire(daysToExpire);
        this.setPrice(price);
    }

    public Item(ItemType type, int daysToExpire, int price) {
        this.id = -1;
        this.setType(type);
        this.setDaysToExpire(daysToExpire);
        this.setPrice(price);
    }

    /* Generated getter and setter code */

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(int daysToExpire) {
        this.daysToExpire = daysToExpire;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", daysToExpire=" + daysToExpire +
                ", price=" + price +
                '}';
    }
}

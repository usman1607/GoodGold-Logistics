package com.goodgold.logistics.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @NotNull
    @ManyToOne
    private Category category;

    @NotNull
    @ManyToOne
    private User user;

    @ManyToOne
    private Shipment shipment;

    private int quantity;
    private String description;
    private String status;
    private String ship;

    public Product(){

    }

    public Product(String name, Category category, User user, Shipment shipment, int quantity, String description, String status, String ship) {
        this.name = name;
        this.category = category;
        this.user = user;
        this.shipment = shipment;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.ship = ship;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }
}

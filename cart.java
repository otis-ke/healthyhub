package com.example;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(String medicine, int quantity) {
        items.add(new CartItem(medicine, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }
}

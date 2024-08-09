package com.demo.caching.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductResponse implements Serializable {

    private int uuid;
    private String name;
    private String description;
    private double amount;
}

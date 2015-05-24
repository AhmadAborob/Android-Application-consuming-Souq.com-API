package com.ahnaser.myfirstapp;

/**
 * Created by root on 23/05/15.
 */
public class Product {
    private String id;
    private String label;
    private String msrp;
    private String offerPrice;
    private String link;
    private String imageLink;

    public Product(String id, String label, String msrp, String offerPrice, String link, String imageLink) {
        this.id = id;
        this.label = label;
        this.msrp = msrp;
        this.offerPrice = offerPrice;
        this.link = link;
        this.imageLink = imageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String toString(){
        return "id: "+id+" Label: "+label+" Image: "+imageLink;
    }
}

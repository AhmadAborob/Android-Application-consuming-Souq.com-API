package com.ahnaser.myfirstapp.extras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by root on 26/05/15.
 */
public class ProductSorter {
    public void sortProductByName(ArrayList<com.ahnaser.souqapi.pojos.Product> products){
        Collections.sort(products, new Comparator<com.ahnaser.souqapi.pojos.Product>() {
            @Override
            public int compare(com.ahnaser.souqapi.pojos.Product lhs, com.ahnaser.souqapi.pojos.Product rhs) {
                return lhs.getLabel().compareTo(rhs.getLabel());
            }
        });

    }

    public void sortProductByPrice(ArrayList<com.ahnaser.souqapi.pojos.Product> products){
        Collections.sort(products, new Comparator<com.ahnaser.souqapi.pojos.Product>() {
            @Override
            public int compare(com.ahnaser.souqapi.pojos.Product lhs, com.ahnaser.souqapi.pojos.Product rhs) {
                return lhs.getOffer_price().compareTo(rhs.getOffer_price());
            }
        });

    }
}

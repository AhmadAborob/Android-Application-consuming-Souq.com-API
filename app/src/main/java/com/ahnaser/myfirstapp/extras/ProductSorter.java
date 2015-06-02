package com.ahnaser.myfirstapp.extras;

import com.ahnaser.myfirstapp.pojo.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by root on 26/05/15.
 */
public class ProductSorter {
    public void sortProductByName(ArrayList<Product> products){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getLabel().compareTo(rhs.getLabel());
            }
        });

    }

    public void sortProductByPrice(ArrayList<Product> products){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getOfferPrice().compareTo(rhs.getOfferPrice());
            }
        });

    }
}

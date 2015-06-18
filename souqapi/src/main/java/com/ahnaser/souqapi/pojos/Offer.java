package com.ahnaser.souqapi.pojos;

import com.ahnaser.souqapi.SouqAPIResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Offer {

    private static final String KEY_ID = "id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRICE = "price";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_IN_STOCK = "in_stock";
    private static final String KEY_IMAGES = "images";
    private static final String KEY_SELLER = "seller";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_FREE_SHIPPING = "free_shipping";
    private static final String KEY_NOTE = "note";
    private static final String KEY_HANDLING_TIME = "handling_time";
    private static final String KEY_FULFILLED_BY_SOUQ = "fulfilled_by_souq";
    private static final String KEY_HAS_INTERNATIONAL_SHIPPING = "has_international_shipping";
    private static final String KEY_LINK = "link";

    private String id;
    private String product_id;
    private String price;
    private String currency;
    private String condition;
    private String in_stock;
    private JSONObject images;
    private JSONObject seller;
    private String location;
    private String free_shipping;
    private String note;
    private String handling_time;
    private String fulfilled_by_souq;
    private String has_international_shipping;
    private String link;

    public Offer(SouqAPIResult apiResult) throws JSONException {

        if (apiResult.getData() != null || apiResult.getData().length() > 0) {

            JSONObject data = apiResult.getData();

            if (data.has(KEY_ID) && !data.isNull(KEY_ID)) {
                id = data.getString(KEY_ID);
            }
            if (data.has(KEY_PRODUCT_ID) && !data.isNull(KEY_PRODUCT_ID)) {
                product_id = data.getString(KEY_PRODUCT_ID);
            }
            if (data.has(KEY_PRICE) && !data.isNull(KEY_PRICE)) {
                price = data.getString(KEY_PRICE);
            }
            if (data.has(KEY_CURRENCY) && !data.isNull(KEY_CURRENCY)) {
                currency = data.getString(KEY_CURRENCY);
            }
            if (data.has(KEY_CONDITION) && !data.isNull(KEY_CONDITION)) {
                condition = data.getString(KEY_CONDITION);
            }
            if (data.has(KEY_IN_STOCK) && !data.isNull(KEY_IN_STOCK)) {
                in_stock = data.getString(KEY_IN_STOCK);
            }
            if (data.has(KEY_IMAGES) && !data.isNull(KEY_IMAGES)) {
                images = data.getJSONObject(KEY_IMAGES);
            }
            if (data.has(KEY_SELLER) && !data.isNull(KEY_SELLER)) {
                seller = data.getJSONObject(KEY_SELLER);
            }
            if (data.has(KEY_LOCATION) && !data.isNull(KEY_LOCATION)) {
                location = data.getString(KEY_LOCATION);
            }
            if (data.has(KEY_FREE_SHIPPING) && !data.isNull(KEY_FREE_SHIPPING)) {
                free_shipping = data.getString(KEY_FREE_SHIPPING);
            }
            if (data.has(KEY_NOTE) && !data.isNull(KEY_NOTE)) {
                note = data.getString(KEY_NOTE);
            }
            if (data.has(KEY_HANDLING_TIME) && !data.isNull(KEY_HANDLING_TIME)) {
                handling_time = data.getString(KEY_HANDLING_TIME);
            }
            if (data.has(KEY_FULFILLED_BY_SOUQ) && !data.isNull(KEY_FULFILLED_BY_SOUQ)) {
                fulfilled_by_souq = data.getString(KEY_FULFILLED_BY_SOUQ);
            }
            if (data.has(KEY_HAS_INTERNATIONAL_SHIPPING) && !data.isNull(KEY_HAS_INTERNATIONAL_SHIPPING)) {
                has_international_shipping = data.getString(KEY_HAS_INTERNATIONAL_SHIPPING);
            }
            if (data.has(KEY_LINK) && !data.isNull(KEY_LINK)) {
                link = data.getString(KEY_LINK);
            }
        }

    }

    public Offer(String id, String product_id) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }

    public JSONObject getImages() {
        return images;
    }

    public void setImages(JSONObject images) {
        this.images = images;
    }

    public JSONObject getSeller() {
        return seller;
    }

    public void setSeller(JSONObject seller) {
        this.seller = seller;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFree_shipping() {
        return free_shipping;
    }

    public void setFree_shipping(String free_shipping) {
        this.free_shipping = free_shipping;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHandling_time() {
        return handling_time;
    }

    public void setHandling_time(String handling_time) {
        this.handling_time = handling_time;
    }

    public String getFulfilled_by_souq() {
        return fulfilled_by_souq;
    }

    public void setFulfilled_by_souq(String fulfilled_by_souq) {
        this.fulfilled_by_souq = fulfilled_by_souq;
    }

    public String getHas_international_shipping() {
        return has_international_shipping;
    }

    public void setHas_international_shipping(String has_international_shipping) {
        this.has_international_shipping = has_international_shipping;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

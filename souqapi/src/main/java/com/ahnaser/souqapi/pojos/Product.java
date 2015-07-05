package com.ahnaser.souqapi.pojos;

import com.ahnaser.souqapi.SouqAPIResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    private static final String KEY_ID = "id";
    private static final String KEY_LABEL = "label";
    private static final String KEY_PRODUCT_TYPE_LABEL_PLURAL = "product_type_label_plural";
    private static final String KEY_PRODUCT_TYPE_ID = "product_type_id";
    private static final String KEY_LINK = "link";
    private static final String KEY_IMAGES = "images";
    private static final String KEY_SMALL = "S";
    private static final String KEY_LARGE="L";
    private static final String KEY_MEDIUM ="M";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_MSRP = "msrp";
    private static final String KEY_OFFER_PRICE = "offer_price";
    private static final String KEY_OFFER_ID = "offer_id";
    private static final String KEY_RATING_COUNT = "rating_count";
    private static final String KEY_EAN = "ean";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_ATTRIBUTES = "attributes";
    private static final String KEY_ATTRIBUTES_GROUPS = "attributes_groups";
    private static final String KEY_DESCRIPTION_VALUE="value";
    private static final String KEY_VARIATIONS = "variations";

    private String id;
    private String label;
    private String product_type_label_plural;
    private String product_type_id;
    private String link;
    private JSONObject images;
    private String image_large_link;
    private String image_medium_link;
    private String image_small_link;
    private String currency;
    private String msrp;
    private String offer_price;
    private String offer_id;
    private String rating_count;
    private String description;
    private JSONArray ean;
    private JSONArray video;
    private JSONArray attributes;
    private JSONArray attributes_groups;
    private JSONArray variations;

    public Product(SouqAPIResult apiResult) throws JSONException {

        if (apiResult.getData() != null || apiResult.getData().length() > 0) {

            JSONObject data = apiResult.getData();

            if (data.has(KEY_ID) && !data.isNull(KEY_ID)) {
                id = data.getString(KEY_ID);
            }
            if (data.has(KEY_LABEL) && !data.isNull(KEY_LABEL)) {
                label = data.getString(KEY_LABEL);
            }
            if (data.has(KEY_PRODUCT_TYPE_LABEL_PLURAL) && !data.isNull(KEY_PRODUCT_TYPE_LABEL_PLURAL)) {
                product_type_label_plural = data.getString(KEY_PRODUCT_TYPE_LABEL_PLURAL);
            }
            if (data.has(KEY_PRODUCT_TYPE_ID) && !data.isNull(KEY_PRODUCT_TYPE_ID)) {
                product_type_id = data.getString(KEY_PRODUCT_TYPE_ID);
            }
            if (data.has(KEY_LINK) && !data.isNull(KEY_LINK)) {
                link = data.getString(KEY_LINK);
            }
            if (data.has(KEY_IMAGES) && !data.isNull(KEY_IMAGES)) {
                images = data.getJSONObject(KEY_IMAGES);
                if (images.has(KEY_LARGE) && !images.isNull(KEY_LARGE)) {
                        image_large_link = images.getJSONArray(KEY_LARGE).getString(0);
                    }
                    else if (images.has(KEY_MEDIUM) && !images.isNull(KEY_MEDIUM)) {
                        image_medium_link = images.getJSONArray(KEY_MEDIUM).getString(0);
                    }
                    else if (images.has(KEY_SMALL) && !images.isNull(KEY_SMALL)) {
                        image_small_link = images.getJSONArray(KEY_SMALL).getString(0);
                    }


            }
            if (data.has(KEY_CURRENCY) && !data.isNull(KEY_CURRENCY)) {
                currency = data.getString(KEY_CURRENCY);
            }
            if (data.has(KEY_MSRP) && !data.isNull(KEY_MSRP)) {
                msrp = data.getString(KEY_MSRP);
            }
            if (data.has(KEY_OFFER_PRICE) && data.isNull(KEY_OFFER_PRICE)) {
                offer_price = data.getString(KEY_OFFER_PRICE);
            }
            if (data.has(KEY_OFFER_ID) && !data.isNull(KEY_OFFER_ID)) {
                offer_id = data.getString(KEY_OFFER_ID);
            }
            if (data.has(KEY_RATING_COUNT) && !data.isNull(KEY_RATING_COUNT)) {
                rating_count = data.getString(KEY_RATING_COUNT);
            }
            if (data.has(KEY_EAN) && !data.isNull(KEY_EAN)) {
                ean = data.getJSONArray(KEY_EAN);
            }
            if (data.has(KEY_VIDEO) && !data.isNull(KEY_VIDEO)) {
                video = data.getJSONArray(KEY_VIDEO);
            }
            if (data.has(KEY_ATTRIBUTES) && !data.isNull(KEY_ATTRIBUTES)) {
                attributes = data.getJSONArray(KEY_ATTRIBUTES);
            }
            if (data.has(KEY_ATTRIBUTES_GROUPS) && !data.isNull(KEY_ATTRIBUTES_GROUPS)) {
                attributes_groups = data.getJSONArray(KEY_ATTRIBUTES_GROUPS);

                if (attributes_groups.getJSONObject(0).has(KEY_ATTRIBUTES) && !attributes_groups.getJSONObject(0).isNull(KEY_ATTRIBUTES)) {
                    JSONArray attrs = attributes_groups.getJSONObject(0).getJSONArray(KEY_ATTRIBUTES);
                    if (attrs.getJSONObject(1).has(KEY_DESCRIPTION_VALUE) && !attrs.getJSONObject(1).isNull(KEY_DESCRIPTION_VALUE)) {
                        description = attrs.getJSONObject(1).getString(KEY_DESCRIPTION_VALUE);
                    }
                }
            }
            if (data.has(KEY_VARIATIONS) && !data.isNull(KEY_VARIATIONS)) {
                variations = data.getJSONArray(KEY_VARIATIONS);
            }
        }

    }

    public Product(String id, String label) {
        this.id = id;
        this.label = label;
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

    public String getProduct_type_label_plural() {
        return product_type_label_plural;
    }

    public void setProduct_type_label_plural(String product_type_label_plural) {
        this.product_type_label_plural = product_type_label_plural;
    }

    public String getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public JSONObject getImages() {
        return images;
    }

    public void setImages(JSONObject images) {
        this.images = images;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getRating_count() {
        return rating_count;
    }

    public void setRating_count(String rating_count) {
        this.rating_count = rating_count;
    }

    public JSONArray getEan() {
        return ean;
    }

    public void setEan(JSONArray ean) {
        this.ean = ean;
    }

    public JSONArray getVideo() {
        return video;
    }

    public void setVideo(JSONArray video) {
        this.video = video;
    }

    public JSONArray getAttributes() {
        return attributes;
    }

    public void setAttributes(JSONArray attributes) {
        this.attributes = attributes;
    }

    public JSONArray getAttributes_groups() {
        return attributes_groups;
    }

    public void setAttributes_groups(JSONArray attributes_groups) {
        this.attributes_groups = attributes_groups;
    }

    public JSONArray getVariations() {
        return variations;
    }

    public void setVariations(JSONArray variations) {
        this.variations = variations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_large_link() {
        return image_large_link;
    }

    public void setImage_large_link(String image_large_link) {
        this.image_large_link = image_large_link;
    }

    public String getImage_medium_link() {
        return image_medium_link;
    }

    public void setImage_medium_link(String image_medium_link) {
        this.image_medium_link = image_medium_link;
    }

    public String getImage_small_link() {
        return image_small_link;
    }

    public void setImage_small_link(String image_small_link) {
        this.image_small_link = image_small_link;
    }

}

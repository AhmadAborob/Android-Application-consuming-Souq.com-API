package com.ahnaser.souqapi.pojos;

import com.ahnaser.souqapi.SouqAPIResult;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerProfile {

    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_RESIDENCE_COUNTRY_CODE = "residence_country_code";

    private String customer_id;
    private String firstname;
    private String lastname;
    private String username;
    private String country_code;
    private String residence_country_code;

    public CustomerProfile(SouqAPIResult apiResult) throws JSONException {

        if (apiResult.getData() != null || apiResult.getData().length() > 0) {

            JSONObject data = apiResult.getData();

            if (data.has(KEY_CUSTOMER_ID) && !data.isNull(KEY_CUSTOMER_ID)) {
                customer_id = data.getString(KEY_CUSTOMER_ID);
            }
            if (data.has(KEY_FIRSTNAME) && !data.isNull(KEY_FIRSTNAME)) {
                firstname = data.getString(KEY_FIRSTNAME);
            }
            if (data.has(KEY_LASTNAME) && !data.isNull(KEY_LASTNAME)) {
                lastname = data.getString(KEY_LASTNAME);
            }
            if (data.has(KEY_USERNAME) && !data.isNull(KEY_USERNAME)) {
                username = data.getString(username);
            }
            if (data.has(KEY_COUNTRY_CODE) && !data.isNull(KEY_COUNTRY_CODE)) {
                country_code = data.getString(KEY_COUNTRY_CODE);
            }
            if (data.has(KEY_RESIDENCE_COUNTRY_CODE) && !data.isNull(KEY_RESIDENCE_COUNTRY_CODE)) {
                residence_country_code = data.getString(KEY_RESIDENCE_COUNTRY_CODE);
            }
        }

    }

    public CustomerProfile(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getResidence_country_code() {
        return residence_country_code;
    }

    public void setResidence_country_code(String residence_country_code) {
        this.residence_country_code = residence_country_code;
    }
}

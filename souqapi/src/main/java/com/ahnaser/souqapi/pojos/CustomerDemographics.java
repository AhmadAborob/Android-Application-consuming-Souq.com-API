package com.ahnaser.souqapi.pojos;

import com.ahnaser.souqapi.SouqAPIResult;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerDemographics {

    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DATE_OF_BIRTH = "date_of_birth";

    private String customer_id;
    private String gender;
    private String date_of_birth;

    public CustomerDemographics(SouqAPIResult apiResult) throws JSONException {

        if (apiResult.getData() != null || apiResult.getData().length() > 0) {

            JSONObject data = apiResult.getData();

            if (data.has(KEY_CUSTOMER_ID) && !data.isNull(KEY_CUSTOMER_ID)) {
                customer_id = data.getString(KEY_CUSTOMER_ID);
            }
            if (data.has(KEY_GENDER) && !data.isNull(KEY_GENDER)) {
                gender = data.getString(KEY_GENDER);
            }
            if (data.has(KEY_DATE_OF_BIRTH) && !data.isNull(KEY_DATE_OF_BIRTH)) {
                date_of_birth = data.getString(KEY_DATE_OF_BIRTH);
            }

        }
    }

    public CustomerDemographics(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}

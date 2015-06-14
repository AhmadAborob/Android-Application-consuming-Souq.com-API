package com.ahnaser.souqapi;

import org.json.JSONException;
import org.json.JSONObject;

public class SouqAPIResult{

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_META="meta";
    private static final String KEY_DATA="data";

    private int status;
    private String message;
    private JSONObject data;
    private JSONObject callResult;

    public SouqAPIResult(int responseCode, JSONObject callResult) {
        this.status=responseCode;
        this.callResult = callResult;
        this.data=new JSONObject();

        try {
            JSONObject meta=callResult.getJSONObject(KEY_META);
            this.message=meta.getString(KEY_MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.data=callResult.getJSONObject(KEY_DATA);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                this.data.put(KEY_DATA,callResult.getJSONArray(KEY_DATA));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


    }

    public boolean isResponseOK(){
        if(status==200 || status==201 || status==202){
            return true;
        }
        return false;
    }

    public boolean isAuthenticationFailed(){
        // Forbidden Access
        if (status==403){
            return true;
        }
        return false;
    }

    public boolean isExpiredTokenResponse(){
        if (status==401){
            return true;
        }
        return false;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getData() {
        return data;
    }

    public JSONObject getCallResult() {
        return callResult;
    }

}

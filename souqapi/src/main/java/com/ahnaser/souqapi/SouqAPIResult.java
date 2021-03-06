package com.ahnaser.souqapi;

/**
 * @copyright  Copyright (c) 2015 Souq.com. All rights reserved.
 * @author     Ahmad Naser (aaborob@souq.com), created on 1/06/15
 * @package    SouqAPI
 * @version    1.0
 */

import org.json.JSONException;
import org.json.JSONObject;

public class SouqAPIResult {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_META = "meta";
    private static final String KEY_DATA = "data";

    private int status;
    private String message;
    private JSONObject data;
    private JSONObject callResult;

    public SouqAPIResult(int responseCode, JSONObject callResult) {
        this.status = responseCode;
        this.callResult = callResult;
        this.data = new JSONObject();
        if(callResult!=null) {

            if(callResult.has(KEY_META) && !callResult.isNull(KEY_META)) {
                try {
                    JSONObject meta = callResult.getJSONObject(KEY_META);
                    if(meta.has(KEY_MESSAGE) && !meta.isNull(KEY_MESSAGE)) {
                        this.message = meta.getString(KEY_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(callResult.has(KEY_DATA) && !callResult.isNull(KEY_DATA)) {
                try {
                    this.data = callResult.getJSONObject(KEY_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        this.data.put(KEY_DATA, callResult.getJSONArray(KEY_DATA));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }

    public boolean isResponseOK() {
        if (status == 200 || status == 201 || status == 202) {
            return true;
        }
        return false;
    }

    public boolean isAuthenticationFailed() {
        // Forbidden Access
        if (status == 403) {
            return true;
        }
        return false;
    }

    public boolean isExpiredTokenResponse() {
        if (status == 401) {
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

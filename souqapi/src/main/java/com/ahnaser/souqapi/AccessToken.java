package com.ahnaser.souqapi;

/**
 * @copyright  Copyright (c) 2015 Souq.com. All rights reserved.
 * @author     Ahmad Naser (aaborob@souq.com), created on 1/06/15
 * @package    SouqAPI
 * @version    1.0
 */

public class AccessToken {
    private String value;
    private String customerId;
    private String type;
    private int expires_in;
    private String authorizedScopes;

    public AccessToken(String value, String customerId) {
        this.value = value;
        this.customerId = customerId;
        this.authorizedScopes = "";
    }

    public AccessToken(String value, String customerId, String authorizedScopes) {
        this.value = value;
        this.customerId = customerId;
        this.authorizedScopes = authorizedScopes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        if (type.isEmpty()) {
            type = "Bearer";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAuthorizedScopes() {
        return authorizedScopes;
    }

    public void setAuthorizedScopes(String authorizedScopes) {
        this.authorizedScopes = authorizedScopes;
    }
}



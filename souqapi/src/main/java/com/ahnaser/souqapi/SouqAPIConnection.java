package com.ahnaser.souqapi;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SouqAPIConnection {

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_APP_ID = "app_id";
    private static final String KEY_APP_SECRET = "app_secret";
    private static final String KEY_CLIENT_ID="client_id";
    private static final String KEY_CLIENT_SECRET="client_secret";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CUSTOMER_ID = "customer_id";

    private String apiBaseUrl="https://api.souq.com";
    private String apiUrl="https://api.souq.com/v1/";
    private String authorizeUrl="/oauth/authorize";
    private String accessTokenUrl="/oauth/access_token";
    private String defaultCountry="ae";
    private String defaultLanguage="ar";
    private String clientId;
    private String clientSecret;
    private AccessToken accessToken;
    private RequestQueue requestQueue;
    private SouqAPIResult apiResult;
    private int status;
    private Context context;

    public interface ResponseObserver
    {
        void onError(VolleyError error);
        void onSuccess(JSONObject response);
    }

    private ResponseObserver mObserver;

    public void setResponseObserver(ResponseObserver observer) {
        mObserver = observer;
    }

    public SouqAPIConnection(String clientId,String clientSecret, Context context){
        requestQueue=Volley.newRequestQueue(context);
        this.clientId=clientId;
        this.clientSecret=clientSecret;
        this.context=context;
    }

    private void apiCall(int method, String uri, final Map<String,String> params){

        JsonObjectRequest request=new JsonObjectRequest(method, generateUrl(uri,params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                apiResult=new SouqAPIResult(status,response);
                mObserver.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiResult=new SouqAPIResult(error.networkResponse.statusCode,new JSONObject());
                mObserver.onError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(accessToken!=null){
                    Map<String, String> headers = new HashMap<String,String>();
                    params.put(KEY_ACCESS_TOKEN,accessToken.getValue());
                    params.put(KEY_CUSTOMER_ID,accessToken.getCustomerId());
                    return params;
                }

                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(accessToken!=null) {
                    Map<String, String> headers = new HashMap<String,String>();
                    String auth = "Bearer " + accessToken.getValue();
                    headers.put("Authorization", auth);
                    return headers;
                }
                return super.getHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                status=response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(request);
    }

    private String generateUrl(String uri,Map<String,String> paramss){
        Map<String,String> params=new HashMap<String, String>();
        StringBuilder url = new StringBuilder();

        if (uri!=accessTokenUrl) {

            params.put(KEY_APP_ID, clientId);
            params.put(KEY_APP_SECRET, clientSecret);
            params.put(KEY_COUNTRY, defaultCountry);
            params.put(KEY_LANGUAGE, defaultLanguage);

            if (paramss != null) {
                if (!paramss.isEmpty()) {
                    params.putAll(paramss);
                }
            }

            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            if (iterator.hasNext()) {
                url.append('?');
            }
            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                url.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    url.append('&');
                }
            }
        }
        else {

            params.put(KEY_CLIENT_ID, clientId);
            params.put(KEY_CLIENT_SECRET, clientSecret);

            if (paramss != null) {
                if (!paramss.isEmpty()) {
                    params.putAll(paramss);
                }
            }

            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            if (iterator.hasNext()) {
                url.append('?');
            }
            while (iterator.hasNext()) {
                Map.Entry<String, String> param = iterator.next();
                url.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    url.append('&');
                }
            }
        }
        if (uri==accessTokenUrl) {
            return apiBaseUrl + uri + url.toString();
        }
        else{
            return apiUrl+uri+url.toString();
        }
    }

    public void get(String uri){
        apiCall(Request.Method.GET, uri, null);
    }

    public void post(String uri){
        apiCall(Request.Method.POST, uri, null);
    }

    public void put(String uri){
        apiCall(Request.Method.PUT,uri,null);
    }

    public void delete(String uri){
        apiCall(Request.Method.DELETE, uri, null);
    }

    public void get(String uri, Map<String,String> params){
        apiCall(Request.Method.GET, uri, params);
    }

    public void post(String uri, Map<String,String> params){
        apiCall(Request.Method.POST, uri, params);
    }

    public void put(String uri, Map<String,String> params){
        apiCall(Request.Method.PUT, uri, params);
    }

    public void delete(String uri, Map<String,String> params){
        apiCall(Request.Method.DELETE, uri, params);
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(String defaultCountry) {
        this.defaultCountry = defaultCountry;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getAuthenticationUrl(String redirectUrl, String scopes){
        Map<String,String> params=new HashMap<String,String>();
        params.put("redirect_uri",redirectUrl);
        params.put("client_id",clientId);
        params.put("response_type","code");
        params.put("scope",scopes);
        params.put("redirect_uri",redirectUrl);

        return apiBaseUrl+authorizeUrl+"?"+HttpBuildQuery(params);
    }

    public String getAuthenticationUrl(String redirectUrl, String scopes, String state){
        Map<String,String> params=new HashMap<String,String>();
        params.put("redirect_uri",redirectUrl);
        params.put("client_id",clientId);
        params.put("response_type","code");
        params.put("scope",scopes);
        params.put("state",state);

        return apiBaseUrl+authorizeUrl+"?"+HttpBuildQuery(params);
    }

private String HttpBuildQuery(Map<String,String> params) {
    Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
    StringBuilder url = new StringBuilder();
    while (iterator.hasNext()) {
        Map.Entry<String, String> param = iterator.next();
        url.append(param.getKey()).append('=')
                .append(param.getValue());
        if (iterator.hasNext()) {
            url.append('&');
        }
    }
   return url.toString();
}

    public void setAccessTokenFromServer(String code, String redirectUrl, final String scopes){
        Map<String,String> params=new HashMap<String,String>();
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", redirectUrl);
        /*try {
            params.put("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        this.setResponseObserver(new ResponseObserver() {
            @Override
            public void onError(VolleyError error) {
                Log.v("STATUS CODE: ", Integer.toString(apiResult.getStatus()));
            }

            @Override
            public void onSuccess(JSONObject response) {
                AccessToken accessToken;
                String value,customerId;
                try {
                    value=apiResult.getData().getString("access_token");
                    customerId=apiResult.getData().getString("customer_id");
                    Log.v("CUSTOMER ID: ", customerId);
                    accessToken=new AccessToken(value,customerId,scopes);
                    setAccessToken(accessToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.post(this.accessTokenUrl, params);
    }
}

package com.ahnaser.myfirstapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.extras.L;
import com.ahnaser.souqapi.AccessToken;
import com.ahnaser.souqapi.SouqAPIConnection;
import com.ahnaser.souqapi.SouqAPIResult;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OauthExample extends ActionBarActivity {

    private WebView webView;
    private ProgressDialog pd;
    private SouqAPIConnection connection;
    private StringRequest stringRequest;

    private AccessToken accessToken;
    private RequestQueue requestQueue;
    private SouqAPIResult apiResult;
    private int status;
    private String authorizationToken;

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
    private String scopes="customer_profile,cart_management,customer_demographics,customer_profile,cart_management,customer_demographics";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_example);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=sharedPreferences.edit();

        requestQueue= Volley.newRequestQueue(this);
        test();


    }

    void test(){
        connection=new SouqAPIConnection("38607576","EB008DQ5bnzmSZty8fyp",this);

        webView=(WebView) findViewById(R.id.webView);
        webView.requestFocus(View.FOCUS_DOWN);
        pd = ProgressDialog.show(this, "", "Loading..",true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (url.startsWith("https://api.souq.com/oauth/authorize/")) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(url);

                    authorizationToken = uri.getQueryParameter("code");
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    connection.setResponseObserver(new SouqAPIConnection.ResponseObserver() {
                        @Override
                        public void onError(VolleyError error) {
                            Log.v("STATUS CODE: ",Integer.toString(error.networkResponse.statusCode));
                        }

                        @Override
                        public void onSuccess(JSONObject response, int statusCode) {
                            L.t(getApplicationContext(), "VALUE: " + connection.getAccessToken().getValue());
                            L.t(getApplicationContext(), "CUSTOMER ID: " + connection.getAccessToken().getCustomerId());
                            editor.putString("customer_id",connection.getAccessToken().getCustomerId());
                            editor.putString("value", connection.getAccessToken().getValue());
                            editor.apply();
                            OauthExample.this.finish();
                        }
                    });

                    connection.setAccessTokenFromServer(authorizationToken, "https://api.souq.com/oauth/authorize/", scopes);

                    /*stringRequest = new StringRequest(Request.Method.POST, generateUrl(accessTokenUrl, null),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("STATUS CODE: ", Integer.toString(status));

                                    try {
                                        JSONObject result = new JSONObject(response);
                                        apiResult = new SouqAPIResult(status, result);
                                        try {
                                            AccessToken access_Token;
                                            String value, customerId;
                                            value = apiResult.getData().getString("access_token");
                                            customerId = apiResult.getData().getString("customer_id");
                                            access_Token = new AccessToken(value, customerId, scopes);
                                            connection.setAccessToken(access_Token);

                                            L.t(getApplicationContext(), "VALUE: " + connection.getAccessToken().getValue());
                                            L.t(getApplicationContext(), "CUSTOMER ID: " + connection.getAccessToken().getCustomerId());
                                            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor=sharedPreferences.edit();
                                            editor.putString("customer_id",connection.getAccessToken().getCustomerId());
                                            editor.putString("value",connection.getAccessToken().getValue());
                                            editor.apply();

                                            OauthExample.this.finish();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v("STATUS CODE: ", Integer.toString(error.networkResponse.statusCode));

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("code", authorizationToken);
                            params.put(KEY_CLIENT_ID, connection.getClientId());
                            params.put(KEY_CLIENT_SECRET, connection.getClientSecret());
                            params.put("grant_type", "authorization_code");
                            return params;
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            status = response.statusCode;
                            return super.parseNetworkResponse(response);
                        }
                    };

                    requestQueue.add(stringRequest);*/

                } else {
                    Log.i("Authorize", "Redirecting to: " + url);
                    webView.loadUrl(url);
                }
                return true;
            }
        });

        String authUrl = connection.getAuthenticationUrl("https://api.souq.com/oauth/authorize/",scopes);
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_oauth_example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String generateUrl(String uri,Map<String,String> paramss){
        Map<String,String> params=new HashMap<String, String>();
        StringBuilder url = new StringBuilder();

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

            Log.v("ACCESS TOKEN: ", apiBaseUrl + uri + url.toString());
            return apiBaseUrl + uri + url.toString();
    }
}

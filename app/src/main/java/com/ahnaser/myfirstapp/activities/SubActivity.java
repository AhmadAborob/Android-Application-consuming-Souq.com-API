package com.ahnaser.myfirstapp.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahnaser.myfirstapp.MyApplication;
import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.extras.Constants;
import com.ahnaser.myfirstapp.extras.Keys;
import com.ahnaser.myfirstapp.network.VolleySingleton;
import com.ahnaser.myfirstapp.pojo.Product;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SubActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private TextView textVolleyError;
    private ImageView productImage;
    private String productID;
    private static String API_SOUQ_PRODUCTS1="https://api.souq.com/v1/products/";
    private static String API_SOUQ_PRODUCTS2="?country=ae&language=en&show_offers=0&show_attributes=1&show_variations=1&format=json";
    public Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();
        imageLoader=volleySingleton.getImageLoader();
        textVolleyError=(TextView) findViewById(R.id.textVolleyError);
        productImage=(ImageView) findViewById(R.id.productImage);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        productID = extras.getString("productID");

        sendJSONRequest();

    }

    private void sendJSONRequest(){

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, getRequestUrl(),(String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textVolleyError.setVisibility(View.GONE);
                product=parseJSONRequest(response);
                setUpPage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textVolleyError.setVisibility(View.VISIBLE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    textVolleyError.setText(R.string.error_timeout);

                } else if (error instanceof AuthFailureError) {
                    textVolleyError.setText(R.string.error_auth_failure);
                    //TODO
                } else if (error instanceof ServerError) {
                    textVolleyError.setText(R.string.error_auth_failure);
                    //TODO
                } else if (error instanceof NetworkError) {
                    textVolleyError.setText(R.string.error_network);
                    //TODO
                } else if (error instanceof ParseError) {
                    textVolleyError.setText(R.string.error_parser);
                    //TODO
                }

            }
        });
        requestQueue.add(request);

    }

    private Product parseJSONRequest(JSONObject response){

        Product product=null;

        if (response!=null || response.length()>0) {
            try {
                JSONObject currentProduct = response.getJSONObject(Keys.EndpointProducts.KEY_DATA);
                String id = "-1";
                String label = Constants.NA;
                String msrp = Constants.NA;
                String offerPrice = Constants.NA;
                String link = Constants.NA;
                String image = Constants.NA;
                String description=Constants.NA;

                if (currentProduct.has(Keys.EndpointProducts.KEY_ID) && !currentProduct.isNull(Keys.EndpointProducts.KEY_ID)) {
                    id = currentProduct.getString(Keys.EndpointProducts.KEY_ID);
                }

                if (currentProduct.has(Keys.EndpointProducts.KEY_LABEL) && !currentProduct.isNull(Keys.EndpointProducts.KEY_LABEL)) {
                    label = currentProduct.getString(Keys.EndpointProducts.KEY_LABEL);
                }

                if (currentProduct.has(Keys.EndpointProducts.KEY_MARKET_PRICE) && !currentProduct.isNull(Keys.EndpointProducts.KEY_MARKET_PRICE)) {
                    msrp = "Price: " + currentProduct.getString(Keys.EndpointProducts.KEY_MARKET_PRICE) + " AED";
                }

                if (currentProduct.has(Keys.EndpointProducts.KEY_OFFER_PRICE) && !currentProduct.isNull(Keys.EndpointProducts.KEY_OFFER_PRICE))
                    offerPrice = "Price: " + currentProduct.getString(Keys.EndpointProducts.KEY_OFFER_PRICE) + " AED";
                else
                    offerPrice = msrp;

                if (currentProduct.has(Keys.EndpointProducts.KEY_IMAGES) && !currentProduct.isNull(Keys.EndpointProducts.KEY_IMAGES)) {
                    JSONObject images = currentProduct.getJSONObject(Keys.EndpointProducts.KEY_IMAGES);

                    if (images.has(Keys.EndpointProducts.KEY_SMALL) && !images.isNull(Keys.EndpointProducts.KEY_SMALL)) {
                        image = images.getJSONArray(Keys.EndpointProducts.KEY_SMALL).getString(0);
                    }
                }

                if (currentProduct.has(Keys.EndpointProducts.KEY_LINK) && !currentProduct.isNull(Keys.EndpointProducts.KEY_LINK)) {
                    link = currentProduct.getString(Keys.EndpointProducts.KEY_LINK);
                }

                if(currentProduct.has(Keys.EndpointProducts.KEY_ATTRIBUTES_GROUPS) && !currentProduct.isNull(Keys.EndpointProducts.KEY_ATTRIBUTES_GROUPS)) {
                    JSONArray attrsGroub = currentProduct.getJSONArray(Keys.EndpointProducts.KEY_ATTRIBUTES_GROUPS);
                    if (attrsGroub.getJSONObject(0).has(Keys.EndpointProducts.KEY_ATTRIBUTES) && !attrsGroub.getJSONObject(0).isNull(Keys.EndpointProducts.KEY_ATTRIBUTES)){
                        JSONArray attrs= attrsGroub.getJSONObject(0).getJSONArray(Keys.EndpointProducts.KEY_ATTRIBUTES);
                        if(attrs.getJSONObject(1).has(Keys.EndpointProducts.KEY_DESCRIPTION_VALUE) && !attrs.getJSONObject(1).isNull(Keys.EndpointProducts.KEY_DESCRIPTION_VALUE)){
                            description= attrs.getJSONObject(1).getString(Keys.EndpointProducts.KEY_DESCRIPTION_VALUE);
                        }
                    }
                }

                if (!id.equals("-1") && !label.equals(Constants.NA)) {
                    product = new Product(id, label, msrp, offerPrice, link, image,description);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return product;
    }

    private void setUpPage(){
        if (!product.getImageLink().equals(Constants.NA)){
            imageLoader.get(product.getImageLink(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    productImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
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

        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public String getRequestUrl(){
        return API_SOUQ_PRODUCTS1+productID+API_SOUQ_PRODUCTS2+ MyApplication.CLIENT_ID+MyApplication.API_KEY_SOUQ;
    }
}

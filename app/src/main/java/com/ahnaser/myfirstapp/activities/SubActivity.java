package com.ahnaser.myfirstapp.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahnaser.myfirstapp.MyApplication;
import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.network.VolleySingleton;
import com.ahnaser.souqapi.SouqAPIConnection;
import com.ahnaser.souqapi.SouqAPIResult;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SubActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private TextView textVolleyError;
    private ImageView productImage;
    private TextView productLabel,marketPrice,offerPrice;
    private WebView description;
    private String productID;
    public com.ahnaser.souqapi.pojos.Product product;

    SouqAPIConnection souqAPIConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        volleySingleton=VolleySingleton.getInstance();
        imageLoader=volleySingleton.getImageLoader();
        textVolleyError=(TextView) findViewById(R.id.textVolleyError);
        productImage=(ImageView) findViewById(R.id.productImage);
        productLabel=(TextView) findViewById(R.id.productLabel);
        marketPrice =(TextView) findViewById(R.id.marketPrice);
        offerPrice=(TextView) findViewById(R.id.offerPrice);
        description=(WebView) findViewById(R.id.description);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        productID = extras.getString("productID");

        souqAPIConnection=new SouqAPIConnection(MyApplication.CLIENT_ID,MyApplication.API_KEY_SOUQ,this);

        setProduct();

    }

    private void setProduct(){

        Map<String,String> params=new HashMap<>();
        params.put("show_attributes","1");

        souqAPIConnection.setResponseObserver(new SouqAPIConnection.ResponseObserver() {
            @Override
            public void onError(VolleyError error) {
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

            @Override
            public void onSuccess(JSONObject response, int statusCode) {
                textVolleyError.setVisibility(View.GONE);
                SouqAPIResult souqAPIResult=new SouqAPIResult(statusCode,response);
                try {
                    product=new com.ahnaser.souqapi.pojos.Product(souqAPIResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setUpPage();
            }
        });

        souqAPIConnection.get("products/"+productID, params);
    }

    private void setUpPage() {
        if (product.getImage_large_link() != null) {
            imageLoader.get(product.getImage_large_link(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    productImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }

        productLabel.setText(product.getLabel());
        marketPrice.setText("Market Price: "+product.getMsrp());
        offerPrice.setText("Offer Price: "+product.getOffer_price());
        description.loadData(product.getDescription(), "text/html", null);
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
}

 package com.ahnaser.myfirstapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

 /**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String API_SOUQ_PRODUCTS_SEARCH="https://api.souq.com/v1/products?q=iphone&&page=1&show=20&show_attributes=0&country=ae&language=en&format=json";
     private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
     private RequestQueue requestQueue;
    private ArrayList<Product> listProducts=new ArrayList<>();
    private RecyclerView productsList;
    private AdapterProducts adapterProducts;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();
        sendJsonRequest();

    }

    private void sendJsonRequest(){
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, getRequestUrl(),(String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listProducts=parseJSONRequest(response);
                adapterProducts.setListProducts(listProducts);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }

     private ArrayList<Product> parseJSONRequest(JSONObject response) {
         ArrayList<Product> products=new ArrayList<>();

         if (response!=null || response.length()>0) {
             try {
                 JSONObject dataObject = response.getJSONObject(Keys.EndpointProducts.KEY_DATA);
                 try {
                     StringBuilder data = new StringBuilder();
                     JSONArray arrayProducts = dataObject.getJSONArray(Keys.EndpointProducts.KEY_PRODUCTS);
                     for (int i = 0; i < arrayProducts.length(); i++) {
                         JSONObject currentProduct = arrayProducts.getJSONObject(i);

                         String id = currentProduct.getString(Keys.EndpointProducts.KEY_ID);

                         String label = currentProduct.getString(Keys.EndpointProducts.KEY_LABEL);

                         String msrp = currentProduct.getString(Keys.EndpointProducts.KEY_MARKET_PRICE) + " AED";

                         String offerPrice = "No Offer";
                         if (currentProduct.has(Keys.EndpointProducts.KEY_OFFER_PRICE)) {
                             offerPrice = currentProduct.getString(Keys.EndpointProducts.KEY_OFFER_PRICE) + " AED";
                         }

                         JSONObject images = currentProduct.getJSONObject(Keys.EndpointProducts.KEY_IMAGES);
                         String image = null;
                         if (images.has(Keys.EndpointProducts.KEY_SMALL)) {
                             image = images.getJSONArray(Keys.EndpointProducts.KEY_SMALL).getString(0);
                         }

                         String link = currentProduct.getString(Keys.EndpointProducts.KEY_LINK);

                         Product product = new Product(id, label, msrp, offerPrice, link, image);

                         products.add(product);

                     }
                     //L.T(getActivity(),listProducts.toString());


                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }

         return products;

     }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        productsList=(RecyclerView) view.findViewById(R.id.listProducts);
        productsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterProducts=new AdapterProducts(getActivity());
        productsList.setAdapter(adapterProducts);
        sendJsonRequest();
        return view;
    }

    public static String getRequestUrl(){
        return API_SOUQ_PRODUCTS_SEARCH+MyApplication.CLIENT_ID+MyApplication.API_KEY_SOUQ;
    }
}

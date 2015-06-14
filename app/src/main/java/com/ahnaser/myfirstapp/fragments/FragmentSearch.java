 package com.ahnaser.myfirstapp.fragments;


 import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahnaser.myfirstapp.MyApplication;
import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.activities.SubActivity;
import com.ahnaser.myfirstapp.adapters.AdapterProducts;
import com.ahnaser.myfirstapp.extras.Constants;
import com.ahnaser.myfirstapp.extras.Keys;
import com.ahnaser.myfirstapp.extras.ProductSorter;
import com.ahnaser.myfirstapp.extras.SortListener;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

 /**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment implements SortListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String query="iphone";
    private static String API_SOUQ_PRODUCTS_SEARCH1="https://api.souq.com/v1/products?q=";
     private static String API_SOUQ_PRODUCTS_SEARCH2="&&page=";
     private static String API_SOUQ_PRODUCTS_SEARCH3="&show=20&show_attributes=0&country=ae&language=en&format=json";
     private int current=1,totalPages=1,totalItems=1;
     private VolleySingleton volleySingleton;
     private RequestQueue requestQueue;
    private ArrayList<Product> listProducts=new ArrayList<>();
    private RecyclerView productsList;
    private AdapterProducts adapterProducts;
    private TextView textVolleyError;
     private ProductSorter productSorter=new ProductSorter();
     private int previousTotal = 0;
     private boolean loading = true;
     private int visibleThreshold = 5;
     private int firstVisibleItem, visibleItemCount, totalItemCount;
     private LinearLayoutManager mLayoutManager;
     HashMap <String,String> params;
     JSONObject paraobj;


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
        mLayoutManager=new LinearLayoutManager(getActivity());
    }

     private void sendNewJsonRequest(){

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, getRequestUrl(),(String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textVolleyError.setVisibility(View.GONE);
                listProducts=parseJSONRequest(response);
                adapterProducts.setListProducts(listProducts);

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

     private ArrayList<Product> parseJSONRequest(JSONObject response) {
         ArrayList<Product> products=new ArrayList<>();

         if (response!=null || response.length()>0) {

             if(response.has(Keys.EndpointProducts.KEY_META) && !response.isNull(Keys.EndpointProducts.KEY_META)){
                 JSONObject meta = null;
                 try {
                     meta = response.getJSONObject(Keys.EndpointProducts.KEY_META);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 if(meta.has(Keys.EndpointProducts.KEY_PAGES) && !meta.isNull(Keys.EndpointProducts.KEY_PAGES)){
                     try {
                         this.totalPages=meta.getInt(Keys.EndpointProducts.KEY_PAGES);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }

                 if(meta.has(Keys.EndpointProducts.KEY_TOTAL) && !meta.isNull(Keys.EndpointProducts.KEY_TOTAL)){
                     try {
                         this.totalItems=meta.getInt(Keys.EndpointProducts.KEY_TOTAL);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                 }

             }


             try {
                 JSONObject dataObject = response.getJSONObject(Keys.EndpointProducts.KEY_DATA);
                 try {
                     JSONArray arrayProducts = dataObject.getJSONArray(Keys.EndpointProducts.KEY_PRODUCTS);
                     for (int i = 0; i < arrayProducts.length(); i++) {

                         String id="-1";
                         String label= Constants.NA;
                         String msrp=Constants.NA;
                         String offerPrice=Constants.NA;
                         String link=Constants.NA;
                         String image=Constants.NA;

                         JSONObject currentProduct = arrayProducts.getJSONObject(i);
                         if(currentProduct.has(Keys.EndpointProducts.KEY_ID) && !currentProduct.isNull(Keys.EndpointProducts.KEY_ID)) {
                             id = currentProduct.getString(Keys.EndpointProducts.KEY_ID);
                         }

                         if(currentProduct.has(Keys.EndpointProducts.KEY_LABEL) && !currentProduct.isNull(Keys.EndpointProducts.KEY_LABEL)) {
                             label = currentProduct.getString(Keys.EndpointProducts.KEY_LABEL);
                         }

                         if(currentProduct.has(Keys.EndpointProducts.KEY_MARKET_PRICE) && !currentProduct.isNull(Keys.EndpointProducts.KEY_MARKET_PRICE)) {
                             msrp = "Price: "+ currentProduct.getString(Keys.EndpointProducts.KEY_MARKET_PRICE) + " AED";
                         }

                         if (currentProduct.has(Keys.EndpointProducts.KEY_OFFER_PRICE) && !currentProduct.isNull(Keys.EndpointProducts.KEY_OFFER_PRICE))
                             offerPrice = "Price: "+currentProduct.getString(Keys.EndpointProducts.KEY_OFFER_PRICE) + " AED";
                         else
                             offerPrice=msrp;

                         if(currentProduct.has(Keys.EndpointProducts.KEY_IMAGES) && !currentProduct.isNull(Keys.EndpointProducts.KEY_IMAGES)) {
                             JSONObject images = currentProduct.getJSONObject(Keys.EndpointProducts.KEY_IMAGES);

                             if (images.has(Keys.EndpointProducts.KEY_SMALL) && !images.isNull(Keys.EndpointProducts.KEY_SMALL)) {
                                 image = images.getJSONArray(Keys.EndpointProducts.KEY_SMALL).getString(0);
                             }
                         }

                         if(currentProduct.has(Keys.EndpointProducts.KEY_LINK) && !currentProduct.isNull(Keys.EndpointProducts.KEY_LINK)) {
                             link = currentProduct.getString(Keys.EndpointProducts.KEY_LINK);
                         }


                         if(!id.equals("-1") && !label.equals(Constants.NA) ) {
                             Product product = new Product(id, label, msrp, offerPrice, link, image);
                             products.add(product);
                         }

                     }

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
        textVolleyError=(TextView) view.findViewById(R.id.textVolleyError);
        productsList=(RecyclerView) view.findViewById(R.id.listProducts);
        productsList.setLayoutManager(mLayoutManager);
        adapterProducts=new AdapterProducts(getActivity());
        productsList.setAdapter(adapterProducts);
        productsList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), productsList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), SubActivity.class);
                intent.putExtra("productID", listProducts.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        productsList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = productsList.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    current++;
                    if(current<=totalPages)
                        onCurrentSearch();
                    loading = true;
                }
            }
        });

        sendNewJsonRequest();
        return view;
    }

    public  String getRequestUrl(){
        return API_SOUQ_PRODUCTS_SEARCH1+query+API_SOUQ_PRODUCTS_SEARCH2+Integer.toString(current)+API_SOUQ_PRODUCTS_SEARCH3+MyApplication.CLIENT_ID+ MyApplication.API_KEY_SOUQ;
    }

     @Override
     public void onSortByName() {

         productSorter.sortProductByName(listProducts);
         adapterProducts.notifyDataSetChanged();

     }

     @Override
     public void onSortByPrice() {

         productSorter.sortProductByPrice(listProducts);
         adapterProducts.notifyDataSetChanged();

     }

     public void newSearch(String q){
         query=q;
         current=1;
         totalPages=1;
         totalItems=1;
         adapterProducts=new AdapterProducts(getActivity());
         productsList.setAdapter(adapterProducts);
         sendNewJsonRequest();
     }

     public void onCurrentSearch(){
         JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, getRequestUrl(),(String) null, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 textVolleyError.setVisibility(View.GONE);
                 listProducts.addAll(parseJSONRequest(response));
                 //adapterProducts.setListProducts(listProducts);
                 adapterProducts.notifyDataSetChanged();
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

     class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
         private GestureDetector gestureDetector;
         private ClickListener clickListener;

         public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
             this.clickListener=clickListener;
             gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                 @Override
                 public boolean onSingleTapUp(MotionEvent e) {
                     return true;
                 }

                 @Override
                 public void onLongPress(MotionEvent e) {
                     View child= recyclerView.findChildViewUnder(e.getX(),e.getY());
                     if(child!=null && clickListener!=null){
                         clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                     }
                 }
             });

         }

         @Override
         public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

             View child= rv.findChildViewUnder(e.getX(),e.getY());
             if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
                 clickListener.onClick(rv,rv.getChildAdapterPosition(child));

             }
             return false;
         }

         @Override
         public void onTouchEvent(RecyclerView rv, MotionEvent e) {
         }
     }

     public static interface ClickListener{
         public void onClick(View view, int position);
         public void onLongClick(View view,int position);

     }

     class onScroll extends RecyclerView.OnScrollListener {

     }
 }

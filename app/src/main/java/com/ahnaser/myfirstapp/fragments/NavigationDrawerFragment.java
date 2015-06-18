package com.ahnaser.myfirstapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.adapters.Adapter;
import com.ahnaser.myfirstapp.pojo.Information;
import com.ahnaser.souqapi.AccessToken;
import com.ahnaser.souqapi.SouqAPIConnection;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment //implements Adapter.ClickListener
 {

    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private Adapter adapter;
    private TextView textView;
    private SharedPreferences sharedPreferences;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readToPreference(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout= inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView= (RecyclerView) layout.findViewById(R.id.drawerList);
        textView=(TextView) layout.findViewById(R.id.logged);
        adapter=new Adapter(getActivity(),getData());
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        return layout;
    }

    public static List<Information> getData(){
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_number1,R.drawable.ic_number2,R.drawable.ic_number3,R.drawable.ic_number4,R.drawable.ic_number5};
        String[] titles = {"Phones","Laptops","Cloths", "BlaBla", "BlaBla2"};
        for(int i=0;i<icons.length && i<titles.length;i++){
            Information current = new Information();
            current.icon_Id=icons[i%icons.length];
            current.title=titles[i%icons.length];
            data.add(current);

        }
        return data;
    }


    public void setUp(int fragmentId,DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView=getActivity().findViewById(fragmentId);
        mDrawerLayout=drawerLayout;
        mDrawerToggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String customerId=sharedPreferences.getString("customer_id","");
                String value=sharedPreferences.getString("value","");
                if(!customerId.equalsIgnoreCase("") && !value.equalsIgnoreCase("")) {
                    AccessToken accessToken=new AccessToken(value,customerId);
                    SouqAPIConnection connection=new SouqAPIConnection("38607576","EB008DQ5bnzmSZty8fyp",getActivity());
                    connection.setAccessToken(accessToken);
                    Map<String,String> params = new HashMap<>();
                    params.put("customer_id",connection.getAccessToken().getCustomerId());
                    connection.setResponseObserver(new SouqAPIConnection.ResponseObserver() {
                        @Override
                        public void onError(VolleyError error) {
                            textView.setText("You're Not Logged");
                        }

                        @Override
                        public void onSuccess(JSONObject response, int statusCode) {
                            try {
                                textView.setText("Welcome "+response.getJSONObject("data").getString("firstname"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    connection.get("customers/"+connection.getAccessToken().getCustomerId()+"/profile",params);

                }

                if(!mFromSavedInstanceState){
                    mUserLearnedDrawer=true;
                    saveToPreference(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset<0.6)
                    toolbar.setAlpha(1-slideOffset);

            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreference(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }

    public static String readToPreference(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    /*@Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getActivity(),SubActivity.class));
    }*/

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
}

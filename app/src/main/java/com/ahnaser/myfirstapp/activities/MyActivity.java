package com.ahnaser.myfirstapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.extras.L;
import com.ahnaser.myfirstapp.extras.SortListener;
import com.ahnaser.myfirstapp.fragments.FragmentSearch;
import com.ahnaser.myfirstapp.fragments.NavigationDrawerFragment;
import com.ahnaser.myfirstapp.tabs.SlidingTabLayout;
import com.ahnaser.souqapi.AccessToken;
import com.ahnaser.souqapi.SouqAPIConnection;
import com.android.volley.VolleyError;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MyActivity extends ActionBarActivity implements View.OnClickListener {

    private SearchView searchView;
    private Toolbar toolbar;
    private SlidingTabLayout mTabs;
    private ViewPager mPager;
    private static final int PRODUCT_SEARCH_RESULTS=0;
    private static final String TAG_SORT_NAME = "sortName";
    private static final String TAG_SORT_PRICE= "sortPrice";
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment= (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //searchView=(SearchView)findViewById(R.id.searchView);

        mPager=(ViewPager) findViewById(R.id.pager);
        adapter=new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mTabs=(SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabView(R.layout.custome_tab_view, R.id.tabText);
        mTabs.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.accentColor));
        mTabs.setViewPager(mPager);

        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.ic_sorter);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(imageView).build();

        ImageView sortPrice=new ImageView(this);
        sortPrice.setImageResource(R.drawable.ic_sort_price);
        ImageView sortName=new ImageView(this);
        sortName.setImageResource(R.drawable.ic_sort_name);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        SubActionButton buttonSortPrice = itemBuilder.setContentView(sortPrice).build();
        SubActionButton buttonSortName = itemBuilder.setContentView(sortName).build();
        buttonSortName.setOnClickListener(this);
        buttonSortPrice.setOnClickListener(this);
        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortPrice.setTag(TAG_SORT_PRICE);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortPrice)
                .addSubActionView(buttonSortName)
                .attachTo(actionButton)
                .build();
        handleIntent(getIntent());

        //test();
    }

    private void test(){
        SouqAPIConnection connection=new SouqAPIConnection("38607576","EB008DQ5bnzmSZty8fyp",this);
        AccessToken accessToken=new AccessToken("aSW5tbq5ssE1LJmtfTsqpIydmD8pK6wpnwXDsWhu","9190178");
        connection.setAccessToken(accessToken);
        Map<String,String> params=new HashMap<String,String>();
        params.put("cart_id","36141702");
        params.put("offer_id", "37725300033");
        connection.setResponseObserver(new SouqAPIConnection.ResponseObserver() {
            @Override
            public void onError(VolleyError error) {

                L.T(getApplicationContext(), Integer.toString(error.networkResponse.statusCode));
            }

            @Override
            public void onSuccess(JSONObject response) {

                JSONObject data=new JSONObject();
                try {
                    JSONArray arr=response.getJSONArray("data");
                    data.put("data",arr);
                    L.T(getApplicationContext(), data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        connection.delete("carts/36141702/offers/37725300033", params);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            //Fragment fragment=(Fragment)getSupportFragmentManager().findFragmentById(mPager.getCurrentItem());
            Fragment fragment=adapter.getRegisteredFragment(mPager.getCurrentItem());
            ((FragmentSearch) fragment).newSearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
            Toast.makeText(this,"Hey there"+item.getTitle(),Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id==R.id.using_tab_library){
            startActivity(new Intent(this,ActivityUsingTabLibrary.class));
        }

        if(id==R.id.oauth_example){
            startActivity(new Intent(this,OauthExample.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment= (Fragment) adapter.instantiateItem(mPager,mPager.getCurrentItem());
        if (fragment instanceof SortListener) {
            if (v.getTag().equals(TAG_SORT_NAME)) {
                ((SortListener) fragment).onSortByName();
            }
            else if (v.getTag().equals(TAG_SORT_PRICE)) {
                ((SortListener) fragment).onSortByPrice();
            }
        }

    }

    class MyPagerAdapter extends FragmentStatePagerAdapter{
        String[] tabText=getResources().getStringArray(R.array.tabs);
        int[] icons={R.drawable.ic_number1,R.drawable.ic_number2,R.drawable.ic_number3};
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText=getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;

            switch (position){
                case PRODUCT_SEARCH_RESULTS:
                    fragment= FragmentSearch.newInstance("", "");
                    break;
                case 1:
                    fragment=FragmentSearch.newInstance("","");
                    break;
                case 2:
                    fragment=FragmentSearch.newInstance("","");
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable=getResources().getDrawable(icons[position]);
            drawable.setBounds(0,0,36,36);
            ImageSpan imageSpan=new ImageSpan(drawable);
            SpannableString spannableString=new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    public static class MyFragment extends Fragment{
        private TextView textView;
        public static MyFragment getInstance(int position){
            MyFragment myFragment=new MyFragment();
            Bundle args=new Bundle();
            args.putInt("position",position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout=inflater.inflate(R.layout.fragment_my,container,false);
            textView=(TextView) layout.findViewById(R.id.position);
            Bundle bundle=getArguments();
            if(bundle!=null){
                textView.setText("Page No. "+bundle.getInt("position"));
            }
            return layout;
        }
    }
}

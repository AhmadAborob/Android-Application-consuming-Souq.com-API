package com.ahnaser.myfirstapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahnaser.myfirstapp.R;
import com.ahnaser.myfirstapp.extras.Constants;
import com.ahnaser.myfirstapp.network.VolleySingleton;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by root on 23/05/15.
 */
public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHolderProducts> {

    private LayoutInflater layoutInflater;
    private ArrayList<com.ahnaser.souqapi.pojos.Product> listProducts=new ArrayList<>();
    VolleySingleton volleySingleton;
    ImageLoader imageLoader;

    public AdapterProducts(Context context){
        layoutInflater=LayoutInflater.from(context);
        volleySingleton=VolleySingleton.getInstance();
        imageLoader=volleySingleton.getImageLoader();

    }

    public void setListProducts(ArrayList<com.ahnaser.souqapi.pojos.Product> listProducts){
        this.listProducts=listProducts;
        notifyItemRangeChanged(0,listProducts.size());

    }

    @Override
    public ViewHolderProducts onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.custome_product, parent, false);
        ViewHolderProducts viewHolderProducts=new ViewHolderProducts(view);
        return viewHolderProducts;
    }

    @Override
    public void onBindViewHolder(final ViewHolderProducts holder, int position) {
        com.ahnaser.souqapi.pojos.Product currentProduct=listProducts.get(position);
        holder.productLabel.setText(currentProduct.getLabel());
        holder.productPrice.setText(currentProduct.getOffer_price());
        String imageLink=currentProduct.getImage_small_link();
        loadImages(imageLink,holder);

    }

    public void loadImages(String imageLink, final ViewHolderProducts holder){

        if (!imageLink.equals(Constants.NA)){
            imageLoader.get(imageLink, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.productImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    static class ViewHolderProducts extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productLabel;
        private TextView productPrice;

        public ViewHolderProducts(View itemView) {
            super(itemView);
            productImage=(ImageView) itemView.findViewById(R.id.productImage);
            productLabel=(TextView) itemView.findViewById(R.id.productLabel);
            productPrice=(TextView) itemView.findViewById(R.id.productPrice);
        }
    }
}

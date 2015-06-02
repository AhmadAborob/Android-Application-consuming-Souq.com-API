package com.ahnaser.myfirstapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahnaser.myfirstapp.pojo.Information;
import com.ahnaser.myfirstapp.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by root on 18/05/15.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    //private ClickListener clickListener;
    List<Information> data= Collections.emptyList();
    public Adapter(Context context, List<Information> data){
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.data=data;
    }

    /*public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.custome_row, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current= data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.icon_Id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void delete (int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    //Method No.1 , Using OnClick Listener

    /*class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //context.startActivity(new Intent(context,SubActivity.class));
            if (clickListener!=null){
                clickListener.itemClicked(v,getAdapterPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }*/

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}

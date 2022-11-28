package com.biswa1045.akash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BillDetailsAdapter extends RecyclerView.Adapter<BillDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<bill> list;


    public BillDetailsAdapter(Context context, ArrayList<bill> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cat,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        bill p = list.get(position);

        String productname = p.getProductname();
        String productprice = p.getProductprice();
        String productqnt = p.getProductqntl();
        String img = p.getImg();
        holder.name.setText(productname);
        holder.price.setText(productprice+"/-");
        holder.qnt.setText(productqnt);
        Glide.with(context)
                .load(img)
                .into(holder.img);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView qnt;
        ImageView img;
        View mview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            img = itemView.findViewById(R.id.img_product);
            qnt = itemView.findViewById(R.id.stock_product);
            mview=itemView;

        }
    }

}

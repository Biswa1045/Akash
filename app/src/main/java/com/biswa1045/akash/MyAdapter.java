package com.biswa1045.akash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends FirebaseRecyclerAdapter<product,MyAdapter.MyViewHolder>
{
    public MyAdapter(@NonNull FirebaseRecyclerOptions<product> product)
    {
        super(product);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return  new MyViewHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull product model) {

        String productname = model.getProductname();
        String productprice = model.getProductprice();
        String img = model.getImg();
        holder.name.setText(productname);
        holder.price.setText(productprice+"/-");
        holder.stock.setText(model.getProductstuck()+" pcs");
        Glide.with(holder.img.getContext())
                .load(img)
                .into(holder.img);
        holder.mview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Product")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseDatabase.getInstance().getReference().child("products")
                                        .child(getRef(position).getKey()).removeValue();
                                notifyItemRemoved(position);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();

                return true;
            }
        });
        Dialog dialog = new Dialog(holder.itemView.getContext());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.add_product_dialog);
                EditText product_name =dialog.findViewById(R.id.product_name);
                EditText product_price=dialog.findViewById(R.id.product_price);
                EditText product_stock=dialog.findViewById(R.id.product_stock);
                Button done= dialog.findViewById(R.id.product_add_dialog);
                ImageView img_ = dialog.findViewById(R.id.product_img);
                Glide.with(holder.img.getContext())
                        .load(model.getImg())
                        .into(img_);
                product_name.setText(model.getProductname());
                product_price.setText(model.getProductprice());
                product_stock.setText(model.getProductstuck());
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String pn  = product_name.getText().toString();
                        String pp  = product_price.getText().toString();
                        String ps  = product_stock.getText().toString();
                        if(!pn.equals("") && !pp.equals("") && !ps.equals("")){

                            HashMap<String, String> map = new HashMap<>();
                            map.put("productname", pn);
                            map.put("productprice", pp);
                            map.put("productstuck",ps);
                            map.put("img", img);

                            FirebaseDatabase.getInstance().getReference().child("products")
                                    .child(getRef(position).getKey()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(view.getContext(), "data updated", Toast.LENGTH_SHORT).show();
                                            notifyItemChanged(position);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(view.getContext(), "Fail to add data $it", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });


                        }else{

                        }

                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView price;
        TextView stock;
        ImageView img;

        ImageView edit;
        View mview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            stock = itemView.findViewById(R.id.stock_product);
            img = itemView.findViewById(R.id.img_product);
            edit = itemView.findViewById(R.id.edit_product);
            mview=itemView;

        }
    }

}










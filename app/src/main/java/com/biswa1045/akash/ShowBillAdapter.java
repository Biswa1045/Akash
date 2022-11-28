package com.biswa1045.akash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShowBillAdapter extends RecyclerView.Adapter<ShowBillAdapter.MyViewHolder> {

    Context context;

    ArrayList<billList> list;


    public ShowBillAdapter(Context context, ArrayList<billList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_show_bill,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        billList p = list.get(position);
        String id = p.getId();
        String customerName = p.getCustomerName();
        String time = p.getTime();
        String invoice = p.getInvoice();
        String total = p.getTotal();
        holder.id.setText(invoice);
        holder.location.setText(p.getLocation());
        holder.customerName.setText(customerName);
        holder.time.setText(time);
        holder.total.setText(total+"/-");
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(),BillDetailsActivity.class);
                i.putExtra("id",id);
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView customerName;
        TextView time;
        TextView total;
        TextView location;
        View mview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.bill_id);
            customerName = itemView.findViewById(R.id.customer_name);
            time = itemView.findViewById(R.id.time);
            total = itemView.findViewById(R.id.total_bill);
            location = itemView.findViewById(R.id.location);
            mview=itemView;

        }
    }

}

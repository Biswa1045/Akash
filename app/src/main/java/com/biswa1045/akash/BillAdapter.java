package com.biswa1045.akash;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    Context context;
    View rootView;
    ArrayList<product> list;
    ArrayList<String> ExpAmtArray = new ArrayList<String>();
    ArrayList<String> ExpQntArray = new ArrayList<String>();
    ArrayList<String> ExpNameArray = new ArrayList<String>();
    ArrayList<String> ProductKey = new ArrayList<String>();
    ArrayList<String> ExpImgArray = new ArrayList<String>();
    Float ExpenseFinalTotal_i=0F;
    TextView textviewTotalExpense;
    CardView create_bill;
    int countl ,count_invoice=0;
    Float total_f;
    Dialog dialog;
    String billerName_s,phonenumber_s,discount_s;
    SharedPreferences sharedPreferences;

    public BillAdapter(Context context, ArrayList<product> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_bill,parent,false);

        context = parent.getContext();
        rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        textviewTotalExpense = (TextView) rootView.findViewById(R.id.total);
        create_bill = rootView.findViewById(R.id.create_bill);
        //attach view to MyViewHolder
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        product p = list.get(position);
        String productname = p.getProductname();
        String productprice = p.getProductprice();
        String productstuck = p.getProductstuck();
        String img = p.getImg();
        holder.name.setText(productname);
        holder.price.setText(productprice+"/-");
        holder.stock.setText(productstuck+" pcs");
        Glide.with(context)
                .load(img)
                .into(holder.img);
           ExpenseFinalTotal_i  = ExpenseFinalTotal_i + Float.parseFloat(productprice);
           textviewTotalExpense.setText(String.valueOf(ExpenseFinalTotal_i));
           ExpAmtArray.add(position,productprice);
           ExpQntArray.add(position,"1");
           ExpNameArray.add(position,productname);
           ExpImgArray.add(position,img);
      //  ProductKey.add(0,"empty");

        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float number =Float.parseFloat(holder.qnt.getText().toString());
                if(number>1){
                    Float num=number -1;
                    holder.qnt.setText(num.toString());
                    holder.price.setText(String.valueOf(Float.parseFloat(productprice)*num));
                }else{

                }
            }
        });
        holder.qnt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                try{
                    if(!holder.qnt.toString().equals("")){
                        Float num = Float.parseFloat(holder.qnt.getText().toString());
                        holder.price.setText(String.valueOf(Float.parseFloat(productprice)*num));
                            ExpAmtArray.set(position,holder.price.getText().toString());
                            ExpQntArray.set(position,holder.qnt.getText().toString());
                            Log.d("Array","\n"+ExpNameArray+"\n"+ExpImgArray+"\n"+ExpAmtArray+"\n"+ExpQntArray);
                            Float sum=0F;
                            for(int j=0;j<ExpAmtArray.size();j++){
                                sum+=Float.parseFloat(ExpAmtArray.get(j));
                            }
                        textviewTotalExpense.setText(String.valueOf(sum));
                    }
                }catch(Exception e){

                }


            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float number =Float.parseFloat(holder.qnt.getText().toString());
                if(number>0){
                    Float num=number +1;
                    holder.qnt.setText(num.toString());
                    holder.price.setText(String.valueOf(Float.parseFloat(productprice)*num));
                }else{

                }
            }
        });
        dialog = new Dialog(context);
        create_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.bill_confirm_dialog);
                EditText billerName = dialog.findViewById(R.id.biller_name);
                EditText phonenumber = dialog.findViewById(R.id.biller_no);
                EditText discount = dialog.findViewById(R.id.discount);
                ProgressBar pppp = dialog.findViewById(R.id.pppp);
                dialog.findViewById(R.id.yes_bill).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pppp.setVisibility(View.VISIBLE);
                        billerName_s = billerName.getText().toString();
                        phonenumber_s = phonenumber.getText().toString();
                        discount_s = discount.getText().toString();
                        if(discount.getText().toString().length()==0){
                            discount_s="0";
                        }

                        dialog.findViewById(R.id.yes_bill).setEnabled(false);
                        //get time
                        if(!billerName_s.equals("")) {


                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            final String currentDateandTime = sdf.format(new Date());
                            final String date = sdf2.format(new Date());
                            // get chil count

                            DocumentReference countr = FirebaseFirestore.getInstance().collection("bill").document("bill_count");
                            DocumentReference countbill = FirebaseFirestore.getInstance().collection("bill").document(date);

                            countbill.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        count_invoice = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("count")));
                                    }else{
                                        Map<String,Object> datainvoice = new HashMap<>();
                                        datainvoice.put("count",String.valueOf(0));
                                        countbill.set(datainvoice);

                                    }
                                }
                            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {




                                    countr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        countl = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("count")));
                                                        Log.d("count_database", String.valueOf(countl));

                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            })
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    final int count = countl+1;
                                                    final int countINV = count_invoice+1;
                                                    SharedPreferences shared = context.getSharedPreferences("sharedpreference2", MODE_PRIVATE);
                                                    String location = shared.getString("shopname", "null");
                                                    // create bill no



                                                    Float dis = (Float.parseFloat(textviewTotalExpense.getText().toString())*Float.parseFloat(discount_s))/100;
                                                    total_f = Float.parseFloat(textviewTotalExpense.getText().toString())-dis;




                                                    DatabaseReference databaseReferenceD = FirebaseDatabase.getInstance().getReference().child("bills").child(currentDateandTime + "_" + String.valueOf(count));
                                                    Log.d("count_1st", String.valueOf(count));
                                                    HashMap<String, String> map_details = new HashMap<>();
                                                    map_details.put("id", currentDateandTime + "_" + String.valueOf(count));
                                                    map_details.put("customerName", billerName_s);
                                                    map_details.put("time", currentDateandTime);
                                                    map_details.put("invoice",date+"-"+"INV"+countINV);
                                                    map_details.put("discount",discount_s);
                                                    map_details.put("location",location);
                                                    map_details.put("total", total_f.toString());
                                                    databaseReferenceD.setValue(map_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            for (int k = 0; k < list.size(); k++) {

                                                                DatabaseReference databaseReference;
                                                                //    String id = FirebaseDatabase.getInstance().getReference().child("bills").child(currentDateandTime + "_" + String.valueOf(count)).child("order_list").getKey();

                                                                databaseReference = FirebaseDatabase.getInstance().getReference().child("bills").child(currentDateandTime + "_" + String.valueOf(count)).child("order_list").child(k+"");

                                                                HashMap<String, String> map = new HashMap<>();
                                                                map.put("productname", ExpNameArray.get(k));
                                                                map.put("productprice", ExpAmtArray.get(k));
                                                                map.put("productqntl", ExpQntArray.get(k));
                                                                map.put("img", ExpImgArray.get(k));
                                                                databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(context.getApplicationContext(), "Bill generated", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(context.getApplicationContext(), "Fail to generate bill, Try again later", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });
                                                            }
                                                            Map<String,Object> data = new HashMap<>();
                                                            data.put("count",String.valueOf(count));
                                                            Log.d("count_upload", String.valueOf(count));
                                                            DocumentReference countr = FirebaseFirestore.getInstance().collection("bill").document("bill_count");

                                                            countr.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(context.getApplicationContext(), "Bill generated", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context.getApplicationContext(), "Bill not generated,check internet", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    for(int j=0;j<ExpNameArray.size();j++){
                                                                        String key = ExpNameArray.get(j);

                                                                        FirebaseDatabase.getInstance().getReference().child("products").orderByChild("productname").equalTo(key).addListenerForSingleValueEvent(
                                                                                new ValueEventListener() {

                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        //Map of all products which has city equal to mCurrentUserCity
                                                                                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                                                                            String productkey = childSnapshot.getKey();
                                                                                            ProductKey.add(productkey);
                                                                                        }
                                                                                    }
                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                    }

                                                                                }
                                                                        );
                                                                    }

                                                                    Handler handler = new Handler();
                                                                    final Runnable r = new Runnable() {
                                                                        public void run() {
                                                                              //  if(ProductKey.size()==ExpNameArray.size()){
                                                                                    Intent intent = new Intent(context.getApplicationContext(),TotalActivity.class);
                                                                                    intent.putExtra("total",textviewTotalExpense.getText().toString());
                                                                                    intent.putExtra("ProductKey",ProductKey);
                                                                                    intent.putExtra("ExpAmtArray",ExpAmtArray);
                                                                                    intent.putExtra("ExpNameArray",ExpNameArray);
                                                                                    intent.putExtra("productqnt",ExpQntArray);
                                                                                    intent.putExtra("phonenumber",phonenumber_s);
                                                                                    intent.putExtra("discount",discount_s);
                                                                                    intent.putExtra("invoice",date+"-"+"INV"+countINV);
                                                                                    context.startActivity(intent);
                                                                            //    }


                                                                        }
                                                                    };

                                                                    handler.postDelayed(r, 1500);



                                                                }
                                                            });



                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context.getApplicationContext(), "Something went wrong, Try again later", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                                }
                                            });






                                    Map<String,Object> datainvoice2 = new HashMap<>();
                                    datainvoice2.put("count",String.valueOf(count_invoice+1));
                                    countbill.update(datainvoice2);







                                }
                            });




                        }else{
                            Toast.makeText(context.getApplicationContext(), "Enter Customer Name", Toast.LENGTH_SHORT).show();
                            pppp.setVisibility(View.GONE);
                            dialog.findViewById(R.id.yes_bill).setEnabled(true);

                        }
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView stock;
        ImageView img;
        View mview;
        CardView min,add;
        EditText qnt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            img = itemView.findViewById(R.id.img_product);
            stock = itemView.findViewById(R.id.stock_product);
            min = itemView.findViewById(R.id.qnt_min);
            qnt = itemView.findViewById(R.id.qnt);
            add = itemView.findViewById(R.id.qnt_add);

            mview=itemView;

        }
    }

}

package com.biswa1045.akash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class BillDetailsActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: BillDetailsAdapter? = null
    lateinit var bill_list: ArrayList<bill>
    lateinit var database: DatabaseReference
    var id_d:String=""
     var customerName:String=""
     var time_d:String=""
     var total:String=""
    var discount:String=""
    lateinit var idt:TextView
    lateinit var namet:TextView
    lateinit var datet:TextView
    lateinit var totalt:TextView
    lateinit var discountt:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_details)
        var id :String= intent.extras!!.getString("id").toString()
        idt=findViewById<TextView>(R.id.bill_d_id)
        namet=findViewById<TextView>(R.id.bill_d_name)
        totalt=findViewById<TextView>(R.id.bill_d_amt)
        discountt=findViewById<TextView>(R.id.bill_d_discount)
        datet=findViewById<TextView>(R.id.bill_d_date)

        recyclerView = findViewById(R.id.biil_details_rec)
        recyclerView?.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        bill_list = ArrayList<bill>()
        adapter = BillDetailsAdapter(this, bill_list)

        database = FirebaseDatabase.getInstance().reference.child("bills").child(id).child("order_list")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bill_list.clear()
                for (dataSnapshot in snapshot.children) {
                    val mproduct: bill? = dataSnapshot.getValue(bill::class.java)
                    if (mproduct != null) {
                        bill_list!!.add(mproduct)
                    }
                }

                adapter!!.notifyDataSetChanged()
                recyclerView?.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        FirebaseDatabase.getInstance().reference.child("bills").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                        id_d = dataSnapshot.child("invoice").value.toString()
                        customerName = dataSnapshot.child("customerName").value.toString()
                        time_d = dataSnapshot.child("invoice").value.toString().subSequence(0,10).toString()
                        total = dataSnapshot.child("total").value.toString()
                        discount = dataSnapshot.child("discount").value.toString()
                    idt.setText(id_d)
                    namet.setText(customerName)
                    datet.setText(time_d)
                    totalt.setText(total)
                    discountt.setText(discount)



                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                  }
            })














    }

    override fun onBackPressed() {
        val intent = Intent(this@BillDetailsActivity,ShowBillActivity::class.java)
        startActivity(intent)
        finish()
    }
}
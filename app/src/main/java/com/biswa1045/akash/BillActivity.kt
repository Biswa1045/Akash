package com.biswa1045.akash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class BillActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: BillAdapter? = null
    lateinit var bill_id:String
    lateinit var list: ArrayList<product>
    lateinit var database: DatabaseReference
    lateinit var total: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        bill_id= intent.extras?.getString("time").toString()
        recyclerView = findViewById(R.id.recycle_bill)
        total = findViewById(R.id.total)
        recyclerView?.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        list = ArrayList<product>()
        adapter = BillAdapter(this, list)

        database = FirebaseDatabase.getInstance().reference.child("temp_bills").child(bill_id)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val mproduct: product? = dataSnapshot.getValue(product::class.java)
                    if (mproduct != null) {
                        list!!.add(mproduct)
                    }
                }

                adapter!!.notifyDataSetChanged()
                recyclerView?.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        if (adapter!!.list.size > 0) {


            var total_n:Float= 0.0F
            for (i in 0 until adapter!!.list.size) {

               /* val data: MutableMap<String, Any> = HashMap()
                data["img"] = adapter!!.list[i].img
                data["productname"] = adapter!!.list[0].productname
                data["productprice"] = adapter.selected[i].productprice
                data["productstuck"] = adapter.selected[i].productstuck*/

                total_n += adapter!!.list[i].productprice.toFloat()
                total.text = total_n.toString()

            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@BillActivity,CartActivity::class.java)
        startActivity(intent)
        finish()
    }
}
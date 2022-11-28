package com.biswa1045.akash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.ArrayList

class StockActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: HomeAdapter
    lateinit var progressBar: ProgressBar
    lateinit var list: ArrayList<product>
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)
        progressBar = findViewById(R.id.p_stock)
        recyclerView = findViewById(R.id.recycle_home)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.layoutManager = gridLayoutManager
        list = ArrayList<product>()
        myAdapter = HomeAdapter(this, list)
        database = FirebaseDatabase.getInstance().reference.child("products")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val mproduct: product? = dataSnapshot.getValue(product::class.java)
                    if (mproduct != null) {
                        list!!.add(mproduct)
                    }
                }
                myAdapter!!.notifyDataSetChanged()
                recyclerView?.setAdapter(myAdapter)
                progressBar.setVisibility(View.GONE)
            }
            override fun onCancelled(error: DatabaseError) {
                progressBar.setVisibility(View.GONE)
            }
        })
    }

    override fun onBackPressed() {
            val intent = Intent(this@StockActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()
    }
}
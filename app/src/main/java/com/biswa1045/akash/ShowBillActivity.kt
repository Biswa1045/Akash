package com.biswa1045.akash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*

class ShowBillActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ShowBillAdapter? = null
    lateinit var bill_list: ArrayList<billList>
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_bill)
        findViewById<ProgressBar>(R.id.p_sb).visibility= View.VISIBLE
        recyclerView = findViewById(R.id.show_bill_rec)
     //   recyclerView?.setHasFixedSize(true)
      //  recyclerView!!.layoutManager = LinearLayoutManager(this)
      //  recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))


        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView!!.layoutManager = linearLayoutManager



        bill_list = ArrayList<billList>()
        adapter = ShowBillAdapter(this, bill_list)

        database = FirebaseDatabase.getInstance().reference.child("bills")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bill_list.clear()
                for (dataSnapshot in snapshot.children) {
                    val mproduct: billList? = dataSnapshot.getValue(billList::class.java)
                    if (mproduct != null) {
                        bill_list!!.add(mproduct)
                    }
                }

                adapter!!.notifyDataSetChanged()
                recyclerView?.adapter = adapter
                findViewById<ProgressBar>(R.id.p_sb).visibility= View.INVISIBLE

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this@ShowBillActivity,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
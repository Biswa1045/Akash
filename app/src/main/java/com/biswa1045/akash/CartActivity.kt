package com.biswa1045.akash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CartActivity : AppCompatActivity() {
    lateinit var database: DatabaseReference
    private var recyclerView: RecyclerView? = null
    private var checks: ArrayList<check> = ArrayList()
    private lateinit var adapter: MultiAdapter
    private lateinit var others_name:EditText
    private lateinit var others_price:EditText
    lateinit var databaseReference:DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        others_name = findViewById(R.id.name_others)
        others_price = findViewById(R.id.price_others)

      //  btnGetSelected = findViewById<View>(R.id.btnGetSelected) as AppCompatButton
        recyclerView = findViewById<View>(R.id.recycle_cart) as RecyclerView

       // supportActionBar!!.setTitle("Multiple Selection")
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = MultiAdapter(this, checks)
        recyclerView!!.adapter = adapter
        database = FirebaseDatabase.getInstance().reference.child("products")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                checks.clear()
                for (dataSnapshot in snapshot.children) {
                   // val mproduct: product? = dataSnapshot.getValue(product::class.java)
                    val name=dataSnapshot.child("productname").value.toString()
                    val price=dataSnapshot.child("productprice").value.toString()
                    val stock=dataSnapshot.child("productstuck").value.toString()
                    val img=dataSnapshot.child("img").value.toString()

                        checks.add(check(false,name,price,stock,img))

                }
                adapter!!.setBillList(checks)
              //  adapter!!.notifyDataSetChanged()
               // recyclerView?.adapter = adapter
               // progressBar.setVisibility(View.GONE)
            }

            override fun onCancelled(error: DatabaseError) {
               // progressBar.setVisibility(View.GONE)
            }
        })

        findViewById<LinearLayout>(R.id.next).setOnClickListener{
             val formatted:String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"))
            } else {
               val SDFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
                  formatted = SDFormat.format(Date())
            }


          /*  val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")
            val formatted = current.format(formatter)
*/
            val othername = others_name.text.toString()
            val otherprice = others_price.text.toString()
            if(othername!=""&&otherprice==""){
                Toast.makeText(this@CartActivity, "Enter other product price", Toast.LENGTH_SHORT).show()
            }
            else if(otherprice!=""&&othername==""){
                Toast.makeText(this@CartActivity, "Enter other product name", Toast.LENGTH_SHORT).show()

            }
            else if((othername!="" && otherprice!="")||adapter.selected.size > 0){
                if(adapter.selected.size > 0){
                    for (i in adapter.selected.indices) {
                        // bill_str.add(product())
                        val data: MutableMap<String, Any> = HashMap()
                        data["img"] = adapter.selected[i].img
                        data["productname"] = adapter.selected[i].productname
                        data["productprice"] = adapter.selected[i].productprice
                        data["productstuck"] = adapter.selected[i].productstuck
                        // Toast.makeText(this@CartActivity, adapter.selected.size.toString(), Toast.LENGTH_SHORT).show()

                        val id: String? =FirebaseDatabase.getInstance().reference.child("products").push().key
                        databaseReference = FirebaseDatabase.getInstance().reference.child("temp_bills").child(formatted).child(id!!)

                        databaseReference.setValue(data).addOnCompleteListener {



                            if(adapter.selected.size.toString().toLong()-1==i.toLong()){
                                if(othername!="" && otherprice!=""){
                                    val data2: MutableMap<String, Any> = HashMap()
                                    data2["img"] = "https://firebasestorage.googleapis.com/v0/b/akash-a3a95.appspot.com/o/images%2FAccurator%20Others%20Logo.png?alt=media&token=7b0180c9-58b0-4b25-8645-9a9ec55d363a"
                                    data2["productname"] = othername
                                    data2["productprice"] = otherprice
                                    data2["productstuck"] = "1"
                                    val id: String? =FirebaseDatabase.getInstance().reference.child("products").push().key
                                    databaseReference = FirebaseDatabase.getInstance().reference.child("temp_bills").child(formatted).child(id!!)
                                    databaseReference.setValue(data2).addOnCompleteListener {
                                        nextAct(formatted.toString())

                                    }.addOnFailureListener {
                                        Toast.makeText(this@CartActivity, "Fail to add data $it", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }else{
                                    nextAct(formatted.toString())

                                }

                            }

                        }.addOnFailureListener {
                            Toast.makeText(this@CartActivity, "Fail to add data $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                if(othername!="" && otherprice!=""&&(adapter.selected.size <= 0)){
                    val data2: MutableMap<String, Any> = HashMap()
                    data2["img"] = "https://firebasestorage.googleapis.com/v0/b/akash-a3a95.appspot.com/o/images%2FAccurator%20Others%20Logo.png?alt=media&token=7b0180c9-58b0-4b25-8645-9a9ec55d363a"
                    data2["productname"] = othername
                    data2["productprice"] = otherprice
                    data2["productstuck"] = "1"
                    val id: String? =FirebaseDatabase.getInstance().reference.child("products").push().key
                    databaseReference = FirebaseDatabase.getInstance().reference.child("temp_bills").child(formatted).child(id!!)
                    databaseReference.setValue(data2).addOnCompleteListener {
                        nextAct(formatted.toString())

                    }.addOnFailureListener {
                        Toast.makeText(this@CartActivity, "Fail to add data $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                }




            }
            else{



                Toast.makeText(this, "Select At least one product ", Toast.LENGTH_SHORT).show()
            }
      //
        }
    findViewById<LinearLayout>(R.id.back).setOnClickListener{
        val intent =Intent(this@CartActivity,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    }



    private fun nextAct(id: String) {
        val intent =Intent(this@CartActivity,BillActivity::class.java)
        intent.putExtra("time",id)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        val intent = Intent(this@CartActivity,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}


package com.biswa1045.akash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: HomeAdapter
    lateinit var list: ArrayList<product>
    lateinit var database: DatabaseReference
    private val sharedPrefFile = "sharedpreference"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)




        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("check",null)
        if(sharedIdValue=="admin"){
            findViewById<CardView>(R.id.edit).visibility= View.VISIBLE
            findViewById<TextView>(R.id.signin).visibility=View.INVISIBLE
        }else{
            findViewById<TextView>(R.id.signin).visibility=View.INVISIBLE
        }
        findViewById<CardView>(R.id.edit).setOnClickListener(){
            val intent = Intent(this@HomeActivity,AddProductActivity::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<CardView>(R.id.show_bills).setOnClickListener(){
            val intent = Intent(this@HomeActivity,ShowBillActivity::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<CardView>(R.id.bill).setOnClickListener(){
            val intent = Intent(this@HomeActivity,CartActivity::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<CardView>(R.id.stock).setOnClickListener(){
            val intent = Intent(this@HomeActivity,StockActivity::class.java)
            startActivity(intent)
            finish()
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){

            }else{
                requestPermissions((arrayOf(Manifest.permission.SEND_SMS)),1)

            }
        }








        }

    fun signin_home(view: View) {
        val intent = Intent(this@HomeActivity,SigninActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {

        finishAffinity()
    }

}
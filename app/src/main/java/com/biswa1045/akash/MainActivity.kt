package com.biswa1045.akash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    lateinit var editor:SharedPreferences.Editor
    lateinit var editor2:SharedPreferences.Editor
    lateinit var sharedPreferences: SharedPreferences
    lateinit var shopname:EditText
    lateinit var sharedPreferences2: SharedPreferences
    private val sharedPrefFile = "sharedpreference"
    private val sharedPrefFile2 = "sharedpreference2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopname = findViewById(R.id.shopname)
        sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedIdValue = sharedPreferences.getString("check",null)
        editor=  sharedPreferences.edit()
        sharedPreferences2 = this.getSharedPreferences(sharedPrefFile2, Context.MODE_PRIVATE)
        val sharedIdValue2 = sharedPreferences2.getString("shopname",null)
        editor2=  sharedPreferences2.edit()
        findViewById<ImageView>(R.id.logo).visibility=View.VISIBLE
        Handler().postDelayed({
            if(sharedIdValue!=null && sharedIdValue2!=null){
                val intent = Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                findViewById<ImageView>(R.id.logo).visibility=View.INVISIBLE
                findViewById<LinearLayout>(R.id.l).visibility=View.VISIBLE
                findViewById<CardView>(R.id.c).visibility=View.VISIBLE
            }
        }, 1500)
    }
    fun signin(view: View) {
        if(shopname.text.toString().length!=0){
            editor2.putString("shopname", shopname.text.toString())
            editor2.apply()
            editor2.commit()
            val intent = Intent(this@MainActivity,SigninActivity::class.java)
            startActivity(intent)
            finish()
        } else{
            Toast.makeText(this@MainActivity,"Enter shop name",Toast.LENGTH_SHORT).show()

        }
    }
    fun home(view: View) {
        if(shopname.text.toString().length!=0) {
            editor.putString("check", "shop")
            editor.apply()
            editor.commit()
            editor2.putString("shopname", shopname.text.toString())
            editor2.apply()
            editor2.commit()
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this@MainActivity,"Enter shop name",Toast.LENGTH_SHORT).show()

        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
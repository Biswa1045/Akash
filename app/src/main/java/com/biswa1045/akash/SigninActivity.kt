package com.biswa1045.akash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class SigninActivity : AppCompatActivity() {
    lateinit var editor:SharedPreferences.Editor
    lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "sharedpreference"
    lateinit var passwod:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        passwod = findViewById(R.id.password)
         sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor=  sharedPreferences.edit()

    }
    fun done(view: View) {
        val pass = passwod.text.toString()
        if(pass!=""&&pass=="akash@123"){
            editor.putString("check","admin")
            editor.apply()
            editor.commit()
            val intent = Intent(this@SigninActivity,HomeActivity::class.java)
            startActivity(intent)


        }else{
            editor.putString("check","shop")
            editor.apply()
            editor.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
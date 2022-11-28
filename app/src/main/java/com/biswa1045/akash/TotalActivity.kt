package com.biswa1045.akash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates


class TotalActivity : AppCompatActivity() {

    var ProductKeys = ArrayList<String>()
    private var productqnt = ArrayList<String>()
    private var ExpAmtArray = ArrayList<String>()
    private var ExpNameArray = ArrayList<String>()
    lateinit var phonenumber:String
    lateinit var invoice:String
    lateinit var discount:String
    lateinit var total:String
     var total_f:Float = 0.0f
    var productstuck by Delegates.notNull<Float>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total)
        total = intent.extras!!.getString("total").toString()
        ProductKeys  = intent.extras!!.get("ProductKey") as ArrayList<String>
        productqnt  = intent.extras!!.get("productqnt") as ArrayList<String>
        ExpAmtArray = intent.extras!!.get("ExpAmtArray") as ArrayList<String>
        ExpNameArray = intent.extras!!.get("ExpNameArray") as ArrayList<String>
        invoice = intent.extras!!.getString("invoice").toString()
        discount = intent.extras!!.getString("discount","0").toString()
        phonenumber=intent.extras!!.getString("phonenumber").toString()
        // create bill no
        val dis: Float = (total.toFloat() * discount.toFloat()) / 100
        total_f = total.toFloat() - dis

        val msg = StringBuilder()
        msg.append("Patra Enterprise   "+invoice+"\n")
        for (q in 0 until ExpNameArray.size){
            val txt = ExpNameArray.get(q)+"....."+(ExpAmtArray.get(q).toFloat()/productqnt.get(q).toFloat())+"*"+productqnt.get(q)+"="+ExpAmtArray.get(q)+"\n"
            msg.append(txt)
        }
        msg.append("discount "+discount+"%"+"\n")
        msg.append("Total= "+total_f+"/-"+"\n")
        msg.append("For any query contact on 9668532442")




        //product stock deduct
        for( l in 0 until  ProductKeys.size)
        {
            FirebaseDatabase.getInstance().reference.child("products").child(ProductKeys.get(l)).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            productstuck = dataSnapshot.child("productstuck").value.toString().toFloat()

                        }

                        dataSnapshot.ref.child("productstuck").setValue((productstuck-(productqnt.get(l)).toFloat()).toString())

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
            )
        }

        findViewById<Button>(R.id.gohome).isEnabled=true
        findViewById<TextView>(R.id.t).text= total_f.toString()
        findViewById<TextView>(R.id.td).text= discount.toString()+"% off of "+total.toString()
        findViewById<Button>(R.id.gohome).setOnClickListener{
                try{
                    val smsUri: Uri = Uri.parse("smsto:" + phonenumber)
                    val intent = Intent(Intent.ACTION_VIEW, smsUri)
                    intent.putExtra("sms_body",msg.toString() )
                    startActivity(intent)
                }catch (e:Exception){
                    Toast.makeText(this@TotalActivity,
                        "$e",
                        Toast.LENGTH_SHORT).show()

                }
            findViewById<Button>(R.id.gohome).isEnabled=false
        }
    }

    fun gohome() {
        val t = Intent(this@TotalActivity,HomeActivity::class.java)
        startActivity(t)

    }
    override fun onBackPressed() {
        gohome()

    }

   }
package com.biswa1045.akash

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class AddProductActivity : AppCompatActivity() {
    lateinit var databaseReference:DatabaseReference
    lateinit var del:DatabaseReference
    var img_uri: Uri? =null
    lateinit var img:ImageView
    lateinit var dialog: Dialog
    lateinit var progressBar: ProgressBar
    lateinit var storageReference: StorageReference
    lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
//    lateinit var list: ArrayList<product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        storageReference = FirebaseStorage.getInstance().getReference("images").child("products")
        progressBar = findViewById(R.id.progressbar_addproduct)
        dialog = Dialog(this)
        findViewById<LinearLayout>(R.id.add_product).setOnClickListener{
        val intent =Intent(this@AddProductActivity,AddActivity::class.java)
        startActivity(intent)
            finish()
        // showpopup()
        }
        recyclerView = findViewById(R.id.recycle_product)
        recyclerView.layoutManager = LinearLayoutManager(this)

    val options: FirebaseRecyclerOptions<product> = FirebaseRecyclerOptions.Builder<product>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("products"), product::class.java)
            .build()

    myAdapter = MyAdapter(options)
    recyclerView.adapter = myAdapter
    progressBar.setVisibility(View.GONE)
    myAdapter.notifyDataSetChanged()
    findViewById<TextView>(R.id.clear_temp_file).setOnClickListener{
        del = FirebaseDatabase.getInstance().reference.child("temp_bills")
        if(del!=null){
            del.removeValue()
        }


    }
    }
    fun showpopup() {
        dialog.setContentView(R.layout.add_product_dialog)
        val productname:EditText
        val productprice:EditText
      //  val img:ImageView
        val done:Button
        done = dialog.findViewById(R.id.product_add_dialog)
        productname = dialog.findViewById(R.id.product_name)
        productprice = dialog.findViewById(R.id.prodduct_price)
        img = dialog.findViewById(R.id.product_img)
        img.setOnClickListener{
            SelectImage()
        }
        done.setOnClickListener(){

            val productname: String =  productname.text.toString()
            val productprice: String = productprice.text.toString()


            if (TextUtils.isEmpty(productname) && TextUtils.isEmpty(productprice) && img_uri!=null) {

                Toast.makeText(this@AddProductActivity, "Please add some data.", Toast.LENGTH_SHORT)
                    .show()
            } else {
               uploadimage(productname,productprice)

            }
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun SelectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."),
            100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.data != null) {
            img_uri = data.data!!
            img.setImageURI(img_uri)
        }
    }

    private fun getfileExt(imageuri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri))
    }

    private fun uploadimage(productname:String,productprice:String) {
      //  progressBar.setVisibility(View.VISIBLE)
        if (img_uri != null) {

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            val ref: StorageReference =
                storageReference.child(System.currentTimeMillis().toString() + "." + getfileExt(
                    img_uri!!))
            ref.putFile(img_uri!!)
                .addOnSuccessListener(OnSuccessListener<Any?> {
                    //extract image uri
                    ref.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        var firebase_img_uri = uri.toString()
                        val id: String? =FirebaseDatabase.getInstance().reference.child("products").push().key
                        databaseReference = FirebaseDatabase.getInstance().reference.child("products").child(id!!)
                        val data: MutableMap<String, Any> = HashMap()
                        data["img"] = firebase_img_uri
                        data["productname"] = productname
                        data["productprice"] = productprice
                        databaseReference.setValue(data).addOnCompleteListener {
                            Toast.makeText(this@AddProductActivity, "data added", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener {
                            Toast.makeText(this@AddProductActivity, "Fail to add data $it", Toast.LENGTH_SHORT)
                                .show()
                        }

                    //    progressBar.setVisibility(View.INVISIBLE)
                        progressDialog.dismiss()
                    })

                }).addOnFailureListener(OnFailureListener {
                    Toast.makeText(applicationContext, "network error", Toast.LENGTH_SHORT).show()
                  //  progressBar.setVisibility(View.INVISIBLE)
                    progressDialog.dismiss()
                }).addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                    override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                        val progress: Double = (100.0
                                * snapshot.getBytesTransferred()
                                / snapshot.getTotalByteCount())
                        progressDialog.setMessage(
                            "Uploaded "
                                    + progress + "%")
                    }
                })
        } else {
            Toast.makeText(applicationContext, "Uri not found", Toast.LENGTH_SHORT).show()
                //  progressBar.setVisibility(View.INVISIBLE)
        }
    }
    override fun onStart() {
        super.onStart()
        myAdapter.startListening()

    }
    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
        finish()
    }
/* database = FirebaseDatabase.getInstance().reference.child("products")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
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
        })*/
override fun onBackPressed() {

    val intent = Intent(this@AddProductActivity,HomeActivity::class.java)
    startActivity(intent)
    finish()
}
}
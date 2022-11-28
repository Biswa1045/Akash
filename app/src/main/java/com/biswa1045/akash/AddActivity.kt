package com.biswa1045.akash

import android.app.Dialog
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddActivity : AppCompatActivity() {
    lateinit var databaseReference: DatabaseReference
    var img_uri: Uri? =null
    lateinit var img: ImageView
    lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        storageReference = FirebaseStorage.getInstance().getReference("images").child("products")

        val productname: EditText
        val productprice: EditText
        val productstuck: EditText
        val done: Button
        done = findViewById(R.id.product_add_dialog)
        productname = findViewById(R.id.product_name)
        productprice = findViewById(R.id.prodduct_price)
        productstuck = findViewById(R.id.prodduct_total)
        img = findViewById(R.id.product_img)
        img.setOnClickListener{
            SelectImage()
        }
        done.setOnClickListener(){

            val productname: String =  productname.text.toString()
            val productprice: String = productprice.text.toString()
            val productstuck: String = productstuck.text.toString()


            if (TextUtils.isEmpty(productname) && TextUtils.isEmpty(productprice) && img_uri!=null) {

                Toast.makeText(this@AddActivity, "Please add some data.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                uploadimage(productname,productprice,productstuck)

            }
        }
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

    private fun uploadimage(productname:String,productprice:String,productstuck:String) {
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
                        val id: String? =
                            FirebaseDatabase.getInstance().reference.child("products").push().key
                        databaseReference = FirebaseDatabase.getInstance().reference.child("products").child(id!!)
                        val data: MutableMap<String, Any> = HashMap()
                        data["img"] = firebase_img_uri
                        data["productname"] = productname
                        data["productprice"] = productprice
                        data["productstuck"] = productstuck
                        databaseReference.setValue(data).addOnCompleteListener {
                            Toast.makeText(this@AddActivity, "data added", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener {
                            Toast.makeText(this@AddActivity, "Fail to add data $it", Toast.LENGTH_SHORT)
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

    override fun onBackPressed() {
        val intent = Intent(this@AddActivity,AddProductActivity::class.java)
        startActivity(intent)
        finish()
    }
}
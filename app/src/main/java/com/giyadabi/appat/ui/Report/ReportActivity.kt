package com.giyadabi.appat.ui.Report

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.giyadabi.appat.NavigationActivity
import com.giyadabi.appat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.nav_header_navigation.*
import java.io.IOException
import java.util.*

class ReportActivity : AppCompatActivity(){

    private lateinit var database: DatabaseReference


    companion object{
        val TAG = "ReportActivity"
    }

    private lateinit var date:String
    private lateinit var location:String
    private lateinit var description:String

    //buat id disini untuk  simpan nilai id pas push ke tabel data
    // dua stelah itu saat simpan ke laporan user
    // ref.child("laporanuser").child("id") | saya coba step by step dulu kak

    private var id : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)




        button_select_photo_report.setOnClickListener {
            Log.d(TAG,"try to take report photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,2)
        }

        btn_upload_laporan.setOnClickListener{
            performReport()
        }



    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uid = FirebaseAuth.getInstance().uid
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG,"Photo was selected, your uid is :$uid")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            select_imageview_photo.setImageBitmap(bitmap)
            button_select_photo_report.alpha = 0f

        }
    }

    private fun performReport() {
        val description = edit_text_input_description.text.toString()

        if (description.isEmpty()) {
            Toast.makeText(this,"anda harus mengisi deskripsi",Toast.LENGTH_SHORT).show()
            return
        }

        uploadImageToFirebaseStorage()
        progressBarReport.setVisibility(View.VISIBLE)
        Toast.makeText(this,"sedang mengupload laporan, mohon menunggu beberapa menit",Toast.LENGTH_SHORT).show()
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d("ReportActivity","Succesfully upload image : ${it.metadata}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("ReportActivity","File Location: $it")

//                savedataToAdminFirebase(it.toString())
                saveDataToFirebaseDatabase(it.toString())
            }
        }
            .addOnFailureListener{

            }
    }

//    ini untuk tabel laporan user kak
    private fun savedataToAdminFirebase(reportImageUrl: String, id: String?) {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("APPAT/laporan_user")
        var rate = findViewById<View>(R.id.ratingBar) as RatingBar
        var nilai = rate.rating.toString()
        val verification: Boolean = false
        date = editText_date.text.toString()
        location = editText_location.text.toString()
        description = edit_text_input_description.text.toString()
        val data = Report(id,uid,reportImageUrl,nilai,location,date,description,verification)
        ref.child(id!!).setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this,"laporan telah diterima di admin",Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent(this,ResultReportActivity::class.java))
                startActivity(intent)
            }
    }

//    ini untuk tabel data
    private fun saveDataToFirebaseDatabase(reportImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("APPAT/data/$uid")
        var rate = findViewById<View>(R.id.ratingBar) as RatingBar
        var nilai = rate.rating.toString()
        val verification: Boolean = false
        id = ref.push().getKey()
        date = editText_date.text.toString()
        location = editText_location.text.toString()
        description = edit_text_input_description.text.toString()
        val data = Report(id,uid,reportImageUrl,nilai,location,date,description,verification)
        ref.child(id!!).setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this,"laporan telah diterima",Toast.LENGTH_SHORT).show()
                savedataToAdminFirebase(reportImageUrl, id)
            }
    }

// ini halaman report activity
}


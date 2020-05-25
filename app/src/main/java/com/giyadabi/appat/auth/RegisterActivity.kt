package com.giyadabi.appat.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.giyadabi.appat.NavigationActivity
import com.giyadabi.appat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_haveaccount_textView.setOnClickListener{
            Log.d(TAG, "Try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_buttonregister.setOnClickListener{
            Log.d("RegisterActivity","try to show photo selected")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//            proceed an check the image
            Log.d("RegisterActivity","Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)
            select_photo_buttonregister.alpha = 0f

//            val bitmapDrawable = BitmapDrawable(bitmap)
//            select_photo_buttonregister.setBackgroundDrawable(bitmapDrawable)
        }
    }



    private fun performRegister() {
        val email = email_editText_register.text.toString()
        val password = password_editText_register.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }
        //        Authentication with firebase

        progressBar_register.setVisibility(View.VISIBLE)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
//                    else if successfull
                Log.d("Main", "succes, yout uid is : ${it.result?.user?.uid}")

                val user = FirebaseAuth.getInstance().currentUser
                user?.sendEmailVerification()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Verification","Your email has been sent the verification")
                        Toast.makeText(this, "Please use the valid email", Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage()
//                        val intent = Intent(Intent(this,
//                            LoginActivity::class.java))
                        progressBar_register.setVisibility(View.INVISIBLE)
//                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Please use the valid email", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            .addOnFailureListener{
                Log.d("Main", "failed to create user: ${it.message}")
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
                Log.d("Register","Succesfully uploaded image : ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
        }
            .addOnFailureListener{

            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val email:String = email_editText_register.text.toString().trim()
        val ref = FirebaseDatabase.getInstance().getReference("APPAT/member/$uid")

        val user = User(
            uid,
            usernama_editText_register.text.toString(),
            profileImageUrl,
            email
        )
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this,NavigationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Log.d("RegisterActivity","Finally we save user in realtime database with $uid")
            }
    }

}

//@Parcelize
class User(
    val uid: String,
    val username: String,
    val profileImageUrl: String,
    val email:String)
//    :Parcelable
//{
//    constructor(): this("","","","")
//}

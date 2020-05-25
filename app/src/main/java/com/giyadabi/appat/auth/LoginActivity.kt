package com.giyadabi.appat.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.giyadabi.appat.NavigationActivity
import com.giyadabi.appat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

//    val user: FirebaseAuth = FirebaseAuth.getInstance()
private var mBackPressed: Int = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            performLogin()
        }

        back_to_register_textview.setOnClickListener{
            val intent = Intent(Intent(this, RegisterActivity::class.java))
            startActivity(intent)
        }


//        if (users != null) {
//            val intent = Intent(Intent(this, NavigationActivity::class.java))
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//        } else{
//
//        }
    }

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"please fill out email/password", Toast.LENGTH_SHORT).show()
            return
        }

//        if(email.isEx)

        progressBar_login.setVisibility(View.VISIBLE)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener
                val intent = Intent(Intent(this, NavigationActivity::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                progressBar_login.setVisibility(View.INVISIBLE)
//                finish()
                Log.d("Login", "Succesfully login in : ${it.result?.user?.uid}")

            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    val users = FirebaseAuth.getInstance().currentUser
    override fun onStart() {
        super.onStart()
//        if (users != null) {
//            val intent = Intent(Intent(this, NavigationActivity::class.java))
//            startActivity(intent)
//        } else{
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}

package com.giyadabi.appat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide.with
import com.giyadabi.appat.auth.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_about_me.*

class AboutMeActivity : AppCompatActivity() {
    private val TAG = "AboutMeActivity"
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val uid = FirebaseAuth.getInstance().uid?: ""
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("APPAT/member/$uid")

        refUsers!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val aboutMe: AboutMe? = p0.getValue(AboutMe::class.java)

                    textView_email_aboutme.text = aboutMe!!.getEmail()
                    textView_username_about_me.text = aboutMe.getusername()
//                    textView_email_aboutme.text = aboutMe.

//                    Picasso.get().load(aboutMe?.getProfilImageUrl()).into(imageView_aboutme)
                    Picasso.get().load(aboutMe.getProfileImageUrl()).placeholder(R.drawable.appatuser).into(imageView_aboutme)
                    Log.d("this","uid kamu adalah $uid")
                    progressBar_aboutme.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}

class AboutMe{
    private var email: String = ""
    private var profileImageUrl: String = ""
    private var uid: String = ""
    private var username: String = ""

    constructor()

    constructor(email: String, profileImageUrl: String, uid: String, username: String) {
        this.email = email
        this.profileImageUrl = profileImageUrl
        this.uid = uid
        this.username = username
    }

    fun getUID(): String?{
        return uid
    }

    fun setUID(uid: String) {
        this.uid = uid
    }

    fun getEmail(): String?{
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getusername(): String?{
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getProfileImageUrl(): String?{
        return profileImageUrl
    }

    fun setProfileImageUrl(profileImageUrl: String) {
        this.profileImageUrl = profileImageUrl
    }
}

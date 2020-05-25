package com.giyadabi.appat.ui.Report

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.giyadabi.appat.NavigationActivity
import com.giyadabi.appat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_result_report.*
import kotlinx.android.synthetic.main.item_row_report.view.*

class ResultReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_report)

        supportActionBar?.title="Report Cord"

//        val adapter = GroupAdapter<ViewHolder>()
//
////        adapter.add(UserItem())
////        adapter.add(UserItem())
////        adapter.add(UserItem())
//
//
//        rv_newreport.adapter = adapter

        fetchReports()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_result, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_back_to_dashboard -> {
                val intent = Intent(this,NavigationActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun fetchReports() {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference().child("APPAT").child("data").child("$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                progressbar_result.setVisibility(View.VISIBLE)
                p0.children.forEach {
                    Log.d("ResultReport",it.toString())
                    val report = it.getValue(Report::class.java)
                    if (report != null) {
                        adapter.add(UserItem(report))
                    }
                }
                rv_newreport.adapter = adapter
                progressbar_result.setVisibility(View.INVISIBLE)
                Log.d("ResultReport", "ini nilai $ref")
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    class UserItem(val report: Report) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textview_location_result.text = report.location
            viewHolder.itemView.textview_description_result.text = report.description
            viewHolder.itemView.textview_rating_result.text = report.rating

            Picasso.get().load(report.reportImageUrl).into(viewHolder.itemView.imageview_result_report)
        }
        override fun getLayout(): Int {
            return R.layout.item_row_report
        }

    }

}

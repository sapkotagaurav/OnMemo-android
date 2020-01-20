package com.gaurav.onmemo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*


class Main2Activity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mData: DatabaseReference
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mAuth = FirebaseAuth.getInstance()
        mData = FirebaseDatabase.getInstance().reference
        user = mAuth.currentUser!!
        var photo = user?.photoUrl
        Glide.with(this).load(photo).circleCrop().into(img)
        adddatatoview()
        logout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }


    fun showtexts(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun adddatatoview() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    var stamp = it.child("date").value.toString().toLong()
                    var title = it.child("title").value.toString()
                    var post = it.child("post").value.toString()
                    var date = Date(stamp)
                    addviews(title, post, date)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        }
        mData.child("posts/${user.uid}").addValueEventListener(postListener)
    }

    private fun addviews(title: String, post: String, date: Date) {
        var cardView = CardView(this)
        var linearLayout1 = LinearLayout(this)
        linearLayout1.orientation =LinearLayout.VERTICAL
        val params1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,50)
        cardView.layoutParams=params1
        cardView.setContentPadding(10,10,10,10)
        cardView.setCardBackgroundColor(Color.WHITE)
        var tv = TextView(this)
        tv.layoutParams = params
        tv.text = title
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        tv.setTextColor(Color.BLACK)
        var pv = TextView(this)
        pv.layoutParams = params
        pv.text = post
        pv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        pv.setTextColor(Color.GRAY)
        var dv = TextView(this)
        dv.layoutParams = params
        dv.text = date.toString()
        dv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
        dv.setTextColor(Color.BLUE)
        linearLayout1.addView(tv)
        linearLayout1.addView(pv)
        linearLayout1.addView(dv)
        cardView.addView(linearLayout1)
        cardView.setOnClickListener { showdialogb(title,post,date) }
        linearlayout.addView(cardView)
    }

    private fun showdialogb(title: String, post: String, date: Date) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(title)
        //set message for alert dialog
        builder.setMessage(post)
        builder.setIcon(R.drawable.logo)

        builder.setNeutralButton("Ok"){dialogInterface , which ->
dialogInterface.dismiss()
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}

package com.gaurav.onmemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth= FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signup.setOnClickListener {
            var signinact = Intent(this, MainActivity::class.java)
            startActivity(signinact)
            overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoright);
            finish()
        }
        passwordsignup.setOnKeyListener{v,keyCode,event->
           if (keyCode==66) {
               createuser(signupname.text.toString(),emailsignup.text.toString(),passwordsignup.toString())
               true
           }else{
               false
           }
        }
    }

    private fun createuser(name: String, email: String, password: String) {
mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
    if (it.isSuccessful){
        val user=mAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        user?.updateProfile(profileUpdates)
        var signinact = Intent(this, MainActivity::class.java)
        startActivity(signinact)
        overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoright);
        finish()
    }else{
        var signinact = Intent(this, MainActivity::class.java)
        startActivity(signinact)
        overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoright);
        finish()
    }
}
    }

    override fun onBackPressed() {
        var signinact = Intent(this, MainActivity::class.java)
        startActivity(signinact)
        overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoright);
        finish()
        super.onBackPressed()
    }
    
    fun showtexts(context: Context,text:String){
        Toast.makeText(context,text, Toast.LENGTH_LONG).show()
    }
}

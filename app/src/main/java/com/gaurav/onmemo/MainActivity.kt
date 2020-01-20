package com.gaurav.onmemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        var googleSignInOptions =
            GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        passwordsignin.setOnKeyListener { v, keyCode, event ->
            if (keyCode == 66) {

                signin(emailsignin.text.toString(), passwordsignin.text.toString())
                return@setOnKeyListener true
            }
            false

        }
        signin.setOnClickListener {
            val signupact = Intent(this, Signup::class.java)
            startActivity(signupact)
            finish()
        }
        googlesignin.setOnClickListener {
            val signinintent=googleSignInClient.signInIntent
            startActivityForResult(signinintent, RC_SIGNIN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode== RC_SIGNIN){
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
val account = task.getResult(ApiException::class.java)
                firebasewithgoogle(account)
            }catch (e:ApiException){
showtexts(this,e.toString())
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebasewithgoogle(account: GoogleSignInAccount?) {
val cred = GoogleAuthProvider.getCredential(account?.idToken,null)
        mAuth.signInWithCredential(cred).addOnCompleteListener {
            if (it.isSuccessful){
                startActivity(Intent(this, Main2Activity::class.java))
                finish()
            }else{
                showtexts(this,"Failed")
            }
        }
    }

    fun showtexts(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun signin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                startActivity(Intent(this, Main2Activity::class.java))

            } else {
                showtexts(this, it.exception.toString())
            }
        }


    }

    override fun onStart() {
        var user = mAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, Main2Activity::class.java))
        }
        super.onStart()
    }
    companion object {
        private const val RC_SIGNIN =9001
        private const val TAG = "GoogleActivity"
    }
}




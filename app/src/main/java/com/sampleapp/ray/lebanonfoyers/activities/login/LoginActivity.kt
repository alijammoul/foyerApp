package com.sampleapp.ray.lebanonfoyers.activities.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sampleapp.ray.lebanonfoyers.R
import com.sampleapp.ray.lebanonfoyers.activities.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*


class LoginActivity : AppCompatActivity() {
    private var isInLoginMode = true // check current mode status
    private lateinit var firebaseAuthentication: FirebaseAuth // fix lateinit later
    private var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuthentication = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = firebaseAuthentication.currentUser


    }
    //called when textView is clicked
    fun onClick(view: View) {

        if (isInLoginMode) {
            fullname.visibility = View.VISIBLE
            phonenumber.visibility = View.VISIBLE
            confirmpassword.visibility = View.VISIBLE
            login_signup.text = "SignUp"
            modechange.text = "Login"
            isInLoginMode = false

        } else {
            fullname.visibility = View.GONE
            phonenumber.visibility = View.GONE
            confirmpassword.visibility = View.GONE
            login_signup.text = "Login"
            modechange.text = "SignUp"
            isInLoginMode = true
        }

    }
        // called when button is clicked
    fun onLoginSignup(view: View) {
        if (isInLoginMode) {
            val email = view.email.text.toString()
            val password = view.password.text.toString()

            if((email.length==0) or (password.length==0)){
                Toast.makeText(this, "Enter email and Password", Toast.LENGTH_SHORT).show()
            }else{
                signinUser(email ,password)
            }

        } else {
            //signup successful
        }
    }

    private fun signinUser(email: String, password: String) {
        firebaseAuthentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = firebaseAuthentication.getCurrentUser()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed",Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun createUser(email: String, password: String) {
        firebaseAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = firebaseAuthentication.getCurrentUser()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed",Toast.LENGTH_SHORT).show()
                    }

                })
    }
}

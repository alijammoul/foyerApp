package com.sampleapp.ray.lebanonfoyers.activities.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
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
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sampleapp.ray.lebanonfoyers.R.id.email
import com.sampleapp.ray.lebanonfoyers.models.User


class LoginActivity : AppCompatActivity() {
    private var isInLoginMode = true // check current mode status
    private lateinit var firebaseAuthentication: FirebaseAuth // fix lateinit later
    private lateinit var firestoreDatabase:FirebaseFirestore
    private var firebaseUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuthentication = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = firebaseAuthentication.currentUser

        if (firebaseUser != null) {
            val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }
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
            val semail = email.text
            val spassword = password.text
            val sname = fullname.text
            val sphoneNumber = phonenumber.text
            val sconfirmPassword = confirmpassword.text
        if (isInLoginMode) {

            if((semail.toString().length==0) or (spassword.toString().length==0)){
                Toast.makeText(this, "Enter email and Password", Toast.LENGTH_SHORT).show()
            }else{
                signinUser(semail.toString().trim() ,spassword.toString().trim())
            }

        } else {
            //signup successful
            val passwordsMatch= passwordsMatch(spassword.toString().trim(),sconfirmPassword.toString().trim())
            val fieldsAreNotEmpty=fieldsAreNotEmpty(sname,semail,spassword,sconfirmPassword,sphoneNumber)
                    if(fieldsAreNotEmpty) {
                        if (!passwordsMatch) {
                            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                        }else{
                            createUser(semail.toString(),spassword.toString())
                            createFireStoreUser(sname.toString(),semail.toString(),Integer.parseInt(sphoneNumber.toString()))
                        }
                    }else{
                        Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
                    }

        }
    }

    private fun createFireStoreUser(name : String ,email:String,phoneNumber : Int) {

        val id = firebaseAuthentication.currentUser?.uid
        val user = id?.let { User(it,name,email,phoneNumber) }

        firestoreDatabase = FirebaseFirestore.getInstance()
        user?.let {
            firestoreDatabase.collection("users") .add(it)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Welcome ${user.fullName}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()

                }
        }

    }

    private fun fieldsAreNotEmpty(name: Editable, email: Editable, password: Editable, confirmPassword: Editable, phoneNumber: Editable): Boolean {
        return !((email.isEmpty()) or (password.isEmpty()) or (name.isEmpty()) or (confirmPassword.isEmpty()) or (phoneNumber.isEmpty()))
    }

    private fun passwordsMatch(password: String, confirmPassword: String): Boolean {
        if(password.equals(confirmPassword))
            return true
        return false
    }

    private fun signinUser(email: String, password: String) {
        firebaseAuthentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
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
                }
    }

    private fun createUser(email: String, password: String) {
        firebaseAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        firebaseUser = firebaseAuthentication.getCurrentUser()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Failed to create user",Toast.LENGTH_SHORT).show()
                    }

                })
    }
}

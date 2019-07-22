package com.divinegrace.machat.registerlogin

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.app.AppCompatActivit
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.divinegrace.machat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        ///Login button onclick Listnerer
        login_button_login.setOnClickListener {
            val email=email_edittext_login.text.toString()
            val password=password_edittext_login.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful

                } ///unsuccesful wil rresult into code below run
                .addOnFailureListener{
                    Toast.makeText(this,"Failed to Login  : ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


        //return back to register action button
        back_to_register_textView.setOnClickListener{
           finish ()
        }


    }




}
package com.divinegrace.machat.messages

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.divinegrace.machat.R
import com.divinegrace.machat.registerlogin.RegisterActivity
//import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LatestMessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        verifyUserIsLoggedIn()

    }

    private fun verifyUserIsLoggedIn(){
        //Checking if user is Logged in
        val uid = FirebaseAuth.getInstance().uid
        if (uid==null){
            val intent= Intent(this, RegisterActivity::class.java)
            //clearing views
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

//logout menue handles
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
       when(item?.itemId){
           R.id.menu_new_message ->{
               val intent =Intent(this, NewMessageActivity::class.java)
               startActivity(intent)

           }
           R.id.menu_sign_out ->{
               FirebaseAuth.getInstance().signOut()
               val intent= Intent(this, RegisterActivity::class.java)
               //clearing views
               intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(intent)
           }
       }

        return super.onOptionsItemSelected(item)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }
}

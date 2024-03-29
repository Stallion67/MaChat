package com.divinegrace.machat.messages

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.divinegrace.machat.R
import com.divinegrace.machat.registerlogin.RegisterActivity
import com.divinegrace.machat.registerlogin.User


//import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LatestMessagesActivity : AppCompatActivity() {

    //definig a global variable
    companion object{
        var currentUser: User? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        fetchcurrentuser()
        verifyUserIsLoggedIn()

    }

    private fun fetchcurrentuser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref =FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                currentUser=p0.getValue(User::class.java)
                Log.d("latests message","Current User ${currentUser?.username}")

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
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

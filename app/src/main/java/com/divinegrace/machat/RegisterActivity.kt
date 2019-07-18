package com.divinegrace.machat

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import com.google.firebase.storage.StorageReference




class RegisterActivity : AppCompatActivity() {
    //firebase declrattion
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAuth: FirebaseAuth? = null
    private var mStorageRef: StorageReference? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
        //firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference();
        
        setContentView(R.layout.activity_register)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //register button click event
        register_button_register.setOnClickListener {
           performRegister()

        }


        //Already have accoun redirect
        already_have_account_text_view.setOnClickListener {
                //launching the login activity
            val intent =Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //select photo
         selectphoto_button_register.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
             intent.type="image/*"
             startActivityForResult(intent,0)
         }

    }

    //variable for image
    var selectedPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode==0 && resultCode==Activity.RESULT_OK && data !=null){
            //proceed and check what the selected image was....

           selectedPhotoUri=data.data
            val bitmap =MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

             //selected photo data
            selectphoto_imageview_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha=0f
      //      val bitmapDrawable=BitmapDrawable(bitmap)
        //    selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }


    //function to regiser a user
 private fun performRegister(){
     //acccesing fields
     val email=email_edittext_register.text.toString()
     val password=password_edittext_register.text.toString()
     // checking if user provided infomation in the fields
     if(email.isEmpty()|| password.isEmpty()) {
         Toast.makeText(this,"Please enter text in email and password",Toast.LENGTH_SHORT).show()
         return
     }


     //Firebase authentication and create user with email and password
     // Initialize Firebase Auth
     FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener {
             if (!it.isSuccessful) return@addOnCompleteListener
             // if successful

             //Upload image
             uploadImageToFirebaseStorage( )

         } ///unsuccesful wil rresult into code below run
         .addOnFailureListener{
             Toast.makeText(this,"Failed to create user : ${it.message}",Toast.LENGTH_SHORT).show()
         }



 }

    private  fun uploadImageToFirebaseStorage(){
         if (selectedPhotoUri==null) return

        val filename = UUID.randomUUID().toString()
       val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

          ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener{

           ref.downloadUrl.addOnSuccessListener {
              // it.toString() //converting url of image to string
                saveUserToFirebaseDatabse(it.toString())
               }
            }
              .addOnFailureListener{
                  // do some logging here
              }

    }

    private fun saveUserToFirebaseDatabse(profileImageurl: String){
        val uid = FirebaseAuth.getInstance().uid?:""
       val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")

     val user=User(uid,username_edittext_register.text.toString(),profileImageurl )

        ref.setValue(user)
            .addOnSuccessListener {
                //loaiding inten
                val intent=Intent(this,LatestMessagesActivity::class.java)
                    //clearing views
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }
    }

}

// new class to define user in app

class User (val uid:String, val username:String, val profileImageurl:String){
    constructor(): this("","","")
}

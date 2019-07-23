package com.divinegrace.machat.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.divinegrace.machat.R
import com.divinegrace.machat.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.text.FieldPosition
import kotlin.coroutines.coroutineContext
import com.divinegrace.machat.registerlogin.User as User

class ChatLogActivity : AppCompatActivity() {
 companion object{
     val TAG ="ChatLog"
 }
 val adapter=GroupAdapter<ViewHolder>()
 var toUser: User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //displaying the new message
        recyclerview_chat_log.adapter=adapter

        //supportActionBar?.title="Chat Log"


        //toUser= intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title= toUser?.username

      // setUpDummyData()
        listenForMessages()

     send_button_chat_log.setOnClickListener {
          Log.d(TAG, "ATTEMPT TO SENE MESSAGE")
         performSendMessage()
     }
    }

    private  fun listenForMessages(){
        val ref=FirebaseDatabase.getInstance().getReference("/messages")
    ref.addChildEventListener(object:ChildEventListener{
        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
          val chatMessage=p0.getValue(ChatMessage::class.java)

            if (chatMessage !=null){
                Log.d(TAG,chatMessage?.text)

                if (chatMessage.fromId ==FirebaseAuth.getInstance().uid ){
                   val currentUser=LatestMessagesActivity.currentUser
                    adapter.add(ChatFromItem(chatMessage.text,currentUser!! ))
                }
                 else {
                    adapter.add(ChatToItem(chatMessage.text, toUser!!))
                }
            }

        }

        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }
    })

    }


    private  fun performSendMessage(){
        // send a message to firebase database
   val text=   edittext_chat_log.text.toString()

      val fromId=FirebaseAuth.getInstance().uid


      // val user =  intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title= toUser?.username
        val toId = toUser?.uid.toString()


        if (fromId==null) return

        val reference =FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage = ChatMessage(reference.key!!,text, fromId,toId,System.currentTimeMillis()/1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our Chat message:${reference.key}")
            }

    }

}

class ChatFromItem(val text:String,val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
 viewHolder.itemView.textView_from_row.text= text
        //loading chat user image
        val uri = user.profileImageurl
        val targetImageView=viewHolder.itemView.imageView_chat_from_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }
}

class ChatToItem(val text:String,val user:User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, posittoion: Int) {
        viewHolder.itemView.textView_to_row.text= text

        //loading chat user image
        val uri = user.profileImageurl
        val targetImageView=viewHolder.itemView.imageView_chat_to_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}


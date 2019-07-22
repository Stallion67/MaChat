package com.divinegrace.machat.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.divinegrace.machat.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_new_message.*
import java.text.FieldPosition

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //supportActionBar?.title="Chat Log"

       val username= intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title=username


       val adapter = GroupAdapter<ViewHolder>()

       adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatToItem())

        recyclerview_chat_log.adapter=adapter
    }
}

class ChatFromItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }
}

class ChatToItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}


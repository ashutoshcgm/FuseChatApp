package com.example.fusechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messsageBox:EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<Message>

    private lateinit var mdBRef:DatabaseReference

    var recieverRoom:String?=null
    var senderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name=intent.getStringExtra("name")


        val recieverUid=intent.getStringExtra("uid")

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid

        mdBRef=FirebaseDatabase.getInstance().getReference()

        senderRoom=recieverUid + senderUid
        recieverRoom=senderUid + recieverUid

        supportActionBar?.title=name


        chatRecyclerView=findViewById(R.id.chatRecyclerView)
        messsageBox=findViewById(R.id.messageBox)
        sendButton=findViewById(R.id.chatsent)
        messageList=ArrayList()
        messageAdapter=MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        //logic for adding data to recycler view
        mdBRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        sendButton.setOnClickListener{
            val message=messsageBox.text.toString()
            val messageObject=Message(message,senderUid)

            mdBRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mdBRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            messsageBox.setText("")
        }


    }
}
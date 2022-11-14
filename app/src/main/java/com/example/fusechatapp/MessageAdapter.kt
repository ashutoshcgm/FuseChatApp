package com.example.fusechatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messageList:ArrayList<Message>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    val ITEM_Recieve=1
    val ITEM_Sent=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType==1){
            // inflate recieve
            val view:View = LayoutInflater.from(context)
                .inflate(R.layout.recieve, parent, false)
            return recieveViewHolder(view)
        }else{
            // inflate send
            val view:View = LayoutInflater.from(context)
                .inflate(R.layout.send, parent, false)
            return sentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage=messageList[position]

        if (holder.javaClass==sentViewHolder::class.java){
            //do the stuff for sent view holder

            val viewHolder=holder as sentViewHolder
            holder.sentMessage.text=currentMessage.message
        }
        else{
            // do the stuff for receive view holder
            val viewHolder=holder as recieveViewHolder
            holder.recieveMessage.text=currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_Sent
        }
        else{
            return ITEM_Recieve
        }
    }

    class sentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.txt_sent_message)

    }
    class recieveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val recieveMessage=itemView.findViewById<TextView>(R.id.txt_recieve_message)
    }

}
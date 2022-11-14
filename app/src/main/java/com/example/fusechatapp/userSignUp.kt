package com.example.fusechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.ActionCodeEmailInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class userSignUp : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        supportActionBar?.hide()

        backButton=findViewById(R.id.back)
        edtName=findViewById(R.id.edt_name)
        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnSignup=findViewById(R.id.signup)
        mAuth=FirebaseAuth.getInstance()

        backButton.setOnClickListener{
            val intent=Intent(this,Login::class.java)
            startActivity(intent)
        }
        btnSignup.setOnClickListener {
            if (TextUtils.isEmpty(edtEmail.text.toString()) or TextUtils.isEmpty(edtName.text.toString()) or TextUtils.isEmpty(edtPassword.text.toString())){
                Toast.makeText(this, "Please Enter Details ", Toast.LENGTH_SHORT).show()
            }
            else {
                val name=edtName.text.toString()
                val email=edtEmail.text.toString()
                val password=edtPassword.text.toString()
                signupUser(name,email,password)
            }
        }

    }
    private fun signupUser(name:String,email:String,password:String){
//        logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    code for jumping to home activity
                    //Add user to database
                    addUserToDataBase(name,email,mAuth.currentUser?.uid!!)
                    val intent=Intent(this@userSignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@userSignUp,"Signing Failed ",Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@userSignUp,"Try With Bigger Passcode and Correct Email",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDataBase(name: String,email: String,uid:String){
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }

}
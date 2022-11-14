package com.example.fusechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup:Button
    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth=FirebaseAuth.getInstance()


        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnLogin=findViewById(R.id.login)
        btnSignup=findViewById(R.id.signup)

        btnSignup.setOnClickListener{
            val intent=Intent(this,userSignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(edtEmail.text.toString()) or  TextUtils.isEmpty(edtPassword.text.toString())) {
                Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show()
            } else {
                val email=edtEmail.text.toString()
                val password=edtPassword.text.toString()
                loginUser(email,password)
            }
        }

    }
    private fun loginUser(email:String, password:String){
//        logic for login user

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent=Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(this@Login, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
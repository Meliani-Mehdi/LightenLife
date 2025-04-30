package com.app.lightenlife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    private lateinit var authManager: AuthenticationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.doctorloginpage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userManager = UserManager()
        authManager = AuthenticationManager(userManager)
    }

    fun goNext(){
        startActivity(Intent(this, Nav_activity::class.java))
        finish()
    }

    fun goSignUpC(v: View){
        startActivity(Intent(this, doctorSignup_activity::class.java))
        finish()
    }

    fun signInC(v: View){
        val userE = findViewById<EditText>(R.id.username_edit_text)
        val passE = findViewById<EditText>(R.id.password_edit_text)
        authManager.signIn(userE.text.toString(), passE.text.toString()) { user ->
            if (user != null) {
                Log.d("MainActivity", "User signed in: ${user.uid}")
                goNext()
            } else {
                Log.e("MainActivity", "Sign-in failed.")
            }
        }
    }
}
package com.app.lightenlife

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class clientSignUp_Activity : AppCompatActivity() {

    private lateinit var authManager: AuthenticationManager

    private lateinit var username : EditText
    private lateinit var name : EditText
    private lateinit var mail : EditText
    private lateinit var pass : EditText
    private lateinit var passC : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.singuppatient)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userManager = UserManager()
        authManager = AuthenticationManager(userManager)

        username = findViewById(R.id.signup_user_name)
        name = findViewById(R.id.signup_name)
        mail = findViewById(R.id.signup_email)
        pass = findViewById(R.id.signup_Password)
        passC = findViewById(R.id.signup_c_Password)
    }

    private fun checkFields(): Boolean {
        if(username.text.trim().isEmpty()){
            username.error = "This field is required"
            return false
        }
        if(name.text.trim().isEmpty()){
            name.error = "This field is required"
            return false
        }
        if(mail.text.trim().isEmpty()){
            mail.error = "This field is required"
            return false
        }
        if(pass.text.isEmpty()){
            pass.error = "This field is required"
            return false
        }
        if(passC.text == pass.text){
            passC.error = "Confirmation field"
            return false
        }
        return true
    }

    fun signUpC(v: View) {
        if(checkFields()){
            val userC = Client(name.text.trim().toString(), username.text.trim().toString())
            authManager.signUp(mail.text.trim().toString(), pass.text.toString(), userC) {
                success ->
                if (success){
                    Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Nav_activity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this, "can't use this information ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun goSignInC(v: View){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
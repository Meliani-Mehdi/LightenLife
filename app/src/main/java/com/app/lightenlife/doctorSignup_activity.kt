package com.app.lightenlife

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class doctorSignupActivity : AppCompatActivity() {
    private var pdfUri: Uri? = null

    private var authManager: AuthenticationManager? = null

    private var username: EditText? = null
    private var name: EditText? = null
    private var mail: EditText? = null
    private var pass: EditText? = null
    private var passC: EditText? = null
    private var pdfStatus: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_signup)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            v.setPadding(
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            )
            insets
        }

        val userManager = UserManager()
        authManager = AuthenticationManager(userManager)

        username = findViewById(R.id.signup_user_name)
        name = findViewById(R.id.signup_name)
        mail = findViewById(R.id.signup_email)
        pass = findViewById(R.id.signup_Password)
        passC = findViewById(R.id.signup_c_Password)
        /*pdfStatus = findViewById<TextView>(R.id.pdf_status)*/
        /*val uploadPdfButton = findViewById<Button>(R.id.upload_pdf_button)*/

        /*   uploadPdfButton.setOnClickListener { v: View? -> openFileChooser() }*/
    }

    private fun checkFields(): Boolean {
        if (username!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            username!!.error = "This field is required"
            return false
        }
        if (name!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            name!!.error = "This field is required"
            return false
        }
        if (mail!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            mail!!.error = "This field is required"
            return false
        }
        if (pass!!.text.toString().isEmpty()) {
            pass!!.error = "This field is required"
            return false
        }
        if (passC!!.text.toString() != pass!!.text.toString()) {
            passC!!.error = "Passwords do not match"
            return false
        }
        /*if (pdfUri == null) {
            Toast.makeText(this, "Please upload your certification PDF", Toast.LENGTH_SHORT).show()
            return false
        }*/
        return true
    }

    fun signUpDoctor(v: View?) {
        if (checkFields()) {
            val doctor = Doctor(
                name!!.text.toString().trim { it <= ' ' },
                username!!.text.toString().trim { it <= ' ' },
                pdfUri.toString()
            )
            authManager!!.signUp(
                mail!!.text.toString().trim { it <= ' ' },
                pass!!.text.toString(),
                doctor
            ) { success: Boolean ->
                if (success) {
                    Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Nav_activity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Can't use this information", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

     fun goSignInDoctor(v: View?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

     /*
     fun openFileChooser() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("application/pdf")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST)
    }*/

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            pdfUri = data.data
            pdfStatus!!.text = "PDF Selected"
        }*/
    }

 /*   companion object {
        private const val PICK_PDF_REQUEST = 1
    }*/

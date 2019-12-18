package com.example.praxisprojekt.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.praxisprojekt.R
import com.example.praxisprojekt.fragmente.UserFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val register: TextView = findViewById(R.id.loginRegister)
        register.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserFragment()).commit()
        }

    }
}

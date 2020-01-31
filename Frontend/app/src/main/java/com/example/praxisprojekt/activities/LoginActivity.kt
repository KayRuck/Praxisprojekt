package com.example.praxisprojekt.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.praxisprojekt.R
import com.example.praxisprojekt.fragmente.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val container: FrameLayout? = findViewById(R.id.fragment_container_login)

        if (container != null) {
            if (savedInstanceState != null) return
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_login, LoginFragment()).commit()
        }
    }
}

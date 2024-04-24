package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        val dbHelper = DbHelper(this)
        val registroButton = findViewById<Button>(R.id.registro)

        registroButton.setOnClickListener(){
            val correo = findViewById<EditText>(R.id.correo).text.toString()
            Log.d("Registro","Correo registro: " + correo)
            val password = findViewById<EditText>(R.id.password).text.toString()
            Log.d("Registro","Password registro: " + password)
            if(correo != "" && password !=""){
                val result = dbHelper.insertDataUsuarios(correo, password)
                if (result != -1L) {
                    Toast.makeText(this,"Registro exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,"Error al registrar los datos", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Por favor establezca un correo y password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
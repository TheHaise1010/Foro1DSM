package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Creating the DB
        val dbHelper = DbHelper(this)
        dbHelper.deleteDatabase(this)
        val db = dbHelper.writableDatabase
        val mensaje: String = if (db != null) {
            "La base de datos se creó correctamente."
        } else {
            "Hubo un error al crear la base de datos."
        }
        //Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        Log.d("MiApp",mensaje)

        //INSERT DATA
        val correoTest = "jordanprueba3@prueba.com"
        val passwordTest = "1234"
        val result = dbHelper.insertDataUsuarios(correoTest, passwordTest)

        if (result != -1L) {
            Log.d("MiApp","Datos insertados correctamente.")
        } else {
            Log.d("MiApp","Error al insertar datos.")
        }
        db?.close()

        //READ DATA
        /*
        val cursor = dbHelper.getUsuarios()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password_"))
                // Hacer algo con los datos leídos, por ejemplo mostrarlos en un Toast
                val mensaje = "ID: $id, Nombre: $correo, Edad: $password"
                Log.d("MiApp",mensaje)
                //Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }
        */
        //DELETE DATA
        /*
        val idUsuarioAEliminar = 1 // ID del usuario que quieres eliminar
        val filasEliminadas = dbHelper.deleteUsuario(idUsuarioAEliminar)
        if (filasEliminadas > 0) {
            Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró ningún usuario con ese ID", Toast.LENGTH_SHORT).show()
        }
        */
        val loginButton = findViewById<Button>(R.id.login)
        val registroButton = findViewById<Button>(R.id.registro)
        loginButton.setOnClickListener {
            val inputCorreo = findViewById<EditText>(R.id.correo).text.toString()
            val inputPassword = findViewById<EditText>(R.id.password).text.toString()
            val intent = Intent(this, ItemsActivity::class.java)
            if(dbHelper.login(inputCorreo,inputPassword,this)){
                Toast.makeText(this,"Login exitoso", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }else{
                Toast.makeText(this,"Correo o password incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
        registroButton.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            Log.d("MiApp", "Antes de iniciar RegistroActivity")
            startActivity(intent)
            Log.d("MiApp", "Después de iniciar RegistroActivity")

        }
    }
}
package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "Foro1.db";
private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE usuarios (" +
            "    id INT PRIMARY KEY," +
            "    correo VARCHAR(255) UNIQUE," +
            "    password_ VARCHAR(255)" +
            ")"+
            ("CREATE TABLE articulos (" +
                    "    id INT PRIMARY KEY," +
                    "    nombre VARCHAR(255)," +
                    "    precio DECIMAL(10, 2)," +
                    "    imagen TEXT," +
                    "    descripcion TEXT" +
                    ");");
private const val SQL_CREATE_ENTRIES_2 = "CREATE TABLE detalle_compra (" +
        "    id_detalle INT PRIMARY KEY," +
        "    id_compra INT," +
        "    id_articulo INT," +
        "    cantidad INT," +
        "    precio_total DECIMAL(10, 2)," +
        "    FOREIGN KEY (id_compra) REFERENCES compras(id_compra)," +
        "    FOREIGN KEY (id_articulo) REFERENCES articulos(id)" +
        ");"
private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS MiTabla"

class MiDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES_2)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Creating the DB
        val dbHelper = MiDBHelper(this)
        val db = dbHelper.writableDatabase

        val mensaje: String = if (db != null) {
            "La base de datos se creó correctamente."
        } else {
            "Hubo un error al crear la base de datos."
        }

        // Mostrar el Toast con el mensaje correspondiente
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        db?.close();

        val loginButton = findViewById<Button>(R.id.login)
        val registroButton = findViewById<Button>(R.id.registro)

        loginButton.setOnClickListener {
            val intent = Intent(this, ItemsActivity::class.java)
            startActivity(intent)
        }
        registroButton.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
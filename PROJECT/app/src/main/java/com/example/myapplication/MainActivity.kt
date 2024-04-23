package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "Foro1.db"
private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE usuarios (" +
            "    id INTEGER PRIMARY KEY," +
            "    correo VARCHAR(255) UNIQUE," +
            "    password_ VARCHAR(255)" +
            ");"+
            ("CREATE TABLE articulos (" +
                    "    id INTEGER PRIMARY KEY," +
                    "    nombre VARCHAR(255)," +
                    "    precio DECIMAL(10, 2)," +
                    "    imagen TEXT," +
                    "    descripcion TEXT" +
                    ");")
private const val SQL_CREATE_ENTRIES_2 = "CREATE TABLE detalle_compra (" +
        "    id_detalle INTEGER PRIMARY KEY," +
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

    fun insertDataUsuarios(correo: String, password: String): Long {
        val contentValues = ContentValues()
        contentValues.put("correo", correo)
        contentValues.put("password_", password)

        val db = this.writableDatabase
        val result = db.insert("usuarios", null, contentValues)
        db.close()
        return result
    }
    fun getUsuarios(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM usuarios", null)
    }

    fun deleteDatabase(context: Context) {
        context.deleteDatabase(DATABASE_NAME)
        Toast.makeText(context, "Base de datos eliminada", Toast.LENGTH_SHORT).show()
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Creating the DB
        val dbHelper = MiDBHelper(this)
        dbHelper.deleteDatabase(this)
        val db = dbHelper.writableDatabase

        val mensaje: String = if (db != null) {
            "La base de datos se creó correctamente."
        } else {
            "Hubo un error al crear la base de datos."
        }
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        //INSERT DATA
        val correo = "jordanprueba3@prueba.com"
        val password = "1234"
        val result = dbHelper.insertDataUsuarios(correo, password)

        if (result != -1L) {
            Toast.makeText(this, "Datos insertados correctamente.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al insertar datos.", Toast.LENGTH_SHORT).show()
        }
        db?.close()

        //READ DATA
        val cursor = dbHelper.getUsuarios()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password_"))
                // Hacer algo con los datos leídos, por ejemplo mostrarlos en un Toast
                val mensaje = "ID: $id, Nombre: $correo, Edad: $password"
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }
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
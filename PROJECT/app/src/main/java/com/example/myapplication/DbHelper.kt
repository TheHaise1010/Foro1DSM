package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

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
class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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
        //Toast.makeText(context, "Base de datos eliminada", Toast.LENGTH_SHORT).show()
        Log.d("MiApp","Base de datos eliminada")
    }
    fun deleteUsuario(id: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete("usuarios", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun login(correo:String, password:String, context: Context):Boolean{

        var correo_ = ""
        var password_ = ""
        val db = this.readableDatabase
        val seleccion = "correo = ?"
        val valoresSeleccion = arrayOf(correo)
        val cursor = db.query("usuarios", null, seleccion, valoresSeleccion, null, null, null)
        val TAG = "MiApp"
        Log.d(TAG, "Correo funcion:" + correo)
        Log.d(TAG, "Password funcion:" + password)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                correo_ = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                password_ = cursor.getString(cursor.getColumnIndexOrThrow("password_"))
            }
            cursor.close()
        }

        Log.d(TAG, "Correo encontrado:" + correo_)
        Log.d(TAG, "Password encontrada: " + password_)
        if(correo == correo_ && password == password_ && correo_ != "" && password_ !=""){
            return true
        }else{
            return false
        }

    }

}
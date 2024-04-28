package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

private const val usuario = ""
private var articulosArray:JSONArray = JSONArray()
private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "Foro1.db"
private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE usuarios (" +
            "    id INTEGER PRIMARY KEY," +
            "    correo VARCHAR(255) UNIQUE," +
            "    password_ VARCHAR(255)" +
            ");"
private const val SQL_CREATE_ENTRIES_2 =  "CREATE TABLE articulos (" +
        "    id INTEGER PRIMARY KEY," +
        "    nombre VARCHAR(255)," +
        "    precio DECIMAL(10, 2)," +
        "    imagen TEXT," +
        "    descripcion TEXT" +
        ");"
private const val SQL_CREATE_ENTRIES_3 = "CREATE TABLE detalle_compra (" +
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
        db.execSQL(SQL_CREATE_ENTRIES_3)

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

    fun getArticulo(id:Int):Cursor?{
        try{
            val db = this.readableDatabase
            val seleccion = "id = ?"
            val valoresSeleccion = arrayOf(id.toString())
            val cursor = db.query("articulos", null, seleccion, valoresSeleccion, null, null, null)
            return cursor
        }catch (e:Exception){
            Log.d("MiApp",e.toString())
        }
        return null
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
    fun insertarArticulo(nombre: String, precio: Double, imagen: String, descripcion: String): Long {
        val contentValues = ContentValues().apply {
            put("nombre", nombre)
            put("precio", precio)
            put("imagen", imagen)
            put("descripcion", descripcion)
        }
        val db = this.writableDatabase
        val result = db.insert("articulos", null, contentValues)
        db.close()
        return result
    }

    fun agregarArticulosIniciales() {
        // Artículo 1
        val nombre1 = "Camiseta"
        val precio1 = 19.99
        val imagen1 = "url_imagen_camiseta"
        val descripcion1 = "Una cómoda camiseta de algodón."
        var result = insertarArticulo(nombre1, precio1, imagen1, descripcion1)

        if(result!=-1L){
            Log.d("MiApp","Articulo agregado correctamente")
        }else{
            Log.d("MiApp","Articulo no fue agregado")
        }

        // Artículo 2
        val nombre2 = "Pantalones"
        val precio2 = 29.99
        val imagen2 = "url_imagen_pantalones"
        val descripcion2 = "Un par de pantalones de mezclilla."
        result = insertarArticulo(nombre2, precio2, imagen2, descripcion2)
        if(result!=-1L){
            Log.d("MiApp","Articulo agregado correctamente")
        }else{
            Log.d("MiApp","Articulo no fue agregado")
        }

        // Artículo 3
        val nombre3 = "Zapatos"
        val precio3 = 39.99
        val imagen3 = "url_imagen_zapatos"
        val descripcion3 = "Un par de zapatos elegantes."
        result=insertarArticulo(nombre3, precio3, imagen3, descripcion3)
        if(result!=-1L){
            Log.d("MiApp","Articulo agregado correctamente")
        }else{
            Log.d("MiApp","Articulo no fue agregado")
        }
        // Artículo 4
        val nombre4 = "Bolso"
        val precio4 = 49.99
        val imagen4 = "url_imagen_bolso"
        val descripcion4 = "Un bolso de moda para llevar tus pertenencias."
        result = insertarArticulo(nombre4, precio4, imagen4, descripcion4)
        if(result!=-1L){
            Log.d("MiApp","Articulo agregado correctamente")
        }else{
            Log.d("MiApp","Articulo no fue agregado")
        }
        getArticulos()
    }

    fun getArticulos():Cursor? {
        try {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM articulos", null)
           if (cursor != null) {
               Log.d("MiApp","Cursor no es null")
               return cursor;
               /*
               while (cursor.moveToNext()) {
                   val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                   val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                   val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                   val imagen = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                   val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                   val mensaje = "ID: $id, Nombre: $nombre, Precio: $precio, Imagen: $imagen, Descripcion: $descripcion"
                   Log.d("MiApp",mensaje)
               }
               */
           }
        }catch (e:Exception){
            Log.d("MiApp", e.toString())
        }
        return null;
    }

    fun addArticuloCart(id:Int){
        //Log.d("MiApp","addArticuloCart() " + id)
        val articulo = JSONObject()
        val cursor = getArticulo(id)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                articulo.put("id", cursor.getString(cursor.getColumnIndexOrThrow("id")))
                Log.d("MiApp",cursor.getString(cursor.getColumnIndexOrThrow("id")))
                articulo.put("nombre", cursor.getString(cursor.getColumnIndexOrThrow("nombre")))
                Log.d("MiApp",cursor.getString(cursor.getColumnIndexOrThrow("nombre")))
                articulo.put("imagen", cursor.getString(cursor.getColumnIndexOrThrow("imagen")))
                Log.d("MiApp",cursor.getString(cursor.getColumnIndexOrThrow("imagen")))
                articulo.put("precio", cursor.getString(cursor.getColumnIndexOrThrow("precio")))
                Log.d("MiApp",cursor.getInt(cursor.getColumnIndexOrThrow("precio")).toString())
                articulo.put("descripcion", cursor.getString(cursor.getColumnIndexOrThrow("descripcion")))
                Log.d("MiApp",cursor.getString(cursor.getColumnIndexOrThrow("descripcion")))
                Log.d("MiApp","JSONObject: " + articulo)
                articulosArray.put(articulo)
                Log.d("MiApp","Articulos Array: " + articulosArray)
            }
            cursor.close()
        }
    }

}
package com.example.myapplication

import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import kotlin.math.roundToInt

class ShowCartActivity : AppCompatActivity() {

    private val dbHelper = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_cart)
        if(dbHelper.getArticulosArray().length() !=0){
            val buttonComprar = findViewById<Button>(R.id.button_comprar)
            buttonComprar.visibility = View.VISIBLE
            mostrarArticulosEnVistas(dbHelper.getArticulosArray())
        }else{
            val layoutPadre = findViewById<LinearLayout>(R.id.layout_padre_linear)
            val texto = TextView(this)
            texto.text = "No hay nada en el carrito"

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            texto.layoutParams = layoutParams

            layoutPadre.addView(texto)
            Log.d("MiApp","No hay articulos en la canasta")
        }

    }

    private fun mostrarArticulosEnVistas(articulos:JSONArray) {
        try{
                var index = 1
                for (i in 0 until articulos.length()) {
                    val articulo = articulos.getJSONObject(i)
                    val id = articulo.getInt("id")
                    val nombre = articulo.getString("nombre")
                    val precio = articulo.getDouble("precio")
                    val descripcion = articulo.getString("descripcion")

                    index++

                    val layout = findViewById<LinearLayout>(R.id.layout_padre_linear)
                    var layouthijo = LinearLayout(this).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        gravity = Gravity.CENTER
                        orientation = LinearLayout.VERTICAL
                        setPadding(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
                    }
                    var textView = TextView(this).apply {
                        this.id = id
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = nombre
                        textSize = 18f
                        gravity = Gravity.CENTER
                        setTextColor(Color.WHITE)
                        setPadding(0, 0, 0, 20.dpToPx())
                    }
                    layouthijo.addView(textView)
                    textView = TextView(this).apply {
                        this.id = id
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = "$" + String.format("%.2f", precio)
                        textSize = 18f // Establece el tamaño del texto en sp
                        gravity = Gravity.CENTER // Establece la gravedad del texto al centro
                        setTextColor(Color.WHITE) // Establece el color del texto
                        setPadding(0, 0, 0, 20.dpToPx())
                    }
                    layouthijo.addView(textView)
                    textView = TextView(this).apply {
                        this.id = id
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = descripcion
                        textSize = 18f // Establece el tamaño del texto en sp
                        gravity = Gravity.CENTER // Establece la gravedad del texto al centro
                        setTextColor(Color.WHITE) // Establece el color del texto
                        setPadding(0, 0, 0, 20.dpToPx())
                    }
                    layouthijo.addView(textView)
                    layout.addView(layouthijo)

            }
        }catch (e:Exception){
            Log.d("MiApp",e.toString())
        }

    }
    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).roundToInt()
    }
}
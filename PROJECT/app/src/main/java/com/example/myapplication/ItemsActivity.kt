package com.example.myapplication

import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.databinding.ActivityItemsBinding
import kotlin.math.roundToInt


class ItemsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarItems.toolbar)

        binding.appBarItems.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_items)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        /*val nombreTextView = findViewById<TextView>(getIdForName("name", 1))
        nombreTextView.text = "PRUEBA"*/
        val dbHelper = DbHelper(this)
        val cursor = dbHelper.getArticulos()
        mostrarArticulosEnVistas(cursor)


    }

    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).roundToInt()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_items)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun mostrarArticulosEnVistas(cursor: Cursor?) {
        try{
            if (cursor != null) {
                var index = 1
                while (cursor.moveToNext()) {
                    /*
                    val nombreTextView = findViewById<TextView>(getIdForName("name", index))
                    val precioTextView = findViewById<TextView>(getIdForName("price", index))
                    val descripcionTextView = findViewById<TextView>(getIdForName("description", index))
                    val addButton = findViewById<Button>(getIdForName("add", index))
                     */
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                    val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                    val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))

                    /*
                    nombreTextView.text = nombre
                    precioTextView.text = "$" + String.format("%.2f", precio)
                    descripcionTextView.text = descripcion
                     */
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
                        id = View.generateViewId()
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = nombre // Establece el texto
                        textSize = 18f // Establece el tamaño del texto en sp
                        gravity = Gravity.CENTER // Establece la gravedad del texto al centro
                        setTextColor(Color.WHITE) // Establece el color del texto
                        setPadding(0, 0, 0, 20.dpToPx())
                    }
                    val button = Button(this).apply {
                        id = View.generateViewId()
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text = "Agregar"
                    }
                    layouthijo.addView(textView)
                    textView = TextView(this).apply {
                        id = View.generateViewId()
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
                        id = View.generateViewId()
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
                    layouthijo.addView(button)
                    layout.addView(layouthijo)
                }
                cursor.close()
            }
        }catch (e:Exception){
            Log.d("MiApp",e.toString())
        }

    }

    private fun getIdForName(baseName: String, index: Int): Int {
        return resources.getIdentifier("${baseName}${index}", "id", packageName)
    }

}
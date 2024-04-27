package com.example.myapplication

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
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
import com.example.myapplication.databinding.ActivityItemsBinding


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
                    Log.d("MiApp","Testing primer linea")
                    val nombreTextView = findViewById<TextView>(getIdForName("name", index))
                    val precioTextView = findViewById<TextView>(getIdForName("price", index))
                    val descripcionTextView = findViewById<TextView>(getIdForName("description", index))
                    val addButton = findViewById<Button>(getIdForName("add", index))

                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                    val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                    val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))

                    nombreTextView.text = nombre
                    precioTextView.text = "$" + String.format("%.2f", precio)
                    descripcionTextView.text = descripcion
                    index++
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
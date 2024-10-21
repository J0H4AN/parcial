package com.example.biblioteca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.biblioteca.Screen.AutorScreen
import com.example.biblioteca.Screen.LibroScreen
import com.example.biblioteca.Screen.MiembroScreen
import com.example.biblioteca.Screen.PrestamoScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuramos los botones para navegar a las diferentes pantallas
        val buttonAutores: Button = findViewById(R.id.buttonAutores)
        val buttonLibros: Button = findViewById(R.id.buttonLibros)
        val buttonMiembros: Button = findViewById(R.id.buttonMiembros)
        val buttonPrestamos: Button = findViewById(R.id.buttonPrestamos)

        // Navegación hacia AutorScreen
        buttonAutores.setOnClickListener {
            val intent = Intent(this, AutorScreen::class.java)
            startActivity(intent)
        }

        // Navegación hacia LibroScreen
        buttonLibros.setOnClickListener {
            val intent = Intent(this, LibroScreen::class.java)
            startActivity(intent)
        }

        // Navegación hacia MiembroScreen
        buttonMiembros.setOnClickListener {
            val intent = Intent(this, MiembroScreen::class.java)
            startActivity(intent)
        }

        // Navegación hacia PrestamoScreen
        buttonPrestamos.setOnClickListener {
            val intent = Intent(this, PrestamoScreen::class.java)
            startActivity(intent)
        }
    }
}

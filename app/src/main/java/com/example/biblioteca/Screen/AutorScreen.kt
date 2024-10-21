package com.example.biblioteca.Screen

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biblioteca.Database.BibliotecaDatabase
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.R
import com.example.biblioteca.Repository.AutorRepository
import com.example.biblioteca.ViewModel.AutorViewModel
import com.example.biblioteca.ViewModel.AutorViewModelFactory

// Clase que representa la pantalla de gestión de autores
class AutorScreen : AppCompatActivity() {
    private lateinit var autorViewModel: AutorViewModel
    private lateinit var autoresList: MutableList<Autor>
    private lateinit var adapter: AutorAdapter
    private var autorEdit: Autor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autor)

        val autorDao = BibliotecaDatabase.getDatabase(application).autorDao()
        val repository = AutorRepository(autorDao)
        val factory = AutorViewModelFactory(repository)

        autorViewModel = ViewModelProvider(this, factory).get(AutorViewModel::class.java)

        val editTextNombre: EditText = findViewById(R.id.editTextNombre)
        val editTextApellido: EditText = findViewById(R.id.editTextApellido)
        val editTextNacionalidad: EditText = findViewById(R.id.editTextNacionalidad)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate)
        val listViewAutores: ListView = findViewById(R.id.listViewAutores)

        autoresList = mutableListOf()
        adapter = AutorAdapter(this, autoresList)
        listViewAutores.adapter = adapter

        // Filtros de entrada para permitir solo letras y espacios
        val letterAndSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            if (source != null && !source.toString().matches("^[a-zA-Z ]+$".toRegex())) {
                return@InputFilter ""
            }
            null
        }

        editTextNombre.filters = arrayOf(letterAndSpaceFilter)
        editTextApellido.filters = arrayOf(letterAndSpaceFilter)
        editTextNacionalidad.filters = arrayOf(letterAndSpaceFilter)

        buttonAdd.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val apellido = editTextApellido.text.toString().trim()
            val nacionalidad = editTextNacionalidad.text.toString().trim()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && nacionalidad.isNotEmpty()) {
                val autor = Autor(nombre = nombre, apellido = apellido, nacionalidad = nacionalidad)
                autorViewModel.insert(autor)
                limpiarCampos(editTextNombre, editTextApellido, editTextNacionalidad)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        buttonUpdate.setOnClickListener {
            autorEdit?.let {
                val nombre = editTextNombre.text.toString().trim()
                val apellido = editTextApellido.text.toString().trim()
                val nacionalidad = editTextNacionalidad.text.toString().trim()

                // Solo actualizar si los campos no están vacíos
                val updatedAutor = it.copy(
                    nombre = if (nombre.isNotEmpty()) nombre else it.nombre,
                    apellido = if (apellido.isNotEmpty()) apellido else it.apellido,
                    nacionalidad = if (nacionalidad.isNotEmpty()) nacionalidad else it.nacionalidad
                )
                autorViewModel.update(updatedAutor)
                limpiarCampos(editTextNombre, editTextApellido, editTextNacionalidad)
                autorEdit = null
                buttonUpdate.isEnabled = false
            }
        }

        listViewAutores.setOnItemClickListener { _, _, position, _ ->
            autorEdit = autoresList[position]
            editTextNombre.setText(autorEdit?.nombre)
            editTextApellido.setText(autorEdit?.apellido)
            editTextNacionalidad.setText(autorEdit?.nacionalidad)
            buttonUpdate.isEnabled = true
        }

        listViewAutores.setOnItemLongClickListener { _, _, position, _ ->
            val autorAEliminar = autoresList[position]

            // Crear y mostrar el AlertDialog de confirmación
            AlertDialog.Builder(this)
                .setTitle("Eliminar Autor")
                .setMessage("¿Estás seguro de que deseas eliminar a ${autorAEliminar.nombre} ${autorAEliminar.apellido}? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    autorViewModel.delete(autorAEliminar)
                    Toast.makeText(this, "Autor eliminado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()

            true
        }

        autorViewModel.allAutores.observe(this, { autores ->
            autoresList.clear()
            autoresList.addAll(autores)
            adapter.notifyDataSetChanged()
        })
    }

    private fun limpiarCampos(vararg editTexts: EditText) {
        editTexts.forEach { it.text.clear() }
    }
}

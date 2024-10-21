package com.example.biblioteca.Screen

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biblioteca.Database.BibliotecaDatabase
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.R
import com.example.biblioteca.Repository.AutorRepository
import com.example.biblioteca.Repository.LibroRepository
import com.example.biblioteca.ViewModel.AutorViewModel
import com.example.biblioteca.ViewModel.AutorViewModelFactory
import com.example.biblioteca.ViewModel.LibroViewModel
import com.example.biblioteca.ViewModel.LibroViewModelFactory

class LibroScreen : AppCompatActivity() {
    private lateinit var libroViewModel: LibroViewModel
    private lateinit var autorViewModel: AutorViewModel
    private lateinit var librosList: MutableList<Libro>
    private lateinit var adapter: ArrayAdapter<String>
    private var libroEdit: Libro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libro)

        // Inicialización de DAOs y Repositories
        val libroDao = BibliotecaDatabase.getDatabase(application).libroDao()
        val autorDao = BibliotecaDatabase.getDatabase(application).autorDao()
        val libroRepository = LibroRepository(libroDao)
        val autorRepository = AutorRepository(autorDao)
        val libroFactory = LibroViewModelFactory(libroRepository)
        val autorFactory = AutorViewModelFactory(autorRepository)

        // Inicialización de ViewModels
        libroViewModel = ViewModelProvider(this, libroFactory).get(LibroViewModel::class.java)
        autorViewModel = ViewModelProvider(this, autorFactory).get(AutorViewModel::class.java)

        // Referencias a las vistas
        val editTextTitulo: EditText = findViewById(R.id.editTextTitulo)
        val editTextGenero: EditText = findViewById(R.id.editTextGenero)
        val spinnerAutores: Spinner = findViewById(R.id.spinnerAutores)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate)
        val listViewLibros: ListView = findViewById(R.id.listViewLibros)

        // Inicialización de la lista y adaptador
        librosList = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        listViewLibros.adapter = adapter

        // Observer para los autores
        autorViewModel.allAutores.observe(this) { autores ->
            val autorIds = autores.map { it.autor_id.toString() }
            val adapterAutores = ArrayAdapter(this, android.R.layout.simple_spinner_item, autorIds)
            adapterAutores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAutores.adapter = adapterAutores
        }

        // Observer para los libros
        libroViewModel.allLibros.observe(this) { libros ->
            librosList.clear()
            librosList.addAll(libros)

            // Obtener los nombres de los autores para mostrarlos en la lista
            autorViewModel.allAutores.observe(this) { autores ->
                adapter.clear()
                adapter.addAll(librosList.map { libro ->
                    val autor = autores.find { it.autor_id == libro.autor_id }
                    val autorNombre = autor?.let { "${it.nombre} ${it.apellido}" } ?: "Desconocido"
                    "Título: ${libro.titulo}, Género: ${libro.genero}, Autor: $autorNombre"
                })
            }
        }

        // Botón Agregar
        buttonAdd.setOnClickListener {
            val titulo = editTextTitulo.text.toString().trim()
            val genero = editTextGenero.text.toString().trim()
            val autorId = spinnerAutores.selectedItem?.toString()?.toIntOrNull()

            if (titulo.isNotEmpty() && genero.isNotEmpty() && autorId != null) {
                val libro = Libro(titulo = titulo, genero = genero, autor_id = autorId)
                libroViewModel.insert(libro)

                Toast.makeText(this, "Libro creado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos(editTextTitulo, editTextGenero)
            } else {
                mostrarError("Los campos deben ser válidos")
            }
        }

        // Botón Actualizar
        buttonUpdate.setOnClickListener {
            libroEdit?.let {
                val titulo = editTextTitulo.text.toString().trim()
                val genero = editTextGenero.text.toString().trim()
                val autorId = spinnerAutores.selectedItem?.toString()?.toIntOrNull()

                if (titulo.isNotEmpty() && genero.isNotEmpty() && autorId != null) {
                    val updatedLibro = it.copy(titulo = titulo, genero = genero, autor_id = autorId)
                    libroViewModel.update(updatedLibro)

                    Toast.makeText(this, "Libro actualizado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos(editTextTitulo, editTextGenero)
                    libroEdit = null
                    buttonUpdate.isEnabled = false
                } else {
                    mostrarError("Los campos deben ser válidos")
                }
            }
        }

        // Click en un item de la lista
        listViewLibros.setOnItemClickListener { _, _, position, _ ->
            libroEdit = librosList[position]
            editTextTitulo.setText(libroEdit?.titulo)
            editTextGenero.setText(libroEdit?.genero)

            // Seleccionar el autor en el spinner
            val autorPosition = (spinnerAutores.adapter as ArrayAdapter<String>)
                .getPosition(libroEdit?.autor_id.toString())
            if (autorPosition != -1) {
                spinnerAutores.setSelection(autorPosition)
            }

            buttonUpdate.isEnabled = true
        }

        // Long click para eliminar
        listViewLibros.setOnItemLongClickListener { _, _, position, _ ->
            val libroAEliminar = librosList[position]
            mostrarAlertaEliminar(libroAEliminar)
            true
        }
    }

    private fun limpiarCampos(vararg editTexts: EditText) {
        editTexts.forEach { it.text.clear() }
    }

    private fun mostrarError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun mostrarAlertaEliminar(libro: Libro) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Libro")
        builder.setMessage("¿Estás seguro de que deseas eliminar el libro: '${libro.titulo}'?")
        builder.setPositiveButton("Sí") { _, _ ->
            libroViewModel.delete(libro)
            Toast.makeText(this, "Libro eliminado", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }
}

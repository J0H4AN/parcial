package com.example.biblioteca.Screen

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biblioteca.Database.BibliotecaDatabase
import com.example.biblioteca.Model.Libro
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.Model.Prestamo
import com.example.biblioteca.R
import com.example.biblioteca.Repository.LibroRepository
import com.example.biblioteca.Repository.MiembroRepository
import com.example.biblioteca.Repository.PrestamoRepository
import com.example.biblioteca.ViewModel.LibroViewModel
import com.example.biblioteca.ViewModel.LibroViewModelFactory
import com.example.biblioteca.ViewModel.MiembroViewModel
import com.example.biblioteca.ViewModel.MiembroViewModelFactory
import com.example.biblioteca.ViewModel.PrestamoViewModel
import com.example.biblioteca.ViewModel.PrestamoViewModelFactory
import java.util.*

class PrestamoScreen : AppCompatActivity() {
    private lateinit var prestamoViewModel: PrestamoViewModel
    private lateinit var libroViewModel: LibroViewModel
    private lateinit var miembroViewModel: MiembroViewModel
    private lateinit var prestamosList: MutableList<Prestamo>
    private lateinit var adapter: ArrayAdapter<String>
    private var prestamoEdit: Prestamo? = null

    private lateinit var editTextFechaPrestamo: EditText
    private lateinit var editTextFechaDevolucion: EditText
    private lateinit var spinnerLibro: Spinner
    private lateinit var spinnerMiembro: Spinner

    private lateinit var listaLibros: List<Libro>
    private lateinit var listaMiembros: List<Miembro>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prestamo)

        val prestamoDao = BibliotecaDatabase.getDatabase(application).prestamoDao()
        val libroDao = BibliotecaDatabase.getDatabase(application).libroDao()
        val miembroDao = BibliotecaDatabase.getDatabase(application).miembroDao()
        val prestamoRepository = PrestamoRepository(prestamoDao)
        val libroRepository = LibroRepository(libroDao)
        val miembroRepository = MiembroRepository(miembroDao)
        val prestamoFactory = PrestamoViewModelFactory(prestamoRepository)
        val libroFactory = LibroViewModelFactory(libroRepository)
        val miembroFactory = MiembroViewModelFactory(miembroRepository)

        prestamoViewModel = ViewModelProvider(this, prestamoFactory).get(PrestamoViewModel::class.java)
        libroViewModel = ViewModelProvider(this, libroFactory).get(LibroViewModel::class.java)
        miembroViewModel = ViewModelProvider(this, miembroFactory).get(MiembroViewModel::class.java)

        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate)
        val listViewPrestamos: ListView = findViewById(R.id.listViewPrestamos)

        // EditTexts para las fechas
        editTextFechaPrestamo = findViewById(R.id.editTextFechaPrestamo)
        editTextFechaDevolucion = findViewById(R.id.editTextFechaDevolucion)
        spinnerLibro = findViewById(R.id.spinnerLibro)
        spinnerMiembro = findViewById(R.id.spinnerMiembro)

        prestamosList = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, prestamosList.map {
            "Préstamo ID: ${it.prestamo_id}, Libro ID: ${it.libro_id}, Miembro ID: ${it.miembro_id}, Fecha Préstamo: ${it.fecha_prestamo}, Fecha Devolución: ${it.fecha_devolucion ?: "N/A"}"
        })
        listViewPrestamos.adapter = adapter

        buttonAdd.setOnClickListener {
            val libroId = (spinnerLibro.selectedItem as? Libro)?.libro_id ?: return@setOnClickListener
            val miembroId = (spinnerMiembro.selectedItem as? Miembro)?.miembro_id ?: return@setOnClickListener
            val fechaPrestamo = editTextFechaPrestamo.text.toString().trim()
            val fechaDevolucion = editTextFechaDevolucion.text.toString().trim()

            if (libroId >= 0 && miembroId >= 0 && fechaPrestamo.isNotEmpty()) {
                val prestamo = Prestamo(libro_id = libroId, miembro_id = miembroId, fecha_prestamo = fechaPrestamo, fecha_devolucion = if (fechaDevolucion.isNotEmpty()) fechaDevolucion else null)
                prestamoViewModel.insert(prestamo)
                Toast.makeText(this, "Préstamo creado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos(editTextFechaPrestamo, editTextFechaDevolucion)
            } else {
                mostrarError("Los campos deben ser válidos")
            }
        }

        buttonUpdate.setOnClickListener {
            prestamoEdit?.let {
                val libroId = (spinnerLibro.selectedItem as? Libro)?.libro_id ?: return@setOnClickListener
                val miembroId = (spinnerMiembro.selectedItem as? Miembro)?.miembro_id ?: return@setOnClickListener
                val fechaPrestamo = editTextFechaPrestamo.text.toString().trim()
                val fechaDevolucion = editTextFechaDevolucion.text.toString().trim()

                if (libroId >= 0 && miembroId >= 0 && fechaPrestamo.isNotEmpty()) {
                    val updatedPrestamo = it.copy(libro_id = libroId, miembro_id = miembroId, fecha_prestamo = fechaPrestamo, fecha_devolucion = if (fechaDevolucion.isNotEmpty()) fechaDevolucion else null)
                    prestamoViewModel.update(updatedPrestamo)
                    Toast.makeText(this, "Préstamo actualizado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos(editTextFechaPrestamo, editTextFechaDevolucion)
                    prestamoEdit = null
                    buttonUpdate.isEnabled = false
                } else {
                    mostrarError("Los campos deben ser válidos")
                }
            }
        }

        listViewPrestamos.setOnItemClickListener { _, _, position, _ ->
            prestamoEdit = prestamosList[position]
            editTextFechaPrestamo.setText(prestamoEdit?.fecha_prestamo)
            editTextFechaDevolucion.setText(prestamoEdit?.fecha_devolucion)
            buttonUpdate.isEnabled = true
        }

        listViewPrestamos.setOnItemLongClickListener { _, _, position, _ ->
            val prestamoAEliminar = prestamosList[position]
            // Confirmación antes de eliminar
            AlertDialog.Builder(this)
                .setTitle("Eliminar Préstamo")
                .setMessage("¿Estás seguro de que deseas eliminar este préstamo?")
                .setPositiveButton("Sí") { _, _ ->
                    prestamoViewModel.delete(prestamoAEliminar)
                    Toast.makeText(this, "Préstamo eliminado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        prestamoViewModel.allPrestamos.observe(this, { prestamos ->
            prestamosList.clear()
            prestamosList.addAll(prestamos)
            adapter.notifyDataSetChanged()
        })

        // Cargar los libros y miembros para los Spinners
        cargarLibros()
        cargarMiembros()

        // Configurar DatePicker para las fechas
        editTextFechaPrestamo.setOnClickListener { showDatePicker { date -> editTextFechaPrestamo.setText(date) } }
        editTextFechaDevolucion.setOnClickListener { showDatePicker { date -> editTextFechaDevolucion.setText(date) } }
    }

    private fun cargarLibros() {
        libroViewModel.allLibros.observe(this, { libros ->
            listaLibros = libros
            val adapterLibros = ArrayAdapter(this, android.R.layout.simple_spinner_item, libros)
            adapterLibros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLibro.adapter = adapterLibros
        })
    }

    private fun cargarMiembros() {
        miembroViewModel.allMiembros.observe(this, { miembros ->
            listaMiembros = miembros
            val adapterMiembros = ArrayAdapter(this, android.R.layout.simple_spinner_item, miembros)
            adapterMiembros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMiembro.adapter = adapterMiembros
        })
    }

    private fun limpiarCampos(vararg editTexts: EditText) {
        editTexts.forEach { it.text.clear() }
    }

    private fun mostrarError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Formato DD/MM/YYYY
            onDateSet(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}

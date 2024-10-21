package com.example.biblioteca.Screen

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.biblioteca.Database.BibliotecaDatabase
import com.example.biblioteca.Model.Miembro
import com.example.biblioteca.R
import com.example.biblioteca.Repository.MiembroRepository
import com.example.biblioteca.ViewModel.MiembroViewModel
import com.example.biblioteca.ViewModel.MiembroViewModelFactory
import java.util.Calendar

class MiembroScreen : AppCompatActivity() {
    private lateinit var miembroViewModel: MiembroViewModel
    private lateinit var miembrosList: MutableList<Miembro>
    private lateinit var adapter: ArrayAdapter<String>
    private var miembroEdit: Miembro? = null

    private lateinit var editTextFechaInscripcion: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miembro)

        // Configurar el título
        supportActionBar?.title = "MIEMBROS"

        val miembroDao = BibliotecaDatabase.getDatabase(application).miembroDao()
        val repository = MiembroRepository(miembroDao)
        val factory = MiembroViewModelFactory(repository)

        miembroViewModel = ViewModelProvider(this, factory).get(MiembroViewModel::class.java)

        val editTextNombre: EditText = findViewById(R.id.editTextNombre)
        val editTextApellido: EditText = findViewById(R.id.editTextApellido)
        editTextFechaInscripcion = findViewById(R.id.editTextFechaInscripcion)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate)
        val listViewMiembros: ListView = findViewById(R.id.listViewMiembros)

        // Inicializar la lista y el adaptador correctamente
        miembrosList = mutableListOf()
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()  // Inicializar con una lista vacía
        )
        listViewMiembros.adapter = adapter

        buttonAdd.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val apellido = editTextApellido.text.toString().trim()
            val fechaInscripcion = editTextFechaInscripcion.text.toString().trim()

            if (validarCampos(nombre, apellido, fechaInscripcion)) {
                val miembro = Miembro(nombre = nombre, apellido = apellido, fecha_inscripcion = fechaInscripcion)
                miembroViewModel.insert(miembro)
                limpiarCampos(editTextNombre, editTextApellido, editTextFechaInscripcion)
                Toast.makeText(this, "Miembro agregado exitosamente", Toast.LENGTH_SHORT).show()
            }
        }

        buttonUpdate.setOnClickListener {
            miembroEdit?.let {
                val nombre = editTextNombre.text.toString().trim()
                val apellido = editTextApellido.text.toString().trim()
                val fechaInscripcion = editTextFechaInscripcion.text.toString().trim()

                if (validarCampos(nombre, apellido, fechaInscripcion)) {
                    val updatedMiembro = it.copy(nombre = nombre, apellido = apellido, fecha_inscripcion = fechaInscripcion)
                    miembroViewModel.update(updatedMiembro)
                    limpiarCampos(editTextNombre, editTextApellido, editTextFechaInscripcion)
                    miembroEdit = null
                    buttonUpdate.isEnabled = false
                    Toast.makeText(this, "Miembro actualizado exitosamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        listViewMiembros.setOnItemClickListener { _, _, position, _ ->
            miembroEdit = miembrosList[position]
            editTextNombre.setText(miembroEdit?.nombre)
            editTextApellido.setText(miembroEdit?.apellido)
            editTextFechaInscripcion.setText(miembroEdit?.fecha_inscripcion)
            buttonUpdate.isEnabled = true
        }

        listViewMiembros.setOnItemLongClickListener { _, _, position, _ ->
            val miembroAEliminar = miembrosList[position]
            mostrarDialogoEliminar(miembroAEliminar)
            true
        }

        // Observar los cambios en la lista de miembros
        miembroViewModel.allMiembros.observe(this) { miembros ->
            miembrosList.clear()
            miembrosList.addAll(miembros)
            // Actualizar el adaptador con los nuevos datos
            adapter.clear()
            adapter.addAll(miembrosList.map { "${it.nombre} ${it.apellido}" })
            adapter.notifyDataSetChanged()
        }

        // Configurar DatePicker para la fecha de inscripción
        editTextFechaInscripcion.setOnClickListener { showDatePicker { date -> editTextFechaInscripcion.setText(date) } }
    }

    private fun validarCampos(nombre: String, apellido: String, fechaInscripcion: String): Boolean {
        return when {
            nombre.isEmpty() || apellido.isEmpty() || fechaInscripcion.isEmpty() -> {
                mostrarError("Todos los campos son obligatorios")
                false
            }
            nombre.any { it.isDigit() } || apellido.any { it.isDigit() } -> {
                mostrarError("El nombre y el apellido no pueden contener números")
                false
            }
            else -> true
        }
    }

    private fun mostrarDialogoEliminar(miembro: Miembro) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Miembro")
        builder.setMessage("¿Está seguro de que desea eliminar a ${miembro.nombre} ${miembro.apellido}?")
        builder.setPositiveButton("Sí") { _, _ ->
            miembroViewModel.delete(miembro)
            Toast.makeText(this, "Miembro eliminado exitosamente", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No", null)
        builder.show()
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

package com.example.biblioteca.Screen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.biblioteca.Model.Autor
import com.example.biblioteca.R

// Adaptador personalizado para mostrar autores en la lista
class AutorAdapter(context: Context, autores: MutableList<Autor>) :
    ArrayAdapter<Autor>(context, 0, autores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val autor = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_autor, parent, false)

        // Obtener las vistas por ID
        val textViewId = view.findViewById<TextView>(R.id.textViewId)
        val textViewNombreCompleto = view.findViewById<TextView>(R.id.textViewNombreCompleto)
        val textViewNacionalidad = view.findViewById<TextView>(R.id.textViewNacionalidad)

        // Asignar valores a las vistas
        textViewId.text = "ID: ${autor?.autor_id}"  // Mostrar ID del autor
        textViewNombreCompleto.text = "${autor?.nombre} ${autor?.apellido}"
        textViewNacionalidad.text = autor?.nacionalidad

        return view
    }
}

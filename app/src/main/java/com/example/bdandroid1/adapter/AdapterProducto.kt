package com.example.bdandroid1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bdandroid1.Models.Producto
import com.example.bdandroid1.R
import com.google.firebase.database.FirebaseDatabase

// Adapter que maneja la lista de productos en el RecyclerView
class AdapterProducto(private val productos: ArrayList<Producto>, private val context: Context):
    RecyclerView.Adapter<AdapterProducto.ViewHolder>() {

    // Clase ViewHolder que contiene las vistas para cada ítem en el RecyclerView
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre) // Nombre del producto
        val precio: TextView = itemView.findViewById(R.id.tvPrecio) // Precio del producto
        val descripcion: TextView = itemView.findViewById(R.id.tvDescripcion) // Descripción del producto
        val eliminar: Button = itemView.findViewById(R.id.btnEliminar) // Botón para eliminar el producto
    }

    // Método para inflar el layout del ítem y crear una instancia del ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflar el layout de cada item del RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_productos, parent, false)
        return ViewHolder(view) // Devolver una nueva instancia de ViewHolder con el itemView
    }

    // Método para enlazar los datos del producto con las vistas en cada ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position] // Obtener el producto en la posición correspondiente
        holder.nombre.text = producto.nombre // Asignar el nombre del producto
        holder.precio.text = producto.precio // Asignar el precio del producto
        holder.descripcion.text = producto.descripcion // Asignar la descripción del producto

        // Configurar el comportamiento del botón "Eliminar"
        holder.eliminar.setOnClickListener {
            // Llamar al método eliminarProducto y pasar el ID del producto
            eliminarProducto(producto.id, context)

            // Eliminar el producto de la lista local
            productos.removeAt(position)

            // Notificar al RecyclerView que el ítem fue removido
            notifyItemRemoved(position)
        }
    }

    // Método para obtener la cantidad total de productos en la lista
    override fun getItemCount(): Int {
        return productos.size
    }

    // Método para eliminar un producto de Firebase usando su ID
    private fun eliminarProducto(productoId: String?, context: Context) {
        val db = FirebaseDatabase.getInstance() // Obtener una instancia de FirebaseDatabase
        val productosRef = db.getReference("Producto") // Referencia al nodo "Producto" en Firebase

        // Eliminar el producto en Firebase usando el ID
        productosRef.child(productoId!!).removeValue()
            .addOnSuccessListener {
                // Si la eliminación es exitosa, mostrar un mensaje
                Toast.makeText(context, "Producto eliminado exitosamente", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                // Si falla la eliminación, mostrar un mensaje de error
                Toast.makeText(context, "Producto no eliminado", Toast.LENGTH_LONG).show()
            }
    }
}

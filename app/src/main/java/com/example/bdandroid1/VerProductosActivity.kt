package com.example.bdandroid1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bdandroid1.Models.Producto
import com.example.bdandroid1.adapter.AdapterProducto
import com.example.bdandroid1.databinding.ActivityVerProductosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Actividad para mostrar la lista de productos
class VerProductosActivity : AppCompatActivity() {

    // Instancia de ViewBinding para manejar la UI
    private lateinit var binding: ActivityVerProductosBinding

    // Lista para almacenar los productos
    private lateinit var productosList: ArrayList<Producto>

    // RecyclerView para mostrar la lista de productos
    private lateinit var productosRecyclerView: RecyclerView

    // Referencia a la base de datos de Firebase
    private lateinit var database: DatabaseReference

    // Adaptador para vincular los datos al RecyclerView
    private lateinit var adapterProducto: AdapterProducto

    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar Edge-to-Edge para que la UI ocupe toda la pantalla
        enableEdgeToEdge()

        // Inflar el layout de la actividad usando ViewBinding
        binding = ActivityVerProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el RecyclerView y configurarlo
        productosRecyclerView = binding.rvProductos
        productosRecyclerView.layoutManager = LinearLayoutManager(this)  // Usar un LayoutManager lineal
        productosRecyclerView.setHasFixedSize(true)  // Establecer tamaño fijo para mejorar rendimiento

        // Inicializar la lista de productos
        productosList = arrayListOf<Producto>()

        // Crear el adaptador con la lista de productos y el contexto de la actividad
        adapterProducto = AdapterProducto(productosList, this)

        // Asignar el adaptador al RecyclerView
        productosRecyclerView.adapter = adapterProducto

        // Llamar al método para cargar los productos desde Firebase
        getProductos()

        // Configurar el botón para regresar a la actividad anterior
        binding.btnVolver.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()  // Regresar a la actividad anterior
        }
    }

    // Método para obtener los productos desde Firebase
    private fun getProductos() {
        // Referencia a la base de datos en Firebase bajo el nodo "Producto"
        database = FirebaseDatabase.getInstance().getReference("Producto")

        // Establecer un ValueEventListener para escuchar los cambios en la base de datos
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Verificar si el snapshot tiene datos
                if (snapshot.exists()) {
                    // Limpiar la lista de productos para evitar duplicados
                    productosList.clear()

                    // Iterar a través de los elementos hijos (productos)
                    for (productosSnapshot in snapshot.children) {
                        // Convertir el snapshot a un objeto Producto y agregarlo a la lista
                        val producto = productosSnapshot.getValue(Producto::class.java)
                        if (producto != null) {
                            productosList.add(producto)
                        }
                    }

                    // Notificar al adaptador que los datos han cambiado y actualizar la UI
                    adapterProducto.notifyDataSetChanged()
                }
            }

            // Método que maneja cualquier error en la lectura de datos
            override fun onCancelled(error: DatabaseError) {
                // Aquí se podría manejar el error, por ejemplo, mostrando un mensaje en la UI
                // En este caso está vacío, pero podría agregar código de manejo de errores
            }
        })
    }
}

package com.example.bdandroid1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bdandroid1.Models.Producto
import com.example.bdandroid1.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// La actividad principal de la aplicación
class MainActivity : AppCompatActivity() {

    // Instancia de ViewBinding para gestionar las vistas de la interfaz
    private lateinit var binding: ActivityMainBinding

    // Referencia a la base de datos de Firebase
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar la funcionalidad Edge-to-Edge para que la UI ocupe toda la pantalla
        enableEdgeToEdge()

        // Inicializar el ViewBinding inflando el layout de la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar la referencia a la base de datos Firebase, en la colección "Producto"
        database = FirebaseDatabase.getInstance().getReference("Producto")

        // Configuración del botón para guardar un producto en la base de datos
        binding.btnGuardarProd.setOnClickListener {
            // Obtener los datos del formulario (nombre, precio y descripción)
            val nombre = binding.etNombreProducto.text.toString()
            val precio = binding.etPrecioProducto.text.toString()
            val descripcion = binding.etDescripcionProducto.text.toString()

            // Crear un ID único para el producto en la base de datos
            val id = database.child("Productos").push().key

            // Validación de campos vacíos y mostrar errores en los campos correspondientes
            if (nombre.isBlank()) {
                binding.etNombreProducto.error = "Nombre requerido"
                return@setOnClickListener
            }
            if (precio.isBlank()) {
                binding.etPrecioProducto.error = "Precio requerido"
                return@setOnClickListener
            }
            if (descripcion.isBlank()) {
                binding.etDescripcionProducto.error = "Descripción"
                return@setOnClickListener
            }

            // Crear el objeto Producto con los datos obtenidos
            val producto = Producto(id, nombre, precio, descripcion)

            // Guardar el producto en la base de datos de Firebase
            // Usamos el ID generado para el nuevo producto
            database.child(id!!).setValue(producto).addOnSuccessListener {
                // Si se guarda correctamente, mostrar un mensaje de éxito
                Toast.makeText(this, "Producto nuevo añadido", Toast.LENGTH_LONG).show()

                // Limpiar los campos del formulario
                binding.etNombreProducto.setText("")
                binding.etPrecioProducto.setText("")
                binding.etDescripcionProducto.setText("")
            }.addOnFailureListener {
                // Si hay un error al guardar, mostrar un mensaje de error
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        }

        // Configuración del botón para ver los productos guardados
        binding.btnVerProd.setOnClickListener {
            // Iniciar la actividad VerProductosActivity para mostrar la lista de productos
            val intent = Intent(this, VerProductosActivity::class.java)
            startActivity(intent)
        }
    }
}

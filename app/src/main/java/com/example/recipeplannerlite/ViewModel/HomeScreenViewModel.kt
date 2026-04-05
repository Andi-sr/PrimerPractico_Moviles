package com.example.recipeplannerlite.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.recipeplannerlite.Data.Recetas
import kotlin.collections.set

class HomeScreenViewModel : ViewModel() {

    var pantallaActual by mutableStateOf("lista")

    private val _recetas = mutableStateListOf<Recetas>()
    val recetas: List<Recetas> = _recetas

    var planSemanal = mutableStateMapOf<String, String>()

    fun cambiarPantalla(pantalla: String) {
        pantallaActual = pantalla
    }

    fun agregarReceta(nombre: String, ingredientes: List<String>) {
        _recetas.add(Recetas(nombre, ingredientes))
    }

    fun asignarReceta(dia: String, recetas: String) {
        planSemanal[dia] = recetas
    }


    fun obtenerListaCompras(): Map<String, Int> {

        val resultado = mutableMapOf<String, Int>()

        planSemanal.values.forEach { nombreReceta ->

            val receta = recetas.find { it.nombre == nombreReceta }

            receta?.ingredientes?.forEach { ingrediente ->

                val nombre = ingrediente.lowercase()

                resultado[nombre] = (resultado[nombre] ?: 0) + 1
            }
        }

        return resultado
    }
}
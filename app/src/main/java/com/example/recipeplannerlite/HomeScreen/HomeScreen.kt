package com.example.recipeplannerlite.HomeScreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recipeplannerlite.ViewModel.HomeScreenViewModel
import kotlin.collections.filter


@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {

    Column {

        Menu(viewModel)

        when (viewModel.pantallaActual) {
            "lista" -> PantallaListaRecetas(viewModel)
            "crear" -> PantallaCrearReceta(viewModel)
            "plan" -> PantallaPlanSemanal(viewModel)
            "compras" -> PantallaListaCompras(viewModel)
        }
    }
}

@Composable
//Arreglarlo se ve feo

//Menu
fun Menu(viewModel: HomeScreenViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {

        Button(onClick = { viewModel.cambiarPantalla("lista") }, modifier = Modifier.weight(1f)) {
            Text("Recetas")
        }

        Button(onClick = { viewModel.cambiarPantalla("plan") }, modifier = Modifier.weight(1f)) {
            Text("Plan")
        }

        Button(onClick = { viewModel.cambiarPantalla("compras") }, modifier = Modifier.weight(1f)) {
            Text("Compras")
        }
    }
}
//Añadir un scroll
// 1. LISTA DE RECETAS
@Composable
fun PantallaListaRecetas(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    var busqueda by remember { mutableStateOf("") }

    val recetasFiltradas = viewModel.recetas.filter {
        it.nombre.contains(busqueda, ignoreCase = true) ||
                it.ingredientes.any { ing -> ing.contains(busqueda, ignoreCase = true) }
    }

    Column(modifier = modifier.padding(16.dp)) {

        Text(
            "Recetario",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar por nombre o ingrediente...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.cambiarPantalla("crear")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear receta")
        }

        Spacer(modifier = Modifier.height(8.dp))


        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(recetasFiltradas) { receta ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            receta.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Ingredientes: ${receta.ingredientes.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
// El scroll no esta funcionando
// 2. CREAR RECETA
@Composable
fun PantallaCrearReceta(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    var nombre by remember { mutableStateOf("") }
    val ingredientes = remember { mutableStateListOf<String>() }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        Text("Nueva Receta")

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Ingredientes")

        ingredientes.forEachIndexed { index, ing ->

            Row(verticalAlignment = Alignment.CenterVertically) {

                OutlinedTextField(
                    value = ing,
                    onValueChange = { ingredientes[index] = it },
                    placeholder = { Text("Ej: 2Kg Pollo") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    ingredientes.removeAt(index)
                }) {
                    Icon(Icons.Default.Delete, null)
                }
            }
        }

        Button(onClick = { ingredientes.add("") }) {
            Text("Añadir Ingrediente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.agregarReceta(nombre, ingredientes)
                viewModel.cambiarPantalla("lista")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}

//El modelo no se entiende mucho hay que cambiarlo
// 3. PLAN SEMANAL
@Composable
fun PantallaPlanSemanal(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    val dias = listOf(
        "Lunes", "Martes", "Miércoles",
        "Jueves", "Viernes", "Sábado", "Domingo"
    )

    val scrollState = rememberScrollState()
    val recetas = viewModel.recetas

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        Text(
            "Plan de la Semana",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        dias.forEach { dia ->

            var expanded by remember { mutableStateOf(false) }

            val recetaSeleccionada =
                viewModel.planSemanal[dia] ?: "No seleccionada"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(dia, fontWeight = FontWeight.Bold)

                    Text("Receta: $recetaSeleccionada")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { expanded = true }) {
                        Text("Seleccionar receta")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        recetas.forEach { receta ->

                            DropdownMenuItem(
                                text = { Text(receta.nombre) },
                                onClick = {
                                    viewModel.asignarReceta(dia, receta.nombre)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// 4. LISTA DE COMPRAS

@Composable
fun PantallaListaCompras(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {

    val compras = viewModel.obtenerListaCompras()

    Column(modifier = modifier.padding(16.dp)) {

        Text(
            "Lista de Compras",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "Generada automáticamente",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {

            items(compras.toList()) { item ->

                val (nombre, cantidad) = item
                var checked by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )

                    Column {

                        Text(
                            nombre.replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Total: $cantidad",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                HorizontalDivider(thickness = 0.5.dp)
            }
        }
    }
}


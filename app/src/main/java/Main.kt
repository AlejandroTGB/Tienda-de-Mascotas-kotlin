package com.duroc.tiendademascotas
import kotlinx.coroutines.*

fun mostrarCatalogoMascotas(lista: List<Mascota>){
    println("Catalogo de Mascotas:")
    lista.forEach{ mascota ->
        println(mascota.info())
    }
}

fun mostrarCatalogoProductos(lista: List<Producto>){
    println("Catalogo de Productos:")
    lista.forEach{ producto ->
        println("Producto: ${producto.nombre}, Precio: ${producto.precio}")
    }
}

fun filtrarPorTipo(tipo: String, lista: List<Mascota>) {
    println("Filtrando por tipo: $tipo")
    if (tipo.equals("perro", ignoreCase = true)) {
        lista.forEach { mascota ->
            if (mascota is Perro) {
                println(mascota.info())
            }
        }
    } else if (tipo.equals("gato", ignoreCase = true)) {
        lista.forEach { mascota ->
            if (mascota is Gato) {
                println(mascota.info())
            }
        }
    } else if (tipo.equals("ave", ignoreCase = true)) {
        lista.forEach { mascota ->
            if (mascota is Ave) {
                println(mascota.info())
            }
        }
    } else {
        println("Tipo no reconocido.")
    }
}

fun agregarProductoPorIndice(indice: Int, lista: List<Producto>, carrito: Carrito) {
    if (indice in lista.indices) {
        val producto = lista[indice]
        carrito.agregarProducto(producto)
        println("Producto '${producto.nombre}' agregado al carrito.")
    } else {
        println("Indice fuera de rango.")
    }
}

fun verCarrito(carrito: Carrito) {
    println("Contenido del carrito:")
    carrito.listarProductos().forEach { producto ->
        println("Producto: ${producto.nombre}, Precio: ${producto.precio}")
    }
    println("Total: $${carrito.total()}")
}

fun aplicarDescuento(total: Double, porcentaje: Double): Double {
    if (porcentaje < 0 || porcentaje > 100) {
        println("Porcentaje de descuento invalido. Debe estar entre 0 y 100.")
        return total
    } else {
        val descuento = total * (porcentaje / 100.0)
        return total - descuento
    }
}

suspend fun revisarSalud(mascota: Mascota){
    val tiempo =(600..1200).random()
    delay(tiempo.toLong())
    if (mascota.getSalud() < 50) {
        val puntos = (10..30).random()
        mascota.mejorarSalud(puntos)
        println("La salud de ${mascota.nombre} ha mejorado a ${mascota.getSalud()}.")
    } else {
        println("La salud de ${mascota.nombre} esta bien y no necesita mejora.")
    }
}

suspend fun prepararPapeles(mascota: Mascota){
    val tiempo =(500..900).random()
    delay(tiempo.toLong())
    println("Los papeles de adopcion de ${mascota.nombre} estan listos.")
}

suspend fun verificarDomicilio(): Boolean {
    return try {
        withTimeout(2000) {
            val tiempo = (1000..3000).random()
            delay(tiempo.toLong())
            println("Tiempo de verificacion del domicilio: $tiempo ms")
            true
        }
    } catch (e: TimeoutCancellationException) {
        println("Verificacion de domicilio excedio el tiempo limite (2000 ms).")
        false
    }
}

fun main() {
    val catalogoMascotas = listOf(
        Gato("Pelela", 2, 500.0, 80, "Negro"),
        Perro("Reinaldo", 7, 700.0, 20, "Border Collie"),
        Ave("Piolin", 1, 300.0, 85, "Canario"),
        Gato("Garfield", 5, 600.0, 75, "Naranja"),
        Perro("Galaxy Destroyer", 4, 800.0, 95, "Pastor Alemán")
    )

    val catalogoProductos = listOf(
        Producto("Alimento para gatos", 120.0),
        Producto("Juguete para perros", 80.0),
        Producto("Jaula para aves", 250.0),
        Producto("Collar", 60.0),
        Producto("Cama para mascotas", 200.0)
    )

    val carrito = Carrito()
    var opcion: Int

    println("Bienvenido a la Tienda de Mascotas")
    do {
        println("""
        -----------------------------------
        Menu Tienda de Mascotas
        1. Ver mascotas
        2. Filtrar mascotas por tipo
        3. Ver productos
        4. Agregar producto al carrito
        5. Ver carrito y aplicar descuento (PET10 = 10%)
        6. Finalizar adopcion de una mascota
        7. Salir
        Elige una opcion: 
        ------------------------------------
        """)
        opcion = readLine()?.toIntOrNull() ?: 0

        when (opcion) {
            1 -> mostrarCatalogoMascotas(catalogoMascotas)
            2 -> {
                println("Ingrese el tipo de mascota a filtrar (perro, gato, ave):")
                val tipo = readLine() ?: ""
                filtrarPorTipo(tipo, catalogoMascotas)
            }
            3 -> mostrarCatalogoProductos(catalogoProductos)
            4 -> {
                println("Ingrese el indice del producto a agregar al carrito (0-${catalogoProductos.size - 1}):")
                val indice = readLine()?.toIntOrNull() ?: -1
                agregarProductoPorIndice(indice, catalogoProductos, carrito)
            }
            5 -> {
                verCarrito(carrito)
                println("Ingrese un codigo de descuento si tiene (o presione Enter para omitir):")
                val codigo = readLine() ?: ""
                if (codigo.equals("PET10", ignoreCase = true)) {
                    val totalConDescuento = aplicarDescuento(carrito.total(), 10.0)
                    println("Total con descuento aplicado: $$totalConDescuento")
                } else if (codigo.isNotEmpty()) {
                    println("Código de descuento inválido.")
                }
            }
            6 -> {
                println("Ingrese el nombre de la mascota que desea adoptar:")
                val nombreMascota = readLine() ?: ""
                val mascotaAdoptada = catalogoMascotas.find { it.nombre.equals(nombreMascota, ignoreCase = true) }
                if (mascotaAdoptada != null) {
                    runBlocking {
                        val saludJob = launch { revisarSalud(mascotaAdoptada)}
                        val papelesJob = launch { prepararPapeles(mascotaAdoptada) }
                        val domicilioValido = async { verificarDomicilio() }
                        saludJob.join()
                        papelesJob.join()
                        if (domicilioValido.await()) {
                            println("Adopcion aprobada!")
                        } else {
                            println("Adopcion rechazada :(")
                        }

                    }
                } else {
                    println("No se encontro una mascota con ese nombre.")
                }
            }
            7 -> println("Gracias por visitar la Tienda de Mascotas. Hasta luego!")
            else -> println("Opcion no valida. Intente nuevamente.")
        }

    } while (opcion != 7)
}

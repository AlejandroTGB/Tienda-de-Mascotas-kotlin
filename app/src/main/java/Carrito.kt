package com.duroc.tiendademascotas

class Carrito {
    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun listarProductos(): List<Producto> {
        return productos
    }

    fun total(): Double {
        return productos.sumOf { it.precio }
    }
}
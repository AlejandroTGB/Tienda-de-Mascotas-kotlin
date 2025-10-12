package com.duroc.tiendademascotas

class Ave (
    nombre: String,
    edad: Int,
    precio: Double,
    salud: Int,
    val especie: String
) : Mascota(nombre, edad, precio, salud) {

    override fun info(): String {
        return super.info() + ", Especie: $especie"
    }
}
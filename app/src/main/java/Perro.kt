package com.duroc.tiendademascotas

class Perro(
    nombre: String,
    edad: Int,
    precio: Double,
    salud: Int,
    val raza: String
) : Mascota(nombre, edad, precio, salud) {

    override fun info(): String {
        return super.info() + ", Raza: $raza"
    }
}
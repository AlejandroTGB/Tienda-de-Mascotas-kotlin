package com.duroc.tiendademascotas
class Gato (
    nombre: String,
    edad: Int,
    precio: Double,
    salud: Int,
    val color: String
) : Mascota(nombre, edad, precio, salud) {

    override fun info(): String {
        return super.info() + ", Color: $color"
    }
}
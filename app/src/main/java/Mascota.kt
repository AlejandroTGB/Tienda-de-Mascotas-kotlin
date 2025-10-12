package com.duroc.tiendademascotas

open class Mascota (
    val nombre: String,
    var edad: Int,
    val precio: Double,
    private var salud: Int
) {
    fun mejorarSalud(puntos: Int) {
        salud += puntos
        if (salud > 100) {
            salud = 100
        }
    }

    fun getSalud(): Int {
        return salud
    }

    open fun info(): String{
        return "Nombre: $nombre, Edad: $edad, Precio: $precio, Salud: $salud"
    }
}
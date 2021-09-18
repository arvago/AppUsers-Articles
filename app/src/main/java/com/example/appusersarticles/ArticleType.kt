package com.example.appusersarticles

enum class ArticleType(val id: Int, val image: Int, val text: String){
    DEPORTE(1, R.drawable.deporte, "Deporte"),
    SALUD(2, R.drawable.salud, "Salud"),
    SOCIAL(3, R.drawable.social, "Social"),
    ENTRETENIMIENTO(4, R.drawable.entretenimiento, "Entretenimiento"),
    POLITICA(5, R.drawable.politica, "Politica")
}
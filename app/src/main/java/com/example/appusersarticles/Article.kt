package com.example.appusersarticles

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class Article(
    var id: Int? = null,
    var title: String = "",
    var description: String = "",
    var picture: ArticleType? = null,
    var likes: Int? = null,
    var user: Int? = null
): Parcelable {
    companion object {
        val Articles = mutableListOf(
            Article(1, "FIG", "La nueva edición del FIG, se realizará del 12-15 de Noviembre ", ArticleType.SOCIAL, 2, 1),
            Article(2, "El gran Concierto", "Bad Bunny dió su primer concierto después de pandemia", ArticleType.ENTRETENIMIENTO,5, 1),
            Article(3, "Jornada de Vacunación","Inicia Jornada de Segunda Dosis de 40-49 años", ArticleType.SALUD, 6, 5),
            Article(4, "Golazooo!", "Golazoo de Messi! en un partido contra el Real Madrid", ArticleType.DEPORTE, 2, 4),
        )
    }
}
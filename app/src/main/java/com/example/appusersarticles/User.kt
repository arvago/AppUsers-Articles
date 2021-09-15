package com.example.appusersarticles

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class User(
    var id: Int? = null,
    var username: String = "",
    var password: String = "",
    var picture: Int? = null,
    var type: UserLevel? = null,
    var numArticles: Int? = null
): Parcelable {
    companion object {
        val users = arrayOf(
            User(1, "arvago", "ar123", R.drawable.arvago, UserLevel.EDITOR, 1),
            User(2, "yavago", "ya123", R.drawable.yavago, UserLevel.WATCHER, 1),
            User(3, "alraga", "al123", R.drawable.alraga, UserLevel.WATCHER, 1),
            User(4, "memo8a", "me123", R.drawable.memo8a, UserLevel.WATCHER, 0),
            User(5, "shaklo", "sh123", R.drawable.shaklo, UserLevel.EDITOR, 0),
            User(6, "bebabu", "be123", R.drawable.bebabu, UserLevel.EDITOR, 1)
        )
    }
}
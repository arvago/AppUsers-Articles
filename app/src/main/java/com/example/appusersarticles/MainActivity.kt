package com.example.appusersarticles

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class MainActivity : AppCompatActivity() {
    val PREFS_U = "MY_PREFERENCES_U"
    val PREFS_A = "MY_PREFERECES_A"
    val USER_PREFS = "LOGGED_USERS"
    val ARTICLE_PREFS = "ARTICLES"
    val moshi = Moshi.Builder().build()
    lateinit var preferencesUser: SharedPreferences
    lateinit var preferencesArticle: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesUser = getSharedPreferences(PREFS_U, Context.MODE_PRIVATE)
        preferencesArticle = getSharedPreferences(PREFS_A, Context.MODE_PRIVATE)
        supportFragmentManager.beginTransaction().add(R.id.container, LogInFragment()).commit()
        getAllChanges()
    }

    private fun getAllChanges(){
        if(getUserChanges().size > 0){
            User.users.clear()
            getUserChanges().forEach{
                User.users.add(it)
            }
        }
        if(getArticleChanges().size > 0){
            Article.Articles.clear()
            getArticleChanges().forEach{
                Article.Articles.add(it)
            }
        }
    }

    private fun getUserChanges(): MutableList<User>{
        val type = Types.newParameterizedType(MutableList::class.java, String::class.java)
        val adapterU = moshi.adapter<MutableList<User>>(type)

        return preferencesUser.getString(USER_PREFS, null)?.let {
            return@let try {
                adapterU.fromJson(it)
            } catch (e: Exception) {
                mutableListOf()
            }
        } ?: mutableListOf()
    }

    private fun getArticleChanges(): MutableList<Article>{
        val type = Types.newParameterizedType(MutableList::class.java, String::class.java)
        val adapterA = moshi.adapter<MutableList<Article>>(type)

        return preferencesUser.getString(USER_PREFS, null)?.let {
            return@let try {
                adapterA.fromJson(it)
            } catch (e: Exception) {
                mutableListOf()
            }
        } ?: mutableListOf()
    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right)
            replace(R.id.container, fragment)
            addToBackStack(fragment.tag)
            commit()
        }
    }

    fun replaceRemoveFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right)
            replace(R.id.container, fragment)
            addToBackStack(fragment.tag)
            commit()
        }
    }
}
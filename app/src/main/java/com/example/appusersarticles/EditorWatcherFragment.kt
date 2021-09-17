package com.example.appusersarticles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.squareup.moshi.Types

class EditorWatcherFragment : Fragment(R.layout.fragment_editor_watcher) {
    override fun onResume() {
        super.onResume()
        setView()
    }

    private lateinit var user: User
    private lateinit var ivUser: ImageView
    private lateinit var txvName: TextView
    private lateinit var txvUserType: TextView
    private lateinit var txvArticles: TextView
    private lateinit var txvInfo: TextView
    private lateinit var ivArticle: ImageView
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button
    private lateinit var btnVer: ImageView
    private lateinit var btnLike: ImageView
    private lateinit var btnAdd: Button
    private var articleUser: MutableList<Article> = mutableListOf()
    private var favFlag: Boolean = false
    var contadorCarousel = 0

    private fun setView(){
        ivUser = requireView().findViewById(R.id.ivUser)
        txvName = requireView().findViewById(R.id.txvName)
        txvUserType = requireView().findViewById(R.id.txvUserType)
        txvArticles = requireView().findViewById(R.id.txvArticles)
        txvInfo = requireView().findViewById(R.id.txvInfo)
        ivArticle = requireView().findViewById(R.id.ivArticle)
        btnBack = requireView().findViewById(R.id.btnBack)
        btnNext = requireView().findViewById(R.id.btnNext)
        btnVer = requireView().findViewById(R.id.btnVer)
        btnLike = requireView().findViewById(R.id.btnLike)
        btnAdd = requireView().findViewById(R.id.btnAdd)


        user = requireArguments().getParcelable("userLogin") ?: User()
        ivUser.setImageResource(user.picture!!)
        txvName.text = "Usuario: " + user.username
        txvUserType.text = "Nivel: " + user.type.toString()

        if(user.type == UserLevel.EDITOR){
            btnAdd.isVisible = true
            btnLike.isVisible = false
            txvArticles.text = "Articulos Escritos: " + user.numArticles.toString()
            loadArticles(user.id!!)
        }else{
            btnAdd.isVisible = false
            btnLike.isVisible = true
            articleUser = Article.Articles
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvArticles.text = "Mis Articulos Favs: " + user.favArticles.size.toString()
            txvInfo.text = articleUser[contadorCarousel].title
            validateLikes()
        }

        btnBack.setOnClickListener{
            lastImage()
        }
        btnNext.setOnClickListener{
            nextImage()
        }
        btnVer.setOnClickListener{
            watchArticle(contadorCarousel)
        }
        btnLike.setOnClickListener{
            getLike()
        }
        btnAdd.setOnClickListener{
            addArticle()
        }
    }

    private fun validateLikes(){
        if(user.favArticles.any { it.id == articleUser[contadorCarousel].id }) {
            btnLike.setImageResource(R.drawable.corazon_relleno)
            favFlag = true
        }else{
            favFlag = false
            btnLike.setImageResource(R.drawable.corazon)
        }
    }

    private fun loadArticles(id: Int){
        Article.Articles.forEach{
            if(it.user == id){
                articleUser.add(it)
                ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
                txvInfo.text = articleUser[contadorCarousel].title
            }
        }
        if(articleUser.size == 0){
            ivArticle.setImageResource(R.drawable.noarticle)
            txvInfo.text = "Este editor no tiene articulos"
            btnNext.isVisible = false
            btnBack.isVisible = false
            btnVer.isVisible = false
        }
    }

    private fun lastImage(){
        contadorCarousel--
        if(contadorCarousel >= 0){
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.text = articleUser[contadorCarousel].title
            validateLikes()
        }else{
            contadorCarousel = articleUser.size - 1
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.text = articleUser[contadorCarousel].title
            validateLikes()
        }
    }

    private fun nextImage(){
        contadorCarousel++
        if(contadorCarousel < articleUser.size){
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.text = articleUser[contadorCarousel].title
            validateLikes()
        }else{
            contadorCarousel = 0
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.text = articleUser[contadorCarousel].title
            validateLikes()
        }
    }

    private fun watchArticle(contadorCarousel: Int){
        (requireActivity() as MainActivity).replaceFragment(DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userLogin", user)
                putInt("idArticle", articleUser[contadorCarousel].id!!)
            }
        })
    }

    private fun getLike(){
        if(user.favArticles.any { it.id == articleUser[contadorCarousel].id }){
            btnLike.setImageResource(R.drawable.corazon)
            favFlag = false
            articleUser[contadorCarousel].likes = articleUser[contadorCarousel].likes!! - 1
            user.favArticles.removeIf{it.id == articleUser[contadorCarousel].id}
            Article.Articles.find{it.id == articleUser[contadorCarousel].id }?.likes = articleUser[contadorCarousel].likes
            saveChanges()
        }else{
            btnLike.setImageResource(R.drawable.corazon_relleno)
            favFlag = true
            articleUser[contadorCarousel].likes = articleUser[contadorCarousel].likes!! + 1
            user.favArticles.add(articleUser[contadorCarousel])
            Article.Articles.find{it.id == articleUser[contadorCarousel].id }?.likes = articleUser[contadorCarousel].likes
            saveChanges()
        }
    }

    private fun addArticle(){
        (requireActivity() as MainActivity).replaceFragment(DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userLogin", user)
                putBoolean("addNew", true)
            }
        })
    }

    private fun saveChanges(){
        val typeU = Types.newParameterizedType(MutableList::class.java, User::class.java)
        val typeA = Types.newParameterizedType(MutableList::class.java, Article::class.java)
        val adapterU = (requireActivity() as MainActivity).moshi.adapter<MutableList<User>>(typeU)
        val adapterA = (requireActivity() as MainActivity).moshi.adapter<MutableList<Article>>(typeA)

        User.users.find{it.id == user.id}?.favArticles = user.favArticles
        (requireActivity() as MainActivity).preferencesUser.edit().putString((requireActivity() as MainActivity).USER_PREFS,adapterU.toJson(User.users)).commit()
        (requireActivity() as MainActivity).preferencesArticle.edit().putString((requireActivity() as MainActivity).ARTICLE_PREFS,adapterA.toJson(Article.Articles)).commit()
    }
}
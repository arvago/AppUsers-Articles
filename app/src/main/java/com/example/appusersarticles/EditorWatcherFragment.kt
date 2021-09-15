package com.example.appusersarticles

import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible

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
        txvName.setText("Usuario: " + user.username)
        txvUserType.setText("Nivel: " + user.type.toString())

        if(user.type == UserLevel.EDITOR){
            btnAdd.isVisible = true
            btnLike.isVisible = false
            txvArticles.setText("Articulos Escritos: " + user.numArticles.toString())
            loadArticles(user.id!!)
        }else{
            btnAdd.isVisible = false
            btnLike.isVisible = true
            articleUser = Article.Articles
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvArticles.setText("Mis Articulos Favs: " + user.favArticles.toString())
            txvInfo.setText(articleUser[contadorCarousel].title)
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

    private fun loadArticles(id: Int){
        Article.Articles.forEach{
            if(it.user == id){
                articleUser.add(it)
                ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
                txvInfo.setText(articleUser[contadorCarousel].title)
            }
            if(articleUser.size == 0){
                ivArticle.setImageResource(com.example.appusersarticles.R.drawable.noarticle)
                txvInfo.setText("Este editor no tiene articulos")
                btnNext.isVisible = false
                btnBack.isVisible = false
                btnVer.isVisible = false
            }
        }

    }

    private fun lastImage(){
        contadorCarousel--
        if(contadorCarousel >= 0){
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.setText(articleUser[contadorCarousel].title)
        }else{
            contadorCarousel = articleUser.size - 1
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.setText(articleUser[contadorCarousel].title)
        }
    }

    private fun nextImage(){
        contadorCarousel++
        if(contadorCarousel < articleUser.size){
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.setText(articleUser[contadorCarousel].title)
        }else{
            contadorCarousel = 0
            ivArticle.setImageResource(articleUser[contadorCarousel].picture!!.image)
            txvInfo.setText(articleUser[contadorCarousel].title)
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
        btnLike.setImageResource(R.drawable.corazon_relleno)
        articleUser[contadorCarousel].likes = articleUser[contadorCarousel].likes!! + 1
        Article.Articles.forEach{
            when(it.id){
                articleUser[contadorCarousel].id -> it.likes = articleUser[contadorCarousel].likes
            }
        }
    }

    private fun addArticle(){
        (requireActivity() as MainActivity).replaceFragment(DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userLogin", user)
            }
        })
    }
}
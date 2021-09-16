package com.example.appusersarticles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible


class DetailFragment : Fragment(R.layout.fragment_detail) {
    override fun onResume() {
        super.onResume()
        setView()
    }

    private lateinit var user: User
    private var idImage = 0
    private var arrayImages: MutableList<ArticleType> = mutableListOf()
    private lateinit var ivArticle: ImageView
    private lateinit var txvLikes: TextView
    private lateinit var edtTitulo: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button
    var contadorCarousel = 0

    private fun setView(){
        user = requireArguments().getParcelable("userLogin") ?: User()
        idImage = requireArguments().getInt("idArticle")
        ivArticle = requireView().findViewById(R.id.ivArticle)
        txvLikes = requireView().findViewById(R.id.txvLikes)
        edtTitulo = requireView().findViewById(R.id.edtTitulo)
        edtDescripcion = requireView().findViewById(R.id.edtDescripcion)
        btnUpdate = requireView().findViewById(R.id.btnUpdate)
        btnDelete = requireView().findViewById(R.id.btnDelete)
        btnBack = requireView().findViewById(R.id.btnBack)
        btnNext = requireView().findViewById(R.id.btnNext)
        arrayImages.addAll(ArticleType.values())

        if(user.type == UserLevel.EDITOR){
            btnBack.isVisible = true
            btnNext.isVisible = true
            btnUpdate.isVisible = true
            btnDelete.isVisible = true
            edtDescripcion.isEnabled = true
            edtTitulo.isEnabled = true
            if(idImage != 0){
                loadArticle(idImage)
            }else{
                ivArticle.setImageResource(arrayImages[contadorCarousel].image)
                btnDelete.isVisible = false
                txvLikes.text = "0"
            }
        }else{
            btnBack.isVisible = false
            btnNext.isVisible = false
            btnUpdate.isVisible = false
            btnDelete.isVisible = false
            edtDescripcion.isEnabled = false
            edtTitulo.isEnabled = false
            loadArticle(idImage)
        }

        btnBack.setOnClickListener{
            lastImage()
        }
        btnNext.setOnClickListener{
            nextImage()
        }
        btnDelete.setOnClickListener{
            deleteArticle()
        }
        btnUpdate.setOnClickListener{
            updateArticle()
        }
    }

    private fun loadArticle(idImage: Int){
        Article.Articles.forEach{
            if(it.id == idImage){
                txvLikes.text = it.likes.toString()
                edtTitulo.setText(it.title)
                edtDescripcion.setText(it.description)
                ivArticle.setImageResource(it.picture!!.image)
                contadorCarousel = it.picture!!.id - 1
            }
        }
    }

    private fun lastImage(){
        contadorCarousel--
        if(contadorCarousel >= 0){
            ivArticle.setImageResource(arrayImages[contadorCarousel].image)
        }else{
            contadorCarousel = arrayImages.size - 1
            ivArticle.setImageResource(arrayImages[contadorCarousel].image)
        }
    }

    private fun nextImage(){
        contadorCarousel++
        if(contadorCarousel < arrayImages.size){
            ivArticle.setImageResource(arrayImages[contadorCarousel].image)
        }else{
            contadorCarousel = 0
            ivArticle.setImageResource(arrayImages[contadorCarousel].image)
        }
    }

    private fun deleteArticle(){
        Article.Articles.removeIf{it.id == idImage}
        user.numArticles = user.numArticles!! - 1
        Toast.makeText(context, "Articulo Eliminado", Toast.LENGTH_SHORT).show()
        (requireActivity() as MainActivity).replaceRemoveFragment(EditorWatcherFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userLogin", user)
            }
        })
    }

    private fun updateArticle(){
        user.numArticles = user.numArticles!! + 1
    }
}
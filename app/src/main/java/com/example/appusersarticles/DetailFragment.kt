package com.example.appusersarticles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.squareup.moshi.Types

class DetailFragment : Fragment(R.layout.fragment_detail) {
    override fun onResume() {
        super.onResume()
        setView()
    }

    private lateinit var user: User
    private var idImage = 0
    private var addFlag = false
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
        addFlag = requireArguments().getBoolean("addNew")
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
            updateNewArticle()
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
        saveChanges()
        (requireActivity() as MainActivity).replaceRemoveFragment(EditorWatcherFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userLogin", user)
            }
        })
    }

    private fun updateNewArticle(){
        if(addFlag){
            var newArticle: Article = Article()
            var newID = Article.Articles.last().id!! + 1
            newArticle.id = newID
            newArticle.title = edtTitulo.text.toString()
            newArticle.description = edtDescripcion.text.toString()
            newArticle.picture = arrayImages[contadorCarousel]
            newArticle.likes = 0
            newArticle.user = user.id
            Article.Articles.add(newArticle)
            user.numArticles = user.numArticles!! + 1
            Toast.makeText(context, "Articulo Agregado", Toast.LENGTH_SHORT).show()
        }else {
            Article.Articles.forEach {
                if (it.id == idImage) {
                    it.title = edtTitulo.text.toString()
                    it.description = edtDescripcion.text.toString()
                    it.picture = arrayImages[contadorCarousel]
                }
            }
            Toast.makeText(context, "Articulo Actualizado", Toast.LENGTH_SHORT).show()
        }
        saveChanges()
        (requireActivity() as MainActivity).replaceRemoveFragment(EditorWatcherFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userLogin", user)
            }
        })
    }

    private fun saveChanges(){
        val typeU = Types.newParameterizedType(MutableList::class.java, User::class.java)
        val typeA = Types.newParameterizedType(MutableList::class.java, Article::class.java)
        val adapterU = (requireActivity() as MainActivity).moshi.adapter<MutableList<User>>(typeU)
        val adapterA = (requireActivity() as MainActivity).moshi.adapter<MutableList<Article>>(typeA)

        User.users.find{it.id == user.id}?.numArticles = user.numArticles
        (requireActivity() as MainActivity).preferencesUser.edit().putString((requireActivity() as MainActivity).USER_PREFS,adapterU.toJson(User.users)).commit()
        (requireActivity() as MainActivity).preferencesArticle.edit().putString((requireActivity() as MainActivity).ARTICLE_PREFS,adapterA.toJson(Article.Articles)).commit()
    }
}
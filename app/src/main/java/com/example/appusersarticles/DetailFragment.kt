package com.example.appusersarticles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible


class DetailFragment : Fragment(R.layout.fragment_detail) {
    override fun onResume() {
        super.onResume()
        setView()
    }

    private lateinit var user: User
    private var idImage = 0
    private lateinit var ivArticle: ImageView
    private lateinit var txvLikes: TextView
    private lateinit var edtTitulo: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

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
    }

    private fun loadArticle(idImage: Int){
        Article.Articles.forEach{
            if(it.id == idImage){
                txvLikes.text = it.likes.toString()
                edtTitulo.setText(it.title)
                edtDescripcion.setText(it.description)
                ivArticle.setImageResource(it.picture!!.image)
            }
        }
    }
}
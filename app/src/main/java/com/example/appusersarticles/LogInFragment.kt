package com.example.appusersarticles

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class LogInFragment : Fragment(R.layout.fragment_log_in) {
    override fun onResume() {
        super.onResume()
        initView()
    }

    private lateinit var imvPicture: ImageView
    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnLogin: Button

    private fun initView() {
        imvPicture = requireView().findViewById(R.id.imvUser)
        txtUsername = requireView().findViewById(R.id.edtUsername)
        txtPassword = requireView().findViewById(R.id.edtPassword)
        btnLogin = requireView().findViewById(R.id.btnLogIn)

        imvPicture.setImageResource(R.drawable.users)
        txtUsername.setText("")
        txtPassword.setText("")
        txtPassword.isEnabled = false
        btnLogin.isEnabled = false

        txtUsername.addTextChangedListener(textWatcherUsername)
        txtPassword.addTextChangedListener(textWatcherPassword)
        btnLogin.setOnClickListener{
            validateCredential()
        }
    }

    private val textWatcherUsername = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            User.users.forEach {
                if(p0.toString() == it.username){
                    imvPicture.setImageResource(it.picture!!)
                }
            }
            if(txtUsername.text.isNotEmpty()){
                txtPassword.isEnabled = true
            }else{
                txtPassword.isEnabled = false
                txtPassword.setText("")
            }

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private val textWatcherPassword = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(p0: Editable?) {
            btnLogin.isEnabled = txtPassword.text.isNotEmpty()
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun validateCredential(){
        User.users.forEach{
            if((txtUsername.text.toString() == it.username) && (txtPassword.text.toString() == it.password)){
                (requireActivity() as MainActivity).replaceFragment(EditorWatcherFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("userLogin", it)
                    }
                })
            }else{
                Toast.makeText(context, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

/*------------------------*/
/*--By Arturo Vallejo v1--*/
/*------------------------*/
package com.example.mobilecrud

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserDialog(context: Context, private val user: User) : Dialog(context) {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var telephoneEditText: EditText
    private lateinit var adresseEditText: EditText
    private lateinit var villeEditText: EditText
    private lateinit var codePostalEditText: EditText
    private lateinit var paysEditText: EditText
    private lateinit var dateNaissanceEditText: EditText
    private lateinit var genreEditText: EditText
    private lateinit var photoEditText: EditText
    private lateinit var roleEditText: EditText

    private val service = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_update_user)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        telephoneEditText = findViewById(R.id.telephoneEditText)
        adresseEditText = findViewById(R.id.adresseEditText)
        villeEditText = findViewById(R.id.villeEditText)
        codePostalEditText = findViewById(R.id.codePostalEditText)
        paysEditText = findViewById(R.id.paysEditText)
        dateNaissanceEditText = findViewById(R.id.dateNaissanceEditText)
        genreEditText = findViewById(R.id.genreEditText)
        photoEditText = findViewById(R.id.photoEditText)
        roleEditText = findViewById(R.id.roleEditText)

        nameEditText.setText(user.name)
        emailEditText.setText(user.email)
        telephoneEditText.setText(user.telephone)
        adresseEditText.setText(user.adresse)
        villeEditText.setText(user.ville)
        codePostalEditText.setText(user.codePostal)
        paysEditText.setText(user.pays)
        dateNaissanceEditText.setText(user.dateNaissance)
        genreEditText.setText(user.genre)
        photoEditText.setText(user.photo)
        roleEditText.setText(user.role)

        findViewById<Button>(R.id.updateButton).setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val telephone = telephoneEditText.text.toString()
            val adresse = adresseEditText.text.toString()
            val ville = villeEditText.text.toString()
            val codePostal = codePostalEditText.text.toString()
            val pays = paysEditText.text.toString()
            val dateNaissance = dateNaissanceEditText.text.toString()
            val genre = genreEditText.text.toString()
            val photo = photoEditText.text.toString()
            val role = roleEditText.text.toString()

            val updatedUser = User(
                user.id,
                name,
                email,
                telephone,
                adresse,
                ville,
                codePostal,
                pays,
                dateNaissance,
                genre,
                photo,
                role
            )

            service.updateUser(user.id, updatedUser).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        dismiss()
                        Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error updating user", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(context, "Error updating user", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
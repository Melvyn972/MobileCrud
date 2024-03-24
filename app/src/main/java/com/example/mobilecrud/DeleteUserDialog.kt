package com.example.mobilecrud

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteUserDialog(context: Context, private val user: User) : Dialog(context) {

    private lateinit var nameTextView: TextView

    private val service = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete_user)

        nameTextView = findViewById(R.id.nameTextView)
        nameTextView.text = user.name

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            service.deleteUser(user.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        dismiss()
                        Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
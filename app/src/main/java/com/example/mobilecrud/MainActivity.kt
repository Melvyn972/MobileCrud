package com.example.mobilecrud

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        GlobalScope.launch(Dispatchers.IO) {
            val users = getUsers(1)
            withContext(Dispatchers.Main) {
                viewAdapter = UserAdapter(users)
                recyclerView.adapter = viewAdapter
            }
        }
    }

    private suspend fun getUsers(page: Int): List<User> {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(UserService::class.java)
        val usersCall = service.getUsers(page)
        val usersResponse = usersCall.execute()

        if (usersResponse.isSuccessful) {
            return usersResponse.body() ?: emptyList()
        } else {
            throw RuntimeException("Failed to get users: ${usersResponse.message()}")
        }
    }

    /*private suspend fun getUsers(page: Int): List<User> {
        val url = "https://10.0.2.2:8000/api"
        val connection = URL(url).openConnection() as HttpURLConnection

        try {
            connection.doOutput = false
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
                // Parse the response body to List<User> (implementation depends on format)
                return parseJsonToListUser(responseBody) // Replace with your parsing logic
            } else {
                throw RuntimeException("Failed to get users: ${connection.responseCode}")
            }
        } finally {
            connection.disconnect()
        }
    }

    // This function needs to be implemented based on your data format (JSON, etc.)
    private fun parseJsonToListUser(jsonString: String): List<User> {
        val gson = Gson()
        val userJsonList = gson.fromJson(jsonString, Array<User>::class.java).toList()
        return userJsonList
    }*/
}
package com.example.mobilecrud

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.CertificatePinner
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    // Classe pour faire confiance à tous les certificats SSL
    class TrustAllCerts : X509TrustManager {
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {
            // Ne rien faire (faire confiance à tout)
        }

        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {
            // Ne rien faire (faire confiance à tout)
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf() // Retourne un tableau vide pour faire confiance à tous les certificats
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        // Configuration de Retrofit avec la désactivation de la vérification SSL
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            val users = getUsers(retrofit, 1)
            withContext(Dispatchers.Main) {
                viewAdapter = UserAdapter(users)
                recyclerView.adapter = viewAdapter
            }
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val trustAllCerts = TrustAllCerts()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustAllCerts), java.security.SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .build()
    }

    private suspend fun getUsers(retrofit: Retrofit, page: Int): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val service = retrofit.create(UserService::class.java)
                val usersCall = service.getUsers(page)
                val usersResponse = usersCall.execute()

                if (usersResponse.isSuccessful) {
                    usersResponse.body()?.toList() ?: emptyList()
                } else {
                    // Affiche une boîte de dialogue avec les détails de l'exception
                    val errorMessage =
                        "Code de statut : ${usersResponse.code()}\nMessage : ${usersResponse.message()}"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                    }
                    throw RuntimeException("Failed to get users: ${errorMessage}")
                }
            } catch (e: Exception) {
                // Affiche une boîte de dialogue avec les détails de l'exception
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_LONG).show()
                }
                throw e
            }
        }
    }
}
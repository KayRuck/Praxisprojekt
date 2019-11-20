package com.example.praxisprojekt.fragmente

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.praxisprojekt.R
import com.example.praxisprojekt.retrofit.RetroService
import com.example.praxisprojekt.viewModels.SearchViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.android.synthetic.main.search_fragment.view.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.search_fragment, container, false)
        val btn: Button = rootView.search_btn
        btn.setOnClickListener {

            runBlocking {


                val result = HttpClient().get<String>("192.168.0.185:5555/")
                val tw: TextView = rootView.search_tw
                tw.text = result

            }

        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }


}

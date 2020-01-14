package com.maq.gameapplication.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maq.gameapplication.HeadlineViewModel
import com.maq.gameapplication.R
import com.maq.gameapplication.data.Headline
import com.maq.gameapplication.data.HeadlineRepository
import com.maq.gameapplication.database.HeadlineDatabase
import com.maq.gameapplication.database.Headlinedao
import com.maq.gameapplication.databinding.ActivityMainBinding
import com.maq.propertyapp.network.HeadlineApi
import com.maq.propertyapp.properties.HeadlineViewModelFactory
import com.noorlabs.calcularity.interfaces.HeadlineListener
import kotlinx.android.synthetic.main.custom_toast.*


class MainActivity : AppCompatActivity(),HeadlineListener {

    private  lateinit var viewModel: HeadlineViewModel
    private lateinit var factory: HeadlineViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private lateinit var headlinedao: Headlinedao
    private var i:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //databinding
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        //check if phone is connected to internet
        if(isNetworkConnected() == false){
            displayToast("Connect your phone to internet and click refresh")
            return
        }

        //setup the UI
        setupUI()
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

    private fun setupUI() {

        //display progress bar and toast
        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        displayToast("Loading")

        //setup api and repository to read from JSON URL
        val api = HeadlineApi()

        // Gets reference to Headlinedao from HeadlineDatabase to construct the correct HeadlineRepository.
        headlinedao = HeadlineDatabase.getDatabase(application).getHeadlineDao()
        val repository = HeadlineRepository(api,headlinedao)

        //setup view model
        factory =
            HeadlineViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(HeadlineViewModel::class.java)

        //get data from json reponse
        viewModel.getHeadlines()

        //setup databinding
        binding.headline = viewModel

        //setup listener
        viewModel.headlineListener = this


        //observe the data that is fetched from JSON and add it to Room database
        viewModel.headlines.observe(this, Observer { headlines ->

            val headline = Headline(headlines.items,headlines.product,headlines.resultSize,headlines.version)
            viewModel.insertHeadlines(headline)
            Log.i("Created","value inserted")

            //initiate data fetch from Room database
            viewModel.getHeadlinesDb()

        })

        //fetch data from Room database and perform databinding to layout
        viewModel.headlinesfetch.observe(this, Observer {
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE

            //display the first entry on screen
            binding.headlineItem = viewModel.headlines.value?.items?.get(0)

        })



    }

    //function called when a headline is clicked
    override fun onHeadlineClick(view:View){

        val b = view as Button
        val buttonText = b.text.toString()

        //get current score of user
        val scoreText = binding.score.text.toString()
        var score = scoreText.toInt()

        viewModel.headlinesfetch.observe(this, Observer { headlines ->

            //update score of user based on selected option. 2 for correct and -1 for wrong
            val correctAnswerIndex = headlines.items[i].correctAnswerIndex
            val correctAnswer = headlines.items[i].headlines[correctAnswerIndex]
            if(buttonText.equals(correctAnswer)){
                score = score + 2
                binding.info.text = getString(R.string.correct)
                binding.info.setTextColor(ContextCompat.getColor(
                    this, R.color.green))
            }else{
                score = score - 1
                val text = getString(R.string.incorrect)+ " `"+correctAnswer+"`"
                binding.info.text = text
                binding.info.setTextColor(ContextCompat.getColor(this, R.color.red))

            }

            binding.score.text = score.toString()
            binding.buttonNextquestion.visibility = View.VISIBLE
            binding.info.visibility = View.VISIBLE
            binding.buttonHeadline01.visibility = View.GONE
            binding.buttonHeadline02.visibility = View.GONE
            binding.buttonHeadline03.visibility = View.GONE
            binding.buttonSkip.isEnabled = false



        })
    }

    //function to skip to next question
    override fun onSkip(){

        viewModel.headlinesfetch.observe(this, Observer {

            //iterate to next entry in Room database
            i = i + 1
            binding.headlineItem = viewModel.headlines.value?.items?.get(i)


        })
    }

    //function to read article in browser
    override fun onReadMore(){

        viewModel.headlinesfetch.observe(this, Observer { headlines ->

            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(headlines.items[i].storyUrl)
            startActivity(openURL)


        })
    }

    //function to move to next question after submitting answer
    override fun onNextQuestion(){

        viewModel.headlinesfetch.observe(this, Observer {

            //iterate to next question in Room database
            i = i + 1
            binding.headlineItem = viewModel.headlines.value?.items?.get(i)

            binding.buttonNextquestion.visibility = View.GONE
            binding.info.visibility = View.GONE
            binding.buttonHeadline01.visibility = View.VISIBLE
            binding.buttonHeadline02.visibility = View.VISIBLE
            binding.buttonHeadline03.visibility = View.VISIBLE
            binding.buttonSkip.isEnabled = true


        })
    }

    fun displayToast(message:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast = Toast(applicationContext)
        myToast.setGravity(Gravity.TOP,0,200)
        myToast.view = layout
        val toastText = layout.findViewById(R.id.custom_toast_message) as TextView
        toastText.text = message

        myToast.show()
    }

}

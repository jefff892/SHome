package com.example.vlahprojekt

import android.widget.TextView
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.pp.R
import com.example.pp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


val array = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)

class MainActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    var garazaValue: Boolean = true
    var kapijaValue: Boolean = true
    var kuhinjaValue: Boolean = true
    var sobaValue: Boolean = true
    var musicValue: Boolean = true
    var securityValue: Boolean = true
    var curtainsValue: Boolean = true
    var doorValue: Boolean = true
    var lightsValue: Boolean = true
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var temperatureSeekBar: SeekBar
    private lateinit var temperatureTextView: TextView
    private lateinit var database: DatabaseReference
    private lateinit var tvA: TextView
    private lateinit var tvTerm: TextView
    private lateinit var tvNap: TextView
    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tvA = binding.tvAm
        tvTerm= binding.tvTerm
        tvNap= binding.tvNap
        handler = Handler(Looper.getMainLooper())
        main()
        binding.apply {
            lineChart.gradientFillColors = intArrayOf(
                Color.parseColor("#64B5F6"),
                Color.TRANSPARENT
            )
            lineChart.animation.duration = animationDuration
            lineChart.onDataPointTouchListener = { index, _, _ ->
            }
            garazaButton.setOnClickListener { garazab() }//TEST
            kapijaButton.setOnClickListener { kapijab() }
            kuhinjaButton.setOnClickListener { kuhinjab() }
            sobaButton.setOnClickListener { sobab() }
            lightsButton.setOnClickListener { lightsb() }
            musicButton.setOnClickListener { musicb() }
            doorButton.setOnClickListener { doorb() }
            curtainsButton.setOnClickListener { curtainsb() }
            securityButton.setOnClickListener { securityb() }
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.redSeekBar.setOnSeekBarChangeListener(SeekBarListener())
        binding.greenSeekBar.setOnSeekBarChangeListener(SeekBarListener())
        binding.blueSeekBar.setOnSeekBarChangeListener(SeekBarListener())

        temperatureSeekBar = findViewById(R.id.temperatureSeekBar)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        temperatureTextView.text = "${temperatureSeekBar.progress}°C"
        temperatureSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                temperatureTextView.text = "$progress°C"
                database = FirebaseDatabase.getInstance().getReference("test")
                database.child("temp").setValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle touch start
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle touch stop
            }
        })

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            PackageManager.PERMISSION_GRANTED
        )
        var intentRecognizer = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intentRecognizer.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        val button = findViewById<Button>(R.id.btnStart)
        button.setOnClickListener {
            val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            }
            speechRecognizer.startListening(recognizerIntent)
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {}
                override fun onResults(bundle: Bundle) {
                    val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    var string: String? = ""
                    if (matches != null) {
                        string = matches[0]
                        when (string.toLowerCase()) {
                            "garage" -> {
                                garazab()
                            }

                            "room" -> {
                                sobab()
                            }

                            "music" -> {
                                musicb()
                            }

                            "kitchen" -> {
                                kuhinjab()
                            }

                            "gate" -> {
                                kapijab()
                            }

                            "lights" -> {
                                lightsb()
                            }

                            "security" -> {
                                securityb()
                            }

                            "curtains" -> {
                                curtainsb()
                            }

                            "door lock" -> {
                                doorb()
                            }
                        }
                    }
                }

                override fun onPartialResults(partialResults: Bundle) {}
                override fun onEvent(eventType: Int, params: Bundle) {}
            })
        }
    }

    private fun garazab() {
        garazaValue = !garazaValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("garaza").setValue(garazaValue)
    }

    private fun kuhinjab() {
        kuhinjaValue = !kuhinjaValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("kuhinja").setValue(kuhinjaValue)
    }

    private fun kapijab() {
        kapijaValue = !kapijaValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("kapija").setValue(kapijaValue)
    }

    private fun sobab() {
        sobaValue = !sobaValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("soba").setValue(sobaValue)
    }

    private fun lightsb() {
        lightsValue = !lightsValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("lights").setValue(lightsValue)
    }

    private fun securityb() {
        securityValue = !securityValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("security").setValue(securityValue)
    }

    private fun musicb() {
        musicValue = !musicValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("music").setValue(musicValue)
    }

    private fun doorb() {
        doorValue = !doorValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("door").setValue(doorValue)
    }

    private fun curtainsb() {
        curtainsValue = !curtainsValue
        database = FirebaseDatabase.getInstance().getReference("test")
        database.child("curtains").setValue(curtainsValue)
    }

    private fun updateLineSet(): List<Pair<String, Float>> {
        return listOf(
            "LABEL1" to array[0],
            "LABEL2" to array[1],
            "LABEL3" to array[2],
            "LABEL4" to array[3],
            "LABEL5" to array[4],
            "LABEL6" to array[5],
            "LABEL7" to array[6],
        )
    }
    // Call updateLineSet whenever needed
    fun main() {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                for (i in 0 until array.size - 1) {
                    array[i] = array[i + 1]
                }
                readgr()
                val updatedLineSet = updateLineSet()
                handler.post {
                    binding.lineChart.animate(updatedLineSet)
                }
                termometar()
                struja()
            }
        }

        timer.scheduleAtFixedRate(task, 0, 2000)
    }

    private fun readgr() {
        database = FirebaseDatabase.getInstance().getReference("graf")
        database.child("napon").get().addOnSuccessListener {
            array[6] = it.value.toString().toFloat()
            tvNap.setText(it.value.toString() + " V")
        }
    }
    private fun termometar() {
        database = FirebaseDatabase.getInstance().getReference("graf")
        database.child("termometar").get().addOnSuccessListener {
            tvTerm.setText(it.value.toString() + " °C")
        }
    }
    private fun struja() {
        database = FirebaseDatabase.getInstance().getReference("graf")
        database.child("struja").get().addOnSuccessListener {
            tvA.setText(it.value.toString() + " A")
        }
    }

    companion object {
        private const val animationDuration = 0L
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle back button click
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    inner class SeekBarListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val red = binding.redSeekBar.progress
            val green = binding.greenSeekBar.progress
            val blue = binding.blueSeekBar.progress

            // Update color preview
            binding.colorPreview.setBackgroundColor(Color.rgb(red, green, blue))
            database = FirebaseDatabase.getInstance().getReference("test")
            database.child("r").setValue(red)
            database.child("g").setValue(green)
            database.child("b").setValue(blue)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    fun stopBtn(view: View) {
        speechRecognizer?.stopListening()
    }
}
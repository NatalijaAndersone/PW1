package com.android.example.pw1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import android.Manifest

import android.content.pm.PackageManager
import android.media.MediaRecorder

import android.os.Environment

import android.widget.Button
import android.widget.ListView

import androidx.core.app.ActivityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.IOException

class MainActivity3 : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics
    private val recordings = mutableListOf<File>()
    private lateinit var recordingAdapter: RecordingAdapter

    private lateinit var recordButton: Button
    private lateinit var recordingsList: ListView

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics

        recordButton = findViewById(R.id.record_button)
        recordingsList = findViewById(R.id.recordings_list)

        recordingAdapter = RecordingAdapter(this, recordings)
        recordingsList.adapter = recordingAdapter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            initRecorder()
        }

        recordButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }
    }

    private fun initRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(getOutputFile().absolutePath)
    }

    private fun startRecording() {
        analytics.logEvent("Start_audio_recording") {
            param("Screen_location", "MainActivity3")
        }
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
            recordButton.text = "Stop"
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        analytics.logEvent("Stop_audio_recording") {
            param("Screen_location", "MainActivity3")
        }
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        isRecording = false
        recordButton.text = "Record"
        addRecordingToList()
    }

    private fun getOutputFile(): File {
        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/Recordings/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, "recording_${System.currentTimeMillis()}.mp4")
    }

    private fun addRecordingToList() {
        val file = recordings.firstOrNull { it.name == getOutputFile().name }

        if (file == null) {
            recordings.add(getOutputFile())
            recordingAdapter.notifyDataSetChanged()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            initRecorder()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.audio_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.take_images -> {
                analytics.logEvent("Go_to_camera") {
                    param("Screen_location", "MainActivity3")
                }
                val record = Intent(this, MainActivity::class.java)
                startActivity(record)
                true
            }
            R.id.delete -> {
                analytics.logEvent("Delete_audio") {
                    param("Screen_location", "MainActivity3")
                }

                val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/Recordings/")
                if (dir.exists()) {
                    dir.listFiles()?.forEach { file ->
                        if (file.exists()) {
                            file.delete()
                        }
                    }
                }
                recordings.clear()
                recordingAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


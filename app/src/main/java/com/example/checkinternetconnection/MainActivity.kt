package com.example.checkinternetconnection

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (networkAvailable()) {
            Toast.makeText(this,"Internet is available",Toast.LENGTH_SHORT).show()
        }else{
            internetDialog()
        }
    }

    private fun networkAvailable():Boolean{
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager !=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (networkCapabilities !=null){
                    return  if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        true
                    }else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        true
                    }else networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                }
            }else{
                val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                return networkInfo !=null && networkInfo.isConnectedOrConnecting
            }
        }
        return false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun internetDialog() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels * 0.75
        val height = displayMetrics.heightPixels * 0.3
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.check_internet_dialog, null)
        builder.setView(view)
        val btnRetry = view.findViewById<AppCompatButton>(R.id.btnRetry)
        val dialog = builder.create()
        dialog.show()
        dialog.window!!.setLayout(width.toInt(), height.toInt())
        dialog.setCancelable(false)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.setBackgroundDrawable(this.resources.getDrawable(R.drawable.internet_dialog))
        btnRetry.setOnClickListener {
            dialog.dismiss()
            if (networkAvailable()) {
                Toast.makeText(this,"Internet is available", Toast.LENGTH_SHORT).show()
            } else {
                internetDialog()
            }
        }
    }
}
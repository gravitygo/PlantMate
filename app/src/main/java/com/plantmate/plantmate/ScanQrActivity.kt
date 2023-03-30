package com.plantmate.plantmate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.plantmate.plantmate.databinding.ActivityScanQrBinding
import com.plantmate.plantmate.fragments.FragmentTopNav
import com.plantmate.plantmate.objects.FragmentUtils.replaceFragmentInit
import com.plantmate.plantmate.objects.FullScreenUtils.setFullScreen

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ScanQrActivity : AppCompatActivity(){

    private lateinit var binding: ActivityScanQrBinding
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private var detector: BarcodeDetector? = null

    override fun onResume() {
        super.onResume()
        detector?.setProcessor(processor)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        // set binding and fullscreen


        super.onCreate(savedInstanceState)
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreen(this)

        // replace fragment
        val topNav = FragmentTopNav(getColor(R.color.primary))
        replaceFragmentInit(topNav, R.id.top_Panel, supportFragmentManager)

        if(ContextCompat.checkSelfPermission(
                this@ScanQrActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_DENIED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }


    }

    private fun setupControls() {
        binding.cameraSurfaceView.visibility = View.VISIBLE
        detector = BarcodeDetector.Builder(this@ScanQrActivity).build()
        cameraSource = CameraSource.Builder(this@ScanQrActivity, detector!!)
            .setAutoFocusEnabled(true)
            .build()
        binding.cameraSurfaceView.holder.addCallback(surfaceCallBack)
        detector!!.setProcessor(processor)
    }

    private fun askForCameraPermission(){
        binding.cameraSurfaceView.visibility = View.GONE
        ActivityCompat.requestPermissions(
            this@ScanQrActivity,
            arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupControls()
            } else{
                Toast.makeText(applicationContext, "Camera Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback {

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource.stop()
        }

        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        this@ScanQrActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    askForCameraPermission()
                }
                else{
                    cameraSource.start(surfaceHolder)
                }


            } catch (exception: Exception){
                Toast.makeText(applicationContext, "Camera Access Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {
        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            if (detections.detectedItems.isNotEmpty()){
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)

                detector!!.setProcessor(null)
                val intent = Intent(binding.root.context, ViewPlantActivity::class.java)
                intent.putExtra("Hi", code.displayValue)
                startActivity(intent)
            } else {
                binding.textScanResult.text = ""
            }
        }

    }
}
package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.AccountBottomDialogBinding
import Izaac.Doyle.PubsApp.databinding.FragmentJoinAddBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.DelicateCoroutinesApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class BottomJoinAddGroupFragment:BottomSheetDialogFragment() {

    private var _binding: FragmentJoinAddBinding? = null
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var scannedValue = ""
    private val PERMISSIONS_GRANTED = 101


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJoinAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setUpControls()
        }






        return root
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun setUpControls() {
        barcodeDetector = BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(requireContext(),barcodeDetector)
            .setRequestedPreviewSize(1920,1080)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            /**
             * This is called immediately after the surface is first created.
             * Implementations of this should start up whatever rendering code
             * they desire.  Note that only one thread can ever draw into
             * a [Surface], so you should not draw into the Surface here
             * if your normal rendering will be in another thread.
             *
             * @param holder The SurfaceHolder whose surface is being created.
             */
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }


            /**
             * This is called immediately after any structural changes (format or
             * size) have been made to the surface.  You should at this point update
             * the imagery in the surface.  This method is always called at least
             * once, after [.surfaceCreated].
             *
             * @param holder The SurfaceHolder whose surface has changed.
             * @param format The new [PixelFormat] of the surface.
             * @param width The new width of the surface.
             * @param height The new height of the surface.
             */
            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }

            /**
             * This is called immediately before a surface is being destroyed. After
             * returning from this call, you should no longer try to access this
             * surface.  If you have a rendering thread that directly accesses
             * the surface, you must ensure that thread is no longer touching the
             * Surface before returning from this function.
             *
             * @param holder The SurfaceHolder whose surface is being destroyed.
             */
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
                }
        })
        barcodeDetector.setProcessor(object: Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(requireContext(), "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
              val barcode = detections.detectedItems
                if (barcode.size() == 1){
                    scannedValue = barcode.valueAt(0).rawValue

                    GlobalScope.launch(Dispatchers.IO) {
                        cameraSource.stop()
                        Log.d("CameraScan",scannedValue)
                 //       Toast.makeText(requireContext(), scannedValue, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }

                }else{
                    Toast.makeText(requireContext(), "value - else", Toast.LENGTH_SHORT).show()
                }
            }

        })



    }
    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            PERMISSIONS_GRANTED
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_GRANTED && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpControls()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}


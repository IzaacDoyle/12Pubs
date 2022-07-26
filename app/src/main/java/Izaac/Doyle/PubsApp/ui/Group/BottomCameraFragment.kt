package Izaac.Doyle.PubsApp.ui.Group

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.databinding.CameraViewBinding
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class BottomCameraFragment: BottomSheetDialogFragment() {

    private var _binding: CameraViewBinding? = null

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var scannedValue = ""
    private val PERMISSIONS_GRANTED = 101
    lateinit var dataPasser : onDataPasser

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CameraViewBinding.inflate(inflater, container, false)
        val root: View = binding.root


        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
               askForCameraPermission()
        } else {
              setUpControls()
        }

        binding.cancelCamera.setOnClickListener {
            dataPasser.AccountStatus("Group",null,null)
            dismiss()
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

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }



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

                    GlobalScope.launch(Dispatchers.Main) {
                        cameraSource.stop()
                        Log.d("CameraScan",scannedValue)
                       //Toast.makeText(requireContext(), scannedValue, Toast.LENGTH_SHORT).show()
                        dataPasser.AccountStatus("Group",scannedValue,null)
                        dismiss()
                    }

                }
            }

        })



    }
    private fun askForCameraPermission() {

//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(android.Manifest.permission.CAMERA),
//            PERMISSIONS_GRANTED
//        )

        cameraPermission.launch(arrayOf(CAMERA))
    }

    val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permission ->
        permission.entries.forEach { _ ->
            if (permission[CAMERA]== true ){
                    setUpControls()
                }else{
                    Toast.makeText(requireContext(), "Permission for camera denied", Toast.LENGTH_SHORT).show()
                dismiss()
                }
        }
    }



//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)C
//        if (requestCode == PERMISSIONS_GRANTED && grantResults.isNotEmpty()) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                setUpControls()
//            } else {
//                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
//                dismiss()
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()

        val behavior = BottomSheetBehavior.from(requireView().parent as View)
//        behavior.peekHeight = 800
        behavior.isDraggable = false


//        behavior.maxHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as onDataPasser
    }




}
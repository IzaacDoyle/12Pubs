package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

public fun QrCodeDispay(context: Context,activity: Activity){

    val view = View.inflate(context, R.layout.qrcode_dispay,null)

    val builder = AlertDialog.Builder(context)
    builder.setView(view)
    val dialog = builder.create()
    dialog.show()
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val qrImageView = view.findViewById<ImageView>(R.id.QR_code_display)

    val writer = QRCodeWriter()
    try {

        val bitMatrix = writer.encode(CheckCurrentUser()!!.uid, BarcodeFormat.QR_CODE,512,512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565)
        for(x in 0 until width){
            for (y in 0 until height){
                bmp.setPixel(x,y,if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
            }
        }
        qrImageView.setImageBitmap(bmp)

    }catch (e: WriterException){
        e.printStackTrace()
    }

    dialog.setOnDismissListener {
        activity.recreate()

    }
}

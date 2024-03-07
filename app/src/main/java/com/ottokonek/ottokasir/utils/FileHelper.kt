package com.ottokonek.ottokasir.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.nio.channels.FileChannel


object FileHelper {

    val TAG = FileHelper::class.java.simpleName

    const val FILE_XLSX = ".xlsx"

    const val FOLDER_MAIN = "OttoKasir"
    const val FILE_REPORT_EXTENSION = FILE_XLSX
    const val FILE_REPORD_NAME = "Report"


    fun getPath(context: Context, uri: Uri): String? {
        val result: String?
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
            LogHelper(TAG, "file: ${destFile.absolutePath}").run()
        } finally {
            source?.close()
            destination?.close()
        }
    }

    fun fileImageToBase64(file: File): String {
        val bm = BitmapFactory.decodeFile(file.absolutePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos) //bm is the bitmap object

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    fun downloadFile(context: Context, url: String): String? {
        val namaFile = "OK_Transaction_" + DateUtil.getTimestamp()
        val outputFile = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS + File.separator + namaFile)

        var isSuccess = false
        CustomProgressDialog.showDialog(context, "")
        try {
            val u = URL(url)
            val conn: URLConnection = u.openConnection()
            val contentLength: Int = conn.contentLength
            val stream = DataInputStream(u.openStream())
            val buffer = ByteArray(contentLength)
            stream.readFully(buffer)
            stream.close()
            val fos = DataOutputStream(FileOutputStream(outputFile))
            fos.write(buffer)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            CustomProgressDialog.closeDialog()
            LogHelper(TAG, e.message).run()
        } catch (e: IOException) {
            CustomProgressDialog.closeDialog()
            LogHelper(TAG, e.message).run()
        } finally {
            CustomProgressDialog.closeDialog()
            isSuccess = true
        }

        if (isSuccess)
            MessageUserUtil.toastMessage(context, "Berhasil download dokumen!\nCek file anda ($namaFile) di folder download")
        else
            MessageUserUtil.toastMessage(context, "Gagal mengunduh file!")

        return namaFile
    }
}
package com.cinescape1.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoUtils(
    private val mContext: Activity,
    private val mOnImageSelectListener: OnImageSelectListener?,
    var rlInternetLayout: TextView,
    var viewRefreshListener: ViewRefreshListener
) {
    private var mCameraStorageDirectory: File? = null
    private var mCameraStorageFile: File? = null
    private val directoryName = "/MovieTime"
    private val fileName = "/image_"
    var imagePath: String? = null
    var cameraPickUri: Uri? = null
    private fun getPath(uri: Uri?, activity: Activity): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = activity.managedQuery(uri, projection, null, null, null)
        return if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    private val isSDCardAvailable: Boolean
        private get() {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                if (spaceAvailable()) {
                    return true
                } else {
                    Toast.makeText(mContext, "No space availabe", Toast.LENGTH_SHORT).show()
                }
            }
            return false
        }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun spaceAvailable(): Boolean {
        val stat = StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path)
        val bytesAvailable: Long = stat.blockSizeLong * stat.availableBlocksLong
        val megAvailable = bytesAvailable / (1024f * 1024f)
        Log.i("Megs :", megAvailable.toString())
        return megAvailable > 5
    }

    private fun getImageMatrix(fileName: String): Matrix? {
        val exif: ExifInterface
        try {
            exif = ExifInterface(fileName)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            val matrix = Matrix()
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270f)
            }
            return matrix
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun selectImage(context: Activity) {
        val items = arrayOf<CharSequence>("Take a photo", "Choose a photo", "Cancel")
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Upload a photo")
        builder.setCancelable(false)
        builder.setItems(items) { dialog, item ->
            try {
                if (items[item] == "Take a photo") {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    context.startActivityForResult(cameraIntent, Constant.REQUEST_CAMERA)
                    dialog?.dismiss()
                } else if (items[item] == "Choose a photo") {
                    val i = Intent()
                    i.type = "image/*"
                    i.action = Intent.ACTION_GET_CONTENT
                    context.startActivityForResult(Intent.createChooser(i, "Select Picture"), Constant.REQUEST_SELECT_FILE)
                    dialog?.dismiss()
                } else {
                    dialog.dismiss()
                }
            } catch (e: Exception) {
            }
        }
        builder.show()
    }

    private fun createDirectory() {
        if (isSDCardAvailable) {
            mCameraStorageDirectory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) , directoryName)
            if (!mCameraStorageDirectory!!.exists()) {
                mCameraStorageDirectory!!.mkdir()
            }
        } else {
            if (!mCameraStorageDirectory!!.exists()) {
                mCameraStorageDirectory = mContext.getDir(directoryName, Context.MODE_PRIVATE)
            }
        }
        if (sFileCounter >= 0) {
            sFileCounter = imageFileCount
            sFileCounter++
            imageFileCount = sFileCounter
        }
        mCameraStorageFile = File(mCameraStorageDirectory, "$fileName$sFileCounter.jpeg")
        try {
            mCameraStorageFile!!.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity?,
        userImage: ImageView?
    ) {
        var imageBitmap: Bitmap? = null
        if (requestCode == Constant.REQUEST_CAMERA && resultCode == Constant.RESULT_OK) {
            if (resultCode == -1) {
                if (mCameraStorageFile != null && mCameraStorageFile!!.absolutePath != null) {
                    val fileTemp = File(mCameraStorageFile!!.absolutePath)
                    if (mCameraStorageFile!!.length() <= 0) {
                        mCameraStorageFile!!.delete()
                    }
                    if (fileTemp != null) {
                        val matrix = getImageMatrix(fileTemp.absolutePath)
                        if (matrix != null) {
                            val bitmapOptions = BitmapFactory.Options()
                            bitmapOptions.inSampleSize = 8
                            imageBitmap = BitmapFactory.decodeFile(
                                fileTemp.absolutePath,
                                bitmapOptions
                            )
                            if (imageBitmap != null) imageBitmap = Bitmap.createBitmap(
                                imageBitmap,
                                0,
                                0,
                                imageBitmap.width,
                                imageBitmap.height,
                                matrix,
                                true
                            ) // rotating bitmap
                            if (imageBitmap != null && mOnImageSelectListener != null) {
                                userImage?.setImageBitmap(imageBitmap)
                                //cropImage(cameraPickUri, activity)
                                mOnImageSelectListener.onImageSelect(imageBitmap,  File(fileTemp.absolutePath));
                            }
                        }
                    }
                }
            }
        } else if (requestCode == Constant.REQUEST_SELECT_FILE && resultCode == Constant.RESULT_OK) {
            if (data != null && data.data != null) {
                val selectedImageUri = data.data
                val tempPath = getPath(selectedImageUri, mContext)
                if (tempPath != null) {
                    val matrix = getImageMatrix(tempPath)
                    if (matrix != null) {
                        val bitmapOptions = BitmapFactory.Options()
                        bitmapOptions.inSampleSize = 8
                        imageBitmap = BitmapFactory.decodeFile(
                            tempPath,
                            bitmapOptions
                        )
                        if (imageBitmap != null) imageBitmap = Bitmap.createBitmap(
                            imageBitmap,
                            0,
                            0,
                            imageBitmap.width,
                            imageBitmap.height,
                            matrix,
                            true
                        ) // rotating bitmap
                        if (imageBitmap != null && mOnImageSelectListener != null) {
                            //cropImage(selectedImageUri, activity)
                            userImage?.setImageBitmap(imageBitmap)
                            mOnImageSelectListener.onImageSelect(imageBitmap,  File(tempPath));

                        }
                    }
                }
            }
        } else if (resultCode == Constant.RESULT_OK) {
            if (imagePath != null) {
                val matrix = getImageMatrix(imagePath!!)
                if (matrix != null) {
                    val bitmapOptions = BitmapFactory.Options()
                    bitmapOptions.inSampleSize = 8
                    imageBitmap = BitmapFactory.decodeFile(
                        imagePath,
                        bitmapOptions
                    )
                    if (imageBitmap != null) imageBitmap = Bitmap.createBitmap(
                        imageBitmap,
                        0,
                        0,
                        imageBitmap.width,
                        imageBitmap.height,
                        matrix,
                        true
                    ) // rotating bitmap
                    if (imageBitmap != null && mOnImageSelectListener != null) {
//                        val uploadUserInfo = UploadUserInfo(
//                            mContext, true, userImage, imagePath,
//                            rlInternetLayout,
//                            viewRefreshListener
//                        )
                        //uploadUserInfo.updateInfo()
                        userImage?.setImageBitmap(imageBitmap)
                    }
                }
            }
        }
    }

    private var imageFileCount: Int
        private get() {
            val settings =
                mContext.getSharedPreferences(Constant.IMAGE_COUNT, 0)
            return settings.getInt("File count", 0)
        }
        private set(time) {
            val settings =
                mContext.getSharedPreferences(Constant.IMAGE_COUNT, 0)
            val editor = settings.edit()
            editor.putInt("File count", time)
            editor.commit()
        }

    interface OnImageSelectListener {
        fun onImageSelect(bitmap: Bitmap?, file: File?)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        if (!storageDir.exists()) storageDir.mkdirs()
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.absolutePath
        return image
    }

    private fun cropImage(cameraPickedUri: Uri?, activity: Activity?) {
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
//        Crop.of(cameraPickedUri, Uri.fromFile(photoFile)).asSquare().start(activity)
    }

    companion object {
        var sFileCounter = 0
    }
}

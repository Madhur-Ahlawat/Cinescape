package com.cinescape1.utils

import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern


object HelperUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateHasExpired(dateInput: String): Boolean {
        val today: LocalDate = LocalDate.now()
        var dateParsed: LocalDate? = null
        dateParsed = if (dateInput.contains("/")) {
            // parse credit card expiration date
            val ym: YearMonth = YearMonth.parse(dateInput, DateTimeFormatter.ofPattern("MM/yy"))
            // get last day of month (taking care of leap years)
            ym.atEndOfMonth()
        } else {
            // parse funding expiration date
            LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyyMMdd"))
        }

        // expired if today is equals or after dateParsed
        return !today.isBefore(dateParsed)
    }
    fun isValidHexColorCode(colorCode: String): Boolean {
        val HEX_WEBCOLOR_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$"
        val pattern: Pattern = Pattern.compile(HEX_WEBCOLOR_PATTERN)
        val matcher: Matcher = pattern.matcher(colorCode)
        return matcher.matches()
    }


    fun setGradient(
        view: View,
        topColorHexCode: String = "#FFFFFF",
        bottomColorHexCode: String = "#000000"
    ) {
        var colorArray =
            intArrayOf(if(isValidHexColorCode(topColorHexCode)) Color.parseColor(topColorHexCode) else Color.parseColor("#FFFFFF"), Color.parseColor(bottomColorHexCode))
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, colorArray
        )
        gd.cornerRadius = 0f
        view.setBackground(gd)
    }

    fun getBitmapFromView(
        view: View, leftDiffrence: Int,
        topDifference: Int, rightDifference: Int, bottomDifference: Int
    ): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val viewWidth = view.width - rightDifference
        val viewHeight = view.height - bottomDifference
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            // canvas.drawColor(Color.WHITE);
        }
        // draw rect on canvas
        val myPaint = Paint()
        myPaint.color = Color.rgb(0, 0, 0)
        myPaint.strokeWidth = 10f
        myPaint.style = Paint.Style.STROKE
        canvas.drawRect(
            leftDiffrence.toFloat(),
            topDifference.toFloat(),
            viewWidth.toFloat(),
            viewHeight.toFloat(),
            myPaint
        )


        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    /**
     * This method will return cordinates of view after zoom as rectangle
     *
     * @param view     input view whose cordinates are required
     * @param realZoom input scale or current zoomed factor of view
     * @return rect cordinates of zoomed view
     */
    fun locateViewWithZoom(view: View?, realZoom: Float): Rect {
        val loc = Rect()
        val location = IntArray(2)
        if (view == null) {
            return loc
        }
        view.getLocationOnScreen(location)
        loc.left = location[0]
        loc.top = location[1]
        val widthofView = view.width * realZoom
        val heightofView = view.height * realZoom
        loc.right = loc.left + Math.round(widthofView)
        loc.bottom = loc.top + Math.round(heightofView)
        return loc
    }

    /**
     * Will return cordinates of view passed
     *
     * @param view input view whose cordinates needs to be calculated
     * @return rect with cordinates of view
     */
    fun locateView(view: View?): Rect {
        val loc = Rect()
        val location = IntArray(2)
        if (view == null) {
            return loc
        }
        view.getLocationOnScreen(location)
        loc.left = location[0]
        loc.top = location[1]
        loc.right = loc.left + view.width
        loc.bottom = loc.top + view.height
        return loc
    }
}
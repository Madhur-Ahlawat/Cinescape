package com.cinescape1.utils


import android.widget.EditText
import java.util.regex.Pattern


object InputTextValidator {
    private const val EMAIL_REGEX =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    private const val PASSWORD_REGEX =
        "^.(?=.{6,})(?=.*[a-z])(?=.*[\\\\d])(?=.*[\\\\S]).*$"
    private const val NUMBER_REGEX = "^[123456789][0-9]{9}?$"
    fun validateEmail(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.length == 0) false else Pattern.matches(
            EMAIL_REGEX,
            text
        )
    }

    fun validatePhone(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length == 10
    }

    fun validatePassword(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 6
    }

    fun validateComment(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 15
    }

    fun validateNumber(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else Pattern.matches(
            NUMBER_REGEX,
            text
        )
    }



    private fun validatePhoneNumber(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 7..13
        } else false
    }

    fun validateEmail(email: String): Boolean {
        val text = email.trim { it <= ' ' }
        return if (text.isEmpty()) false else Pattern.matches(
            EMAIL_REGEX,
            text
        )
    }

    //    public static boolean validatePassword(EditText editTextPassword,EditText editTextConfirmPassword) {
    //        String password = editTextPassword.getText().toString().trim();
    //        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
    //
    //        if ((password.length() == 0)|| (confirmPassword.length() == 0))
    //            return false;
    //
    //        else if(!password.equals(confirmPassword))
    //            return false;
    //
    //        else
    //            return true;
    //    }
    fun hasText(editText: EditText): Boolean {
        val string = editText.text.toString().trim { it <= ' ' }
        return string.isNotEmpty()
    }

    fun checkFullName(editText: EditText): Boolean {
        val string = editText.text.toString().trim { it <= ' ' }
        return if (string.isNotEmpty()) {
            val nameArr =
                string.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
            nameArr.size > 1
        } else {
            false
        }
    }

    fun validatePin(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length == 6
    }

    fun validatePCMilesMorePin(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length == 5
    }

    fun validateCard(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 12
    }

    fun validateSixteenDigitsCard(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 16
    }

    fun validatePromoCode(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length in 4..12
    }
}


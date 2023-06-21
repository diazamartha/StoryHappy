package com.example.storyhappy.presentation.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyhappy.R

class EmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    validateEmail(it.toString())
                }
            }
        })
    }

    internal fun validateEmail(email: String) {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val isValid = emailPattern.matcher(email).matches()
        error = if (!isValid) {
            context.getString(R.string.error_email_invalid)
        } else {
            null
        }
    }
}
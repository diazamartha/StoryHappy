package com.example.storyhappy.presentation.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storyhappy.R
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText : TextInputEditText {

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
                    validatePasswordLength(it.toString())
                }
            }
        })
    }

    internal fun validatePasswordLength(password: String) {
        error = if (password.length < 8) {
            context.getString(R.string.error_password_invalid)
        } else {
            null
        }
    }
}
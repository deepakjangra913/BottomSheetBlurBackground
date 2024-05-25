package com.example.blurbackgroundpractice.bottom_sheet.ui.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.deep.base.BaseBottomSheetFragment
import com.example.blurbackgroundpractice.R
import com.example.blurbackgroundpractice.databinding.FragmentChildBottomSheetBinding

/**
 * This fragment is used to show the implementation
 * of bottom sheet fragment
 * note: You can use this for reference
 * */
class ChildBottomSheetFragment : BaseBottomSheetFragment<FragmentChildBottomSheetBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentChildBottomSheetBinding {
        return FragmentChildBottomSheetBinding.inflate(inflater, container, attachToParent)
    }

    override fun initViews() {
        setBlurRadius(10f)
        setBlurredContainerId(R.id.view_container)
    }
}

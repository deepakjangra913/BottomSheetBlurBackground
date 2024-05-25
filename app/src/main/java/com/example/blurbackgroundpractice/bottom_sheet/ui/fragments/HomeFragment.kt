package com.example.blurbackgroundpractice.bottom_sheet.ui.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.deep.base.BaseBottomSheetFragment
import com.example.blurbackgroundpractice.databinding.FragmentChildBinding

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentChildBinding

    private var blurStatus : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)

        binding.btnOpenBs.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionOpenBottomSheet())
        }
    }
}
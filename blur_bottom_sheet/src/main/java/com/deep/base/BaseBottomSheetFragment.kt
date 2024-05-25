package com.deep.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.deep.base.databinding.DialogBaseBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Use this fragment to implement a bottom sheet fragment
 * with some enhancements like blur background and dim background
 * */
abstract class BaseBottomSheetFragment<T : ViewBinding> :
    BottomSheetDialogFragment() {

    // Base binding object to handle the child view
    private lateinit var baseBinding: DialogBaseBottomSheetBinding

    // Binding object for child view
    protected lateinit var binding: T

    /*
    * Use this to set the draggable behaviour
    * of bottom sheet
    * */
    private var shouldDraggable = true

    /*
    * Use this to set the cancelable behaviour
    * of bottom sheet when user click outside
    * */
    private var shouldCancelable = true

    /*
    * To get the id from user to implement
    * blur functionality
    * */
    private var blurredViewContainerId: Int? = null

    /*
    * Use this to enable the blur background when
    * bottom sheet will appear
    * */
    private var shouldBackGroundBlur: Boolean = true

    /*
    * Used this variable to handle the visibility
    * of top button
    * */
    private var shouldTopButtonVisible: Boolean = true

    /*
    * Use this to enable dim background when
    * bottom sheet will appear
    * */
    private var shouldBackgroundDim: Boolean = false

    /*
    * Use this value to adjust the radius of blur
    * acc. to your need
    * */
    private var blurRadiusValue: Float = 25f

    /*
    * Use this value to provide the height
    * of expanded height of bottom sheet
    * at initial stage
    * */
    private var heightRequired: Float = 0f

    /*
    * Listener of top button's click event
    * */
    protected var onTopButtonClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initializing the base binding object
        baseBinding =
            DialogBaseBottomSheetBinding.inflate(LayoutInflater.from(context), container, false)

        binding = inflateBinding(inflater, baseBinding.container, true)

        // Handling the click event of top button
        baseBinding.imvTopButton.setOnClickListener {
            if (onTopButtonClickListener != null) {
                onTopButtonClickListener?.invoke()
                return@setOnClickListener
            }
            closeThisBottomSheet()
        }

        initViews()
        return baseBinding.root
    }

    abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): T

    // Use this method to initialize the views
    abstract fun initViews()


    //    // It's necessary to make base fragment as transparent
    override fun getTheme(): Int {
        return R.style.BaseBottomSheetStyle
    }

    /*
    * Used this to apply the blur effect on background
    * when bottom sheet will appear
    * TODO( We're using [RenderScript] to blur the image but it's deprecated by
    *  google so we need to find the alternative solutions)
    * FYR : I've checked multiple libraries but they are also using render script in
    * backend so need to find other reliable solution
    * */
    private fun blurBackground() {
        try {
            /*
            * We need root view of activity to take the screenshot include
            * toolbar
            * */

            val screenShot: Bitmap? = takeScreenshot(requireActivity())
            screenShot?.let { bitmap ->
                // Code to use blur the bitmap
                val rs = RenderScript.create(requireActivity())
                val input = Allocation.createFromBitmap(rs, bitmap)
                val output = Allocation.createTyped(rs, input.type)
                val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
                script.setRadius(blurRadiusValue)
                script.setInput(input)
                script.forEach(output)
                output.copyTo(bitmap)

                /*
                * Here we got the blurred bitmap
                * now we're converting into drawable to
                * set it as background
                * */
                val blurredDrawable = BitmapDrawable(resources, bitmap)

                /*
                * We're getting the view that we define in our activity
                * */
                val activityView =
                    requireActivity().window.decorView.findViewById<View>(blurredViewContainerId!!)

                // Setting the background on activity's view
                activityView.background = blurredDrawable
                // Destroying the render script object after use
                rs.destroy()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Getting Error in blur : $e")
            e.printStackTrace()
        }
    }

    /**
     * Used this to clear the blurred background
     * */
    private fun unBlurBackground() {
        val activityView =
            requireActivity().window.decorView.findViewById<View>(blurredViewContainerId!!)
        activityView.background = null
    }

    /**
     * Used this method to clear the blur effect
     * if bottom sheet is pop up by other fragment
     * with nav controller
     * */
    override fun onDestroyView() {
        if (shouldBackGroundBlur && blurredViewContainerId != null) {
            unBlurBackground()
        }
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shouldBackgroundBlur(shouldBackGroundBlur)
        setVisibilityOfBackButton(shouldTopButtonVisible)

        // Observing the callback for show
        dialog?.setOnShowListener {
            if (shouldBackGroundBlur && blurredViewContainerId != null) {
                blurBackground()
            }
        }

        // To handle the cancelable state of dialog
        dialog?.setCancelable(shouldCancelable)

        // If you want to enable dim then send this true
        if (shouldBackgroundDim.not()) {
            dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        setInitialHeight(heightRequired)
    }

    /**
     * Use this method to handle cancelable state of bottom
     * sheet it can be [True or False] for eg. like if you set
     * it false then your bottom sheet will never dismiss with
     * out side click
     * */
    protected fun shouldCancelable(value: Boolean = true) {
        shouldCancelable = value
    }

    /**
     * Use this method to set the initial height of bottom
     * sheet we're setting according to window percentage
     * Note: Value can be 0.1f to 0.97f acc. to requirement
     * */
    protected fun setBottomSheetHeight(heightPercentage: Float) {
        heightRequired = heightPercentage
    }

    /**
     * Use this method to handle the draggable state of bottom
     * sheet for eg. if you set it false then you can't drag
     * your bottom sheet
     * Note: value can be [True or False]
     * */
    protected fun shouldDraggable(value: Boolean = true) {
        shouldDraggable = value
    }

    /**
     * Use this method to enable or disable the blur option
     * when bottom sheet will appear if you will enable then
     * whenever you will open this bottom sheet then your app's
     * background will be blurry
     * Note: value can be [True or False]
     * */
    protected fun shouldBackgroundBlur(value: Boolean = true) {
        shouldBackGroundBlur = value
    }

    /**
     * Use this method if you want to dim your app background
     * when your bottom sheet will appear
     * Note: value can be [True or False]
     * */
    protected fun shouldBackgroundDim(value: Boolean = true) {
        shouldBackgroundDim = value
    }

    /**
     * Use this method to increase or decrease the
     * blur radius according to your requirement
     * Note: Value can be 0f to 100f
     * */
    protected fun setBlurRadius(radius: Float) {
        blurRadiusValue = radius
    }

    /**
     * Use this method to show or hide the top button of
     * bottom sheet
     * Note: Value can be [True or false]
     * */
    private fun setVisibilityOfBackButton(value: Boolean = true) {
        if (value) {
            baseBinding.imvTopButton.visibility = View.VISIBLE
        } else {
            baseBinding.imvTopButton.visibility = View.GONE
        }
    }

    /**
     * Use to handle the visibility of top button
     * */
    protected fun showTopButton(value: Boolean = true) {
        shouldTopButtonVisible = value
    }

    /**
     * To convert dp into pixels
     * */
    private fun dpToPx(dp: Float): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    /**
     * Use to change the icon for top image view of
     * bottom sheet as well as you can change the color of
     * that icon & also size of the icon
     * */
    protected fun updateTopIcon(
        iconDrawable: Int? = null,
        iconColor: Int? = null,
        sizeInDp: Int? = null
    ) {
        iconDrawable?.let { value ->
            baseBinding.imvTopButton.setImageResource(value)
        }
        iconColor?.let { color ->
            val tintColor = ContextCompat.getColor(baseBinding.imvTopButton.context, color)
            baseBinding.imvTopButton.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        }
        sizeInDp?.let { size ->
            val sizeInPixels = dpToPx(size.toFloat())
            val layoutParams = baseBinding.imvTopButton.layoutParams
            layoutParams.height = sizeInPixels
            layoutParams.width = sizeInPixels
            baseBinding.imvTopButton.layoutParams = layoutParams
        }
    }

    /**
     * Use this method to close the bottom sheet
     * from child fragment
     **/
    protected fun closeThisBottomSheet() {
        dismiss()
    }

    /**
     * Use this method to set the background color
     * of whole bottom sheet
     * */
    protected fun setBottomSheetBackgroundColor(bgColor: Int) {
        baseBinding.container.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), bgColor))
    }

    /**
     * Use this method to initial height
     * of bottom sheet
     * */
    private fun setInitialHeight(percent: Float) {
        val bottomSheetLayout: View = baseBinding.parent

        // To handle the draggable state of dialog
        val bottomSheetBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.isDraggable = shouldDraggable

        dialog?.window?.setGravity(Gravity.BOTTOM)
        val windowHeight = requireActivity().resources.displayMetrics.heightPixels
        val maxAllowedHeight = (windowHeight * percent).toInt()

        if (percent > 0f) {
            val layoutParams: ViewGroup.LayoutParams? = baseBinding.parent.layoutParams
            layoutParams?.height = maxAllowedHeight
            baseBinding.parent.layoutParams = layoutParams
        } else {
            val layoutParams1: ViewGroup.LayoutParams? = baseBinding.container.layoutParams
            layoutParams1?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            baseBinding.container.layoutParams = layoutParams1
        }
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**
     * Used to set the container id that
     * should be blurred
     * */
    protected fun setBlurredContainerId(layoutId: Int) {
        blurredViewContainerId = layoutId
    }

    companion object {

        // TAG for logs
        const val TAG = "com.deep.base.BaseBottomSheetFragment"
    }

    /**
     * Used this method to take the screenshot from activity's
     * root layout
     * */
    private fun takeScreenshot(activity: Activity): Bitmap {

        // Get the root view of the activity
        val rootView = activity.window.decorView.rootView

        // Get the dimensions of the screen
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels

        // Calculate the height of the status bar
        var statusBarHeight = getStatusBarHeight(activity)

        // Create a bitmap of the root view excluding the status bar
        rootView.isDrawingCacheEnabled = true
        val viewHeight = activity.findViewById<View>(blurredViewContainerId!!).height
        val bitmap =
            Bitmap.createBitmap(rootView.drawingCache, 0, statusBarHeight, screenWidth, viewHeight)
        rootView.isDrawingCacheEnabled = false
        return bitmap
    }


    /**
     * To get the status bar height for the device
     * */
    @SuppressLint("InternalInsetResource")
    fun getStatusBarHeight(activity: Activity): Int {
        val resourceId =
            requireActivity().resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            activity.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * Use this method to change the background of the
     * bottom sheet
     * */
    fun setBottomSheetBackground(drawableId: Int) {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableId)
        baseBinding.container.background = drawable
    }
}


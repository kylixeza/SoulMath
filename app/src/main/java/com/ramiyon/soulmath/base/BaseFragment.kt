package com.ramiyon.soulmath.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ramiyon.soulmath.presentation.validator.ConstraintValidator
import com.ramiyon.soulmath.util.ScreenOrientation

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding

    lateinit var fragmentView: View

    abstract fun inflateViewBinding(container: ViewGroup?): VB
    abstract fun VB.binder()
    abstract fun determineScreenOrientation(): ScreenOrientation

    open fun onCreateViewBehaviour(inflater: LayoutInflater, container: ViewGroup?) { }
    open fun onViewCreatedBehaviour() { }
    open fun constraintValidator(): ConstraintValidator<VB>? { return null }
    open fun onDestroyBehaviour() { }
    open fun onBackPressedBehaviour() { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onCreateViewBehaviour(inflater, container)
        if(_binding == null) {
            _binding = inflateViewBinding(container)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.fragmentView = view

        if(determineScreenOrientation() == ScreenOrientation.PORTRAIT) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        binding?.apply {
            binder()
            lifecycleScope.launchWhenStarted {
                constraintValidator()?.apply { validate() }
            }
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedBehaviour()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onDestroyView() {
       super.onDestroyView()
        onDestroyBehaviour()
        _binding = null
    }

}
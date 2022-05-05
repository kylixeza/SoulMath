package com.ramiyon.soulmath.presentation.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ramiyon.soulmath.R
import com.ramiyon.soulmath.presentation.common.buildAestheticDialog
import com.ramiyon.soulmath.presentation.common.buildLottieDialog
import com.ramiyon.soulmath.databinding.FragmentRegisterBinding
import com.ramiyon.soulmath.databinding.LottieDialogBinding
import com.ramiyon.soulmath.domain.model.User
import com.ramiyon.soulmath.presentation.ui.main.MainActivity
import com.ramiyon.soulmath.util.Constanta
import com.ramiyon.soulmath.util.Resource
import com.ramiyon.soulmath.util.toUserBody
import com.thecode.aestheticdialogs.*
import org.koin.android.ext.android.inject

class RegisterFragment : Fragment() {

    private val viewModel by inject<RegisterViewModel>()
    private val binding by viewBinding<FragmentRegisterBinding>()
    private lateinit var lottieBinding: LottieDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lottieBinding = LottieDialogBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            activity?.apply {
                btnRegister.apply {
                    val email = edtEmail.editText?.text.toString()
                    val password = edtPassword.editText?.text.toString()
                    val username = edtUsername.editText?.text.toString()
                    val address = edtAddress.editText?.text.toString()
                    val phoneNumber = edtPhoneNumber.editText?.text.toString()

                    val user = User(
                        address = address, email = email, name = username, phoneNumber = phoneNumber
                    )

                    viewModel.signUp(
                        email,
                        password,
                        user.toUserBody()
                    ).observe(viewLifecycleOwner) { resource ->
                        val lottieDialog = buildLottieDialog(lottieBinding, "loading_blue_paper_airplane.json")
                        when (resource) {
                            is Resource.Loading<*> -> {
                                lottieDialog.show()
                            }

                            is Resource.Success<*> -> {
                                lottieDialog.dismiss()
                                buildAestheticDialog(
                                    DialogType.SUCCESS,
                                    "Register Success",
                                    "Congratulations!\nYour account has been registered"
                                ) {
                                    lottieDialog.dismiss()
                                    if (Constanta.SOURCE == Constanta.SOURCE_LOGOUT)
                                        startActivity(Intent(requireContext(), MainActivity::class.java))
                                    else
                                        view.findNavController().navigate(RegisterFragmentDirections.actionRegisterDestinationToMainDestination())
                                    viewModel.savePrefHaveLoginAppBefore(true)
                                    viewModel.savePrefHaveRunAppBefore(true)
                                    activity?.finish()
                                }
                            }

                            is Resource.Error<*> -> {
                                lottieDialog.dismiss()
                                buildAestheticDialog(
                                    DialogType.ERROR,
                                    "Something Went Wrong!",
                                    resource.message.toString()
                                ) {
                                    it.dismiss()
                                }
                            }

                            else -> {
                                lottieDialog.dismiss()
                            }
                        }
                    }
                }

                onBackPressedDispatcher.addCallback {
                    view.findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterDestinationToOnBoardingDestination("Register")
                    )
                }
            }
        }
    }
}
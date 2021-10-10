package com.aliahmed1973.loginwithfirebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.aliahmed1973.loginwithfirebase.databinding.FragmentHelloBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

const val SIGN_IN_CODE=10
private const val TAG = "HelloFragment"

class HelloFragment : Fragment() {
    private var _binding :FragmentHelloBinding?=null
    private val binding get() = _binding!!
    private val viewModel:LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentHelloBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()
    }

    private fun login() {
        viewModel.authenticationState.observe(viewLifecycleOwner) {
            if (it == AuthenticationState.AUTHENTICATED) {
                binding.btnLoginLogout.text = "Logout"
                binding.tvHello.text =
                    "Hello ${FirebaseAuth.getInstance().currentUser?.displayName}"
                binding.btnLoginLogout.setOnClickListener {
                    AuthUI.getInstance().signOut(requireContext())
                }
            } else {
                binding.btnLoginLogout.text = "Login"
                binding.tvHello.text = "Hello"
                binding.btnLoginLogout.setOnClickListener {
                    signIn()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== SIGN_IN_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK)
            {
                Log.d(TAG, "onActivityResult: Success login username${FirebaseAuth.getInstance().currentUser?.displayName}")
            }else
            {
                Log.d(TAG, "onActivityResult: SignIN failed: ${response?.error.toString()}")
            }
        }
    }

    private fun signIn()
    {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), SIGN_IN_CODE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
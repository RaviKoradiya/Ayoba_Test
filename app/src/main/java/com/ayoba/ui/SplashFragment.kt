package com.ayoba.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ayoba.NavGraphDirections
import com.ayoba.databinding.FragmentSplashBinding
import com.ayoba.utils.hideActionbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class SplashFragment : Fragment() {

    lateinit var mBinding: FragmentSplashBinding

    private val mNavController by lazy {
        Navigation.findNavController(mBinding.root)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().hideActionbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSplashBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO) {
                delay(3000)
            }
            mNavController.navigate(NavGraphDirections.actionGlobalChatListFragment())
        }
    }
}
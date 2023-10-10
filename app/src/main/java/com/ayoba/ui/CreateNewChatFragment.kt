package com.ayoba.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ayoba.databinding.FragmentCreateNewChatBinding
import com.ayoba.room.entity.ChatRoom
import com.ayoba.utils.setCharCounter
import com.ayoba.utils.setText
import com.ayoba.viewmodels.AyobaViewModel
import com.livinglifetechway.k4kotlin.core.androidx.toastNow
import com.livinglifetechway.k4kotlin.core.value
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class CreateNewChatFragment : Fragment() {

    private lateinit var mBinding: FragmentCreateNewChatBinding

    private val mNavController by lazy {
        Navigation.findNavController(mBinding.root)
    }

    private val mViewModel by sharedViewModel<AyobaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCreateNewChatBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setText("Create New Chat")

        mBinding.tvName.setCharCounter(mBinding.textCounter.id)

        mBinding.btnCreateNewChat.setOnClickListener {
            createNewChatRoom()
        }
    }

    private fun createNewChatRoom() {
        val chatName = mBinding.tvName.value.trim()
        if (chatName.isBlank()) {
            toastNow("Please enter Chat Name!")
            return
        }
        lifecycleScope.launch {
            val chatRoom = ChatRoom(System.currentTimeMillis(), chatName, false)
            val result = mViewModel.insert(chatRoom)
            if (result == -1L) {
                toastNow("Chat Name already exist!")
            } else {
                toastNow("Cheat Created Successfully!")
                mNavController.popBackStack()
            }
        }
    }
}
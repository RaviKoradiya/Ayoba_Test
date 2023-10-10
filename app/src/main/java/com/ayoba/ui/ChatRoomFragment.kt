package com.ayoba.ui

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ayoba.BR
import com.ayoba.R
import com.ayoba.databinding.FragmentChatRoomBinding
import com.ayoba.databinding.ItemChatMessageBinding
import com.ayoba.room.entity.ChatMessage
import com.ayoba.utils.setText
import com.ayoba.viewmodels.AyobaViewModel
import com.livinglifetechway.k4kotlin.core.androidx.toastNow
import com.livinglifetechway.k4kotlin.core.hide
import com.livinglifetechway.k4kotlin.core.show
import com.livinglifetechway.k4kotlin.core.value
import com.ravikoradiya.liveadapter.LiveAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ChatRoomFragment : Fragment() {

    private val args by navArgs<ChatRoomFragmentArgs>()

    private lateinit var mBinding: FragmentChatRoomBinding

    private val mViewModel by sharedViewModel<AyobaViewModel>()

    private val selectedMessageIds = ArrayList<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChatRoomBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setText(args.chatRoom.name)

        setupChatRoomList()

        mBinding.fabSend.setOnClickListener {
            sendMessage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.message, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_messages -> {
                deleteMessages()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupChatRoomList() {
        LiveAdapter(
            mViewModel.getAllMessages(args.chatRoom.rId),
            viewLifecycleOwner,
            BR.chatMessage
        )
            .map<ChatMessage, ItemChatMessageBinding>(R.layout.item_chat_message) {
                onBind {
                    it.binding.chatMessage?.let { chatMessage ->
                        if (selectedMessageIds.any { it == chatMessage.mId }) {
                            it.binding.cardView.backgroundTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.selectionGray
                                )
                            )
                        } else {
                            it.binding.cardView.backgroundTintList = null
                        }
                    }
                }
                areItemSame { old, new ->
                    old.mId == new.mId
                }
                areContentsTheSame { old, new ->
                    old.message == new.message
                }
                onClick {
                    if (selectedMessageIds.isNotEmpty()) {
                        it.binding.chatMessage?.let { chatMessage ->
                            if (selectedMessageIds.any { it == chatMessage.mId }) {
                                selectedMessageIds.remove(chatMessage.mId)
                                it.binding.cardView.backgroundTintList = null
                            } else {
                                selectedMessageIds.add(chatMessage.mId)
                                it.binding.cardView.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.selectionGray
                                    )
                                )
                            }
                            updateOptionMenu()
                        }
                    }
                }
                onLongClick {
                    it.binding.chatMessage?.let { chatMessage ->
                        if (selectedMessageIds.any { it == chatMessage.mId }) {
                            selectedMessageIds.remove(chatMessage.mId)
                            it.binding.cardView.backgroundTintList = null
                        } else {
                            selectedMessageIds.add(chatMessage.mId)
                            it.binding.cardView.backgroundTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.selectionGray
                                )
                            )
                        }
                        updateOptionMenu()
                    }
                }
            }
            .onNoData {
                onOnData(it)
            }
            .into(mBinding.rvChatMessages)

        mBinding.rvChatMessages.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (selectedMessageIds.isEmpty())
                mBinding.rvChatMessages.layoutManager?.scrollToPosition(0)
        }
    }

    private fun updateOptionMenu() {
        setHasOptionsMenu(selectedMessageIds.isNotEmpty())
    }

    private fun onOnData(noData: Boolean) {
        if (noData) {
            mBinding.tvNoChatMessages.show()
        } else {
            mBinding.tvNoChatMessages.hide()
        }
    }

    private fun sendMessage() {
        val chatMessageStr = mBinding.tvMessage.value
        if (chatMessageStr.isBlank()) {
            toastNow("Please enter Message!")
            return
        }

        lifecycleScope.launch {
            val chatMessage =
                ChatMessage(args.chatRoom.rId, chatMessageStr, System.currentTimeMillis())
            mViewModel.insert(chatMessage)
            mBinding.tvMessage.value = ""
        }
    }

    private fun deleteMessages() {

        AlertDialog.Builder(requireContext()).setTitle("Alert!")
            .setMessage("Do you really want to delete message(s)?")
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                lifecycleScope.launch {
                    mViewModel.deleteMessages(selectedMessageIds)
                    selectedMessageIds.clear()
                    updateOptionMenu()
                }
            }.setNegativeButton(android.R.string.cancel) { _, _ ->

            }.show()
    }
}
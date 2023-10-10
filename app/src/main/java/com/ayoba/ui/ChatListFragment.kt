package com.ayoba.ui

import android.app.AlertDialog
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayoba.BR
import com.ayoba.NavGraphDirections
import com.ayoba.R
import com.ayoba.databinding.FragmentChatListBinding
import com.ayoba.databinding.ItemChatRoomBinding
import com.ayoba.room.entity.ChatRoom
import com.ayoba.room.entity.ChatRoomWithLastMessage
import com.ayoba.utils.setText
import com.ayoba.viewmodels.AyobaViewModel
import com.livinglifetechway.k4kotlin.core.hide
import com.livinglifetechway.k4kotlin.core.show
import com.ravikoradiya.liveadapter.LiveAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ChatListFragment : Fragment() {

    private lateinit var mBinding: FragmentChatListBinding

    private val mNavController by lazy {
        Navigation.findNavController(mBinding.root)
    }

    private val mViewModel by sharedViewModel<AyobaViewModel>()

    private val selectedChatIds = ArrayList<Long>()

    private var isMuted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentChatListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setText(getString(R.string.app_name))

        setupChatRoomList()

        mBinding.btnCreateNewChat.setOnClickListener {
            mNavController.navigate(NavGraphDirections.actionGlobalCreateNewChatFragment())
        }

        mBinding.fabNewChat.setOnClickListener {
            mNavController.navigate(NavGraphDirections.actionGlobalCreateNewChatFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.chats, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.action_mute_chats).isVisible = isMuted
        menu.findItem(R.id.action_unmute_chats).isVisible = !isMuted
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_chats -> {
                deleteChats()
                return true
            }

            R.id.action_unmute_chats,
            R.id.action_mute_chats -> {
                muteChats()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupChatRoomList() {
        LiveAdapter(mViewModel.getAllRooms(), viewLifecycleOwner, BR.chatRoom)
            .map<ChatRoomWithLastMessage, ItemChatRoomBinding>(R.layout.item_chat_room) {
                onBind {
                    it.binding.chatRoom?.let { chatRoom ->
                        if (selectedChatIds.any { it == chatRoom.chatRoom.rId }) {
                            it.binding.layItem.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.selectionGray
                                )
                            )
                        } else {
                            it.binding.layItem.background = null
                        }

                        if (chatRoom.chatRoom.isMuted) {
                            it.binding.ivMuted.show()
                        } else {
                            it.binding.ivMuted.hide()
                        }
                    }
                }
                areItemSame { old, new ->
                    old.chatRoom.rId == new.chatRoom.rId
                }
                areContentsTheSame { old, new ->
                    old.lastMessage?.message == new.lastMessage?.message && old.chatRoom.isMuted == new.chatRoom.isMuted
                }
                onClick {
                    if (selectedChatIds.isNotEmpty()) {
                        it.binding.chatRoom?.let { chatRoom ->
                            if (selectedChatIds.any { it == chatRoom.chatRoom.rId }) {
                                selectedChatIds.remove(chatRoom.chatRoom.rId)
                                it.binding.layItem.background = null
                            } else {
                                selectedChatIds.add(chatRoom.chatRoom.rId)
                                it.binding.layItem.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.selectionGray
                                    )
                                )
                            }
                            updateOptionMenu()
                        }
                    } else {
                        it.binding.chatRoom?.let {
                            mNavController.navigate(
                                NavGraphDirections.actionGlobalChatRoomFragment(
                                    it.chatRoom
                                )
                            )
                        }
                    }
                }
                onLongClick {
                    it.binding.chatRoom?.let { chatRoom ->
                        if (selectedChatIds.any { it == chatRoom.chatRoom.rId }) {
                            selectedChatIds.remove(chatRoom.chatRoom.rId)
                            it.binding.layItem.background = null
                        } else {
                            selectedChatIds.add(chatRoom.chatRoom.rId)
                            isMuted = chatRoom.chatRoom.isMuted
                            it.binding.layItem.setBackgroundColor(
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
            .into(mBinding.rvChats)

        mBinding.rvChats.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun updateOptionMenu() {
        setHasOptionsMenu(selectedChatIds.isNotEmpty())
    }

    private fun onOnData(noData: Boolean) {
        if (noData) {
            mBinding.layNoChat.show()
            mBinding.rvChats.hide()
            mBinding.fabNewChat.hide()
        } else {
            mBinding.layNoChat.hide()
            mBinding.rvChats.show()
            mBinding.fabNewChat.show()
        }
    }

    private fun deleteChats() {

        AlertDialog.Builder(requireContext()).setTitle("Alert!")
            .setMessage("Do you really want to delete chat(s)?")
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                lifecycleScope.launch {
                    mViewModel.deleteChats(selectedChatIds)
                    selectedChatIds.clear()
                    updateOptionMenu()
                }
            }.setNegativeButton(android.R.string.cancel) { _, _ ->

            }.show()
    }

    private fun muteChats() {

        setHasOptionsMenu(false)

        val info =
            if (isMuted) "Do you really want to unmute chat(s)?"
            else "Do you really want to mute chat(s)?"
        AlertDialog.Builder(requireContext()).setTitle("Alert!")
            .setMessage(info)
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                lifecycleScope.launch {
                    mViewModel.updateChats(!isMuted, selectedChatIds)
                    isMuted = !isMuted
                    updateOptionMenu()
                }
            }.setNegativeButton(android.R.string.cancel) { _, _ ->
                updateOptionMenu()
            }.show()
    }
}
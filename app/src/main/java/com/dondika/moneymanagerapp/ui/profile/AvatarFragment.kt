package com.dondika.moneymanagerapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.databinding.FragmentAvatarBinding
import com.dondika.moneymanagerapp.utils.Utils

class AvatarFragment : Fragment() {

    private lateinit var binding: FragmentAvatarBinding
    private lateinit var avatarAdapter: AvatarAdapter
    private val pref by lazy { PreferenceManager(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
    }

    private fun setupList() {
        val avatarData = arrayListOf<Int>(
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5,
            R.drawable.avatar6,
            R.drawable.avatar7,
            R.drawable.avatar8,
            R.drawable.avatar9,
            R.drawable.avatar10,
        )
        avatarAdapter = AvatarAdapter(avatarData, object : AvatarAdapter.AdapterListener{
            override fun onClick(avatar: Int) {
                //save new avatar
                pref.put(Utils.PREF_AVATAR, avatar)
                findNavController().navigateUp() //back to previous fragment
            }
        })
        binding.listAvatar.adapter = avatarAdapter
    }

}
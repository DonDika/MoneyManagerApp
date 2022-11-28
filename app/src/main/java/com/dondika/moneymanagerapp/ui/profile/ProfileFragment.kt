package com.dondika.moneymanagerapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dondika.moneymanagerapp.R
import com.dondika.moneymanagerapp.data.local.PreferenceManager
import com.dondika.moneymanagerapp.databinding.FragmentProfileBinding
import com.dondika.moneymanagerapp.ui.auth.login.LoginActivity
import com.dondika.moneymanagerapp.utils.Utils


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val pref by lazy { PreferenceManager(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
    }

    override fun onStart() {
        super.onStart()

        getUserData()
    }

    private fun getUserData() {
        //get data from preferences
        binding.imageAvatar.setImageResource(pref.getInt(Utils.PREF_AVATAR)!!)
        binding.textName.text = pref.getString(Utils.PREF_NAME)
        binding.textUsername.text = pref.getString(Utils.PREF_USERNAME)
        binding.textDate.text = pref.getString(Utils.PREF_DATE)
    }

    private fun setupListener() {
        binding.imageAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_avatarFragment)
        }
        binding.cardLogout.setOnClickListener {
            pref.clear()
            Toast.makeText(requireContext(), "See You!", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(requireActivity(), LoginActivity::class.java)
                    .addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                             Intent.FLAG_ACTIVITY_CLEAR_TASK or
                             Intent.FLAG_ACTIVITY_NEW_TASK
                    )
            )
            requireActivity().finish()
        }
    }

}
package com.robert.passwordmanager.screens.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robert.passwordmanager.PasswordViewModel
import com.robert.passwordmanager.R
import com.robert.passwordmanager.PasswordsAdapter
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var txtSearch: SearchView
    private val passwordViewModel: PasswordViewModel by viewModels { PasswordViewModel.Factory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        txtSearch = view.findViewById(R.id.searchView)
        txtSearch.clearFocus()

        val recentsAdapter = PasswordsAdapter(requireContext())
        val recyclerView = view?.findViewById<RecyclerView>(R.id.searchRecyclerView)
        recyclerView?.adapter = recentsAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager

        txtSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchPasswords(newText, recentsAdapter)
                return true
            }
        })

        return view
    }

    private fun searchPasswords(newText: String?, passwordsAdapter: PasswordsAdapter) {
        if (newText != null) {
            if (newText.length > 1){
                val searchText = "$newText%"
                viewLifecycleOwner.lifecycleScope.launch {
                    passwordViewModel.searchPasswords(searchText).collect{
                        passwordsAdapter.updateList(it)
                    }
                }
            }
        }

    }
}
package com.example.repoapp.ui.repolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repoapp.databinding.FragmentListBinding
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.ui.GithubRepoViewModelFactory
import com.example.repoapp.ui.adapters.RepositoriesListAdapter
import com.example.repoapp.ui.changeVisibleGone
import com.example.repoapp.ui.components.ErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: GithubRepoViewModelFactory
    lateinit var viewModel: RepositoriesListViewModel

    private lateinit var recyclerViewAdapter: RepositoriesListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setupViewModel()
        this.setupListAdapter(ArrayList())
        this.setupUiStateObserver()
        this.setupListListener()
    }

    private fun setupUiStateObserver() {
        viewModel.uiState().observe(this as LifecycleOwner) { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        }
    }

    private fun render(repositoriesUiState: RepositoriesUiState) {
        when (repositoriesUiState) {
            is RepositoriesUiState.Loading -> {
                showLoader(true)
            }
            is RepositoriesUiState.Success -> {
                showLoader(false)
                recyclerViewAdapter.addNewPage(repositoriesUiState.response)
            }
            is RepositoriesUiState.Error -> {
                showLoader(false)
                ErrorDialog(requireContext()) { viewModel.fetchRepoPage() }.showDialog()
            }
        }
    }

    private fun showLoader(show: Boolean) {
        binding.lLoader.changeVisibleGone(show)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[RepositoriesListViewModel::class.java]
    }

    private fun setupListAdapter(list: List<GitRepositoriesVO>) {
        recyclerViewAdapter = RepositoriesListAdapter(list as ArrayList<GitRepositoriesVO>, requireContext())
        binding.rvRepository.adapter = recyclerViewAdapter
    }

    private fun setupListListener() {
        layoutManager = binding.rvRepository.layoutManager as LinearLayoutManager

        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        binding.rvRepository.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = binding.rvRepository.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    if (viewModel.uiState().value != RepositoriesUiState.Loading)
                        if (visibleItemCount + pastVisiblesItems + 8 >= totalItemCount) {
                            viewModel.fetchRepoPage()
                        }
                }
            }
        })
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        this.setupListAdapter(viewModel.allRepositoriesList)
    }

    companion object {
        fun newInstance() = RepositoriesListFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
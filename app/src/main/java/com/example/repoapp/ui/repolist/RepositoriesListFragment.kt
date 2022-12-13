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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.repoapp.ui.components.ErrorDialog

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
        this.setupListAdapter()
        this.setupListObserver()
        this.setupLoaderObserver()
        this.setupErrorDialogObserver()
        this.setupListListener()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[RepositoriesListViewModel::class.java]
    }

    private fun setupListAdapter() {
        recyclerViewAdapter = RepositoriesListAdapter(ArrayList(), requireContext())
        binding.rvRepository.adapter = recyclerViewAdapter
    }

    private fun setupListObserver() {
        viewModel.newPage.observe(this as LifecycleOwner) { newPage ->
            if (viewModel.loader.value == true && newPage.isNotEmpty())
                this.addNewPage(newPage)
             else
                this.populateRepositoryList(viewModel.allRepositoriesList)

        }
    }

    private fun setupLoaderObserver() {
        viewModel.loader.observe(this as LifecycleOwner) { showLoader ->
            binding.lLoader.visibility = when (showLoader) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun setupErrorDialogObserver() {
        viewModel.errorDialog.observe(this as LifecycleOwner) { showErrorDialog ->
            if (showErrorDialog) {
                ErrorDialog(requireContext()) {
                    viewModel.fetchRepoPage()
                }.showDialog()
            }
        }
    }

    private fun addNewPage(newPage: List<GitRepositoriesVO>) {
        recyclerViewAdapter.addNewPage(newPage)
    }

    private fun populateRepositoryList(list: List<GitRepositoriesVO>) {
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

                    if (viewModel.loader.value == false)
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            viewModel.loader.postValue(true)
                            viewModel.fetchRepoPage()
                        }
                }
            }
        })
    }

    companion object {
        fun newInstance() = RepositoriesListFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
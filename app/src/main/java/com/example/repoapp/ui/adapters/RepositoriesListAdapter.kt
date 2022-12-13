package com.example.repoapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repoapp.databinding.RepositoryCardBinding
import com.example.repoapp.model.data.vo.GitRepositoriesVO

class RepositoriesListAdapter(
    var repositories: ArrayList<GitRepositoriesVO> = ArrayList(),
    private val context: Context
) : RecyclerView.Adapter<RepositoriesListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(repositories[position]) {

                Glide.with(context)
                    .load(authorImageUrl)
                    .into(ivImage)

                tvRepoName.text = repositoryName
                tvAuthor.text = authorName
                itFork.setCount(forksCount)
                itStar.setCount(stargazersCount)
            }
        }
    }

    fun addNewPage(page: List<GitRepositoriesVO>) {
        for (item in page) {
            repositories.add(item)
            notifyItemInserted(repositories.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RepositoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = repositories.size

    inner class ViewHolder(val binding: RepositoryCardBinding) : RecyclerView.ViewHolder(binding.root)
}
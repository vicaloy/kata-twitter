package com.valoy.presentation.ui.tweets.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.valoy.core.domain.tweet.Tweet
import com.valoy.databinding.TweetItemBinding

class TweetsAdapter(private val tweets: List<Tweet>) :
    RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TweetItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = TweetItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = tweets[position]
        holder.binding.tweet.text = tweet.text
    }

    override fun getItemCount() = tweets.size
}

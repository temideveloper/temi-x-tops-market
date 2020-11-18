package com.robosolutions.temixtopsmarket.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listAdapter")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.bindAdapter(adapter: ListAdapter<T, VH>) =
    setAdapter(adapter)

@BindingAdapter("itemDecoration")
fun RecyclerView.bindDecoration(decor: RecyclerView.ItemDecoration) = addItemDecoration(decor)

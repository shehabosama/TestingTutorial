package com.example.projectwithtestcases.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.projectwithtestcases.R
import com.example.projectwithtestcases.data.local.ShoppingItem
import javax.inject.Inject

class ShoppingItemAdapter  @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {
    class ShoppingItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var ivShoppingImage: ImageView
         var tvName: TextView
         var tvShoppingItemAmount: TextView
         var tvShoppingItemPrice: TextView
        init {
            ivShoppingImage = itemView.findViewById(R.id.ivShoppingImage)
            tvName = itemView.findViewById(R.id.tvName)
            tvShoppingItemAmount = itemView.findViewById(R.id.tvShoppingItemAmount)
            tvShoppingItemPrice = itemView.findViewById(R.id.tvShoppingItemPrice)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(holder.ivShoppingImage)


            holder.itemView.apply {
                glide.load(shoppingItem.imageUrl).into(holder.ivShoppingImage)

                holder.tvName.text = shoppingItem.name
                val amountText = "${shoppingItem.amount}x"
                holder.tvShoppingItemAmount.text = amountText
                val priceText = "${shoppingItem.price}€"
                holder.tvShoppingItemPrice.text = priceText
            }
        }
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }
}
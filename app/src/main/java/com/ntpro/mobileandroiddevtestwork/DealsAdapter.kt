package com.ntpro.mobileandroiddevtestwork

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ntpro.mobileandroiddevtestwork.databinding.DealItemBinding
import kotlin.math.round

class DealsAdapter(private var deals: MutableList<Server.Deal>) :
    RecyclerView.Adapter<DealViewHolder>() {

    fun updateDeals(isSorted: Boolean, newDeals: List<Server.Deal>) {
        if (isSorted) {
            deals.clear()
            deals.addAll(newDeals)
        } else {
            deals.addAll(newDeals)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        return DealViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.deal_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        val backgroundColor: Int = when (deal.side) {
            Server.Deal.Side.BUY -> Color.GREEN
            else -> Color.RED
        }
        holder.bind(deal, backgroundColor)
    }
}

class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = DealItemBinding.bind(itemView)

    fun bind(deal: Server.Deal, backgroundColor: Int) {
        binding.timeStampTv.text = deal.timeStamp.toString()
        binding.nameTv.text = deal.instrumentName
        binding.priceTv.text = setFormatDouble(deal.price)
        binding.amountTv.text = setFormatDoubleRound(round(deal.amount))
        binding.sideTv.text = deal.side.toString()
        binding.priceTv.setBackgroundColor(backgroundColor)
    }

    private fun setFormatDouble(value: Double): String {
        return String.format("%.2f", value)
    }

    private fun setFormatDoubleRound(value: Double): String {
        return String.format("%.0f", value)
    }
}
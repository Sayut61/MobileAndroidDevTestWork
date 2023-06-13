package com.ntpro.mobileandroiddevtestwork

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ntpro.mobileandroiddevtestwork.databinding.ActivityMainBinding

enum class SortingStatus {
    DATE, NAME, PRICE, AMOUNT, SIDE, NOTHING
}

enum class WaySorting {
    UP, DOWN
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DealsAdapter
    private lateinit var recyclerView: RecyclerView

    private var sortStatus = SortingStatus.DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val server = Server()
        val layoutManager = LinearLayoutManager(this)
        adapter = DealsAdapter(mutableListOf())
        recyclerView = binding.recyclerView
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        val fullList = mutableListOf<List<Server.Deal>>()

        server.subscribeToDeals { newDeals ->
            runOnUiThread {

                when (sortStatus) {
                    SortingStatus.AMOUNT -> {
                        adapter.sortItemsByAmount()
                    }

                    SortingStatus.PRICE -> {
                        adapter.sortItemsByPrice()
                    }

                    SortingStatus.NAME -> {
                        adapter.sortItemsByName()
                    }

                    SortingStatus.SIDE -> {
                        adapter.sortItemsBySide()
                    }

                    else -> {
                        if (fullList.isNotEmpty()) {
                            adapter.sortItemsByTimeStamp()
                        } else {
                            fullList.add(newDeals)
                            adapter.updateDeals(newDeals)
                        }
                    }
                }
            }
        }

        binding.amountSortBtn.setOnClickListener {
            setSortStatus(
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.RED,
                Color.BLACK,
                SortingStatus.AMOUNT
            )
        }

        binding.sortSideBtn.setOnClickListener {
            setSortStatus(
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.RED,
                SortingStatus.SIDE
            )
        }

        binding.sortPriceBtn.setOnClickListener {
            setSortStatus(
                Color.BLACK,
                Color.BLACK,
                Color.RED,
                Color.BLACK,
                Color.BLACK,
                SortingStatus.PRICE
            )
        }

        binding.sortNameBtn.setOnClickListener {
            setSortStatus(
                Color.BLACK,
                Color.RED,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                SortingStatus.NAME
            )
        }

        binding.sortDateBtn.setOnClickListener {
            setSortStatus(
                Color.RED,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                SortingStatus.DATE
            )
        }

    }

    private fun setSortStatus(
        color1: Int,
        color2: Int,
        color3: Int,
        color4: Int,
        color5: Int,
        status: SortingStatus
    ) {
        binding.icSort1.setColorFilter(color1)
        binding.icSort2.setColorFilter(color2)
        binding.icSort3.setColorFilter(color3)
        binding.icSort4.setColorFilter(color4)
        binding.icSort5.setColorFilter(color5)
        sortStatus = status
    }
}
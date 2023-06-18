package com.ntpro.mobileandroiddevtestwork

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ntpro.mobileandroiddevtestwork.databinding.ActivityMainBinding

enum class SortingStatus {
    DATE, NAME, PRICE, AMOUNT, SIDE, NOTHING
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DealsAdapter
    private lateinit var recyclerView: RecyclerView

    private var sortStatus = SortingStatus.DATE
    private var sortDirection = false

    private val DATA_TIMEOUT = 500L
    private var lastDataReceivedTime: Long = 0L

    private var dataState = false

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
                lastDataReceivedTime = System.currentTimeMillis()

                when (sortStatus) {
                    SortingStatus.AMOUNT -> {
                        adapter.sortItemsByAmount(sortDirection)
                    }

                    SortingStatus.PRICE -> {
                        adapter.sortItemsByPrice(sortDirection)
                    }

                    SortingStatus.NAME -> {
                        adapter.sortItemsByName(sortDirection)
                    }

                    SortingStatus.SIDE -> {
                        adapter.sortItemsBySide(sortDirection)
                    }

                    else -> {
                        if (fullList.isNotEmpty()) {
                            adapter.sortItemsByTimeStamp(sortDirection)
                        } else {
                            fullList.add(newDeals)
                            adapter.updateDeals(newDeals)
                        }
                    }
                }
            }
        }

        binding.upSortBtn.setOnClickListener {
            setBackStatus(
                Color.GREEN,
                Color.GRAY,
                true
            )
            if (dataState) {
                when (sortStatus) {
                    SortingStatus.AMOUNT -> {
                        adapter.sortItemsByAmount(sortDirection)
                    }

                    SortingStatus.PRICE -> {
                        adapter.sortItemsByPrice(sortDirection)
                    }

                    SortingStatus.NAME -> {
                        adapter.sortItemsByName(sortDirection)
                    }

                    SortingStatus.SIDE -> {
                        adapter.sortItemsBySide(sortDirection)
                    }

                    else -> {
                        adapter.sortItemsByTimeStamp(sortDirection)
                    }
                }
            }
        }

        binding.downSortBtn.setOnClickListener {
            setBackStatus(
                Color.GRAY,
                Color.RED,
                false
            )
            if (dataState) {
                when (sortStatus) {
                    SortingStatus.AMOUNT -> {
                        adapter.sortItemsByAmount(sortDirection)
                    }

                    SortingStatus.PRICE -> {
                        adapter.sortItemsByPrice(sortDirection)
                    }

                    SortingStatus.NAME -> {
                        adapter.sortItemsByName(sortDirection)
                    }

                    SortingStatus.SIDE -> {
                        adapter.sortItemsBySide(sortDirection)
                    }

                    else -> {
                        adapter.sortItemsByTimeStamp(sortDirection)
                    }
                }
            }
        }

        binding.amountSortBtn.setOnClickListener {
            if (dataState) {
                adapter.sortItemsByAmount(sortDirection)
            }
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
            if (dataState) {
                adapter.sortItemsBySide(sortDirection)
            }
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
            if (dataState) {
                adapter.sortItemsByPrice(sortDirection)
            }
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
            if (dataState) {
                adapter.sortItemsByName(sortDirection)
            }
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
            if (dataState) {
                adapter.sortItemsByTimeStamp(sortDirection)
            }
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

    override fun onResume() {
        startDataTimeoutCheck()
        super.onResume()
    }

    private fun startDataTimeoutCheck() {
        val handler = Handler()
        handler.postDelayed({
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - lastDataReceivedTime
            if (elapsedTime >= DATA_TIMEOUT) {
                dataState = true
            } else {
                startDataTimeoutCheck()
            }
        }, DATA_TIMEOUT)
    }

    private fun setBackStatus(
        color1: Int,
        color2: Int,
        status: Boolean
    ) {
        binding.upSortBtn.setColorFilter(color1)
        binding.downSortBtn.setColorFilter(color2)
        sortDirection = status
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
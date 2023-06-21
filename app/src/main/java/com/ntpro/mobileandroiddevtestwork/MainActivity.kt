package com.ntpro.mobileandroiddevtestwork

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ntpro.mobileandroiddevtestwork.databinding.ActivityMainBinding

enum class SortingType {
    DATE, NAME, PRICE, AMOUNT, SIDE, NOTHING
}

var currentMaxVal = MutableLiveData(1000)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DealsAdapter
    private lateinit var recyclerView: RecyclerView


    private var sortType = SortingType.DATE
    private var sortDirection = false
    private var dealsIsSorted = false

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
                if (fullList.isEmpty()) {
                    dealsIsSorted = false
                    adapter.updateDeals(false, newDeals)
                }
                fullList.add(newDeals)
            }
        }

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItem == totalItemCount - 1) {
                    currentMaxVal.value =
                        if (((fullList.flatten().size - 1) - currentMaxVal.value!!) >= 1000) {
                            currentMaxVal.value?.plus(1000)
                        } else {
                            currentMaxVal.value?.plus((fullList.flatten().size - 1) - currentMaxVal.value!!)
                        }
                }
            }
        }
        recyclerView.addOnScrollListener(scrollListener)

        currentMaxVal.observe(this) { currentMaxVal ->
            if (currentMaxVal <= fullList.flatten().size) {
                when (sortType) {
                    SortingType.AMOUNT -> {
                        if (sortDirection) {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedByDescending { it.amount }
                                    .subList(0, currentMaxVal)
                            )
                        } else {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedBy { it.amount }.subList(0, currentMaxVal)
                            )
                        }
                    }

                    SortingType.PRICE -> {
                        if (sortDirection) {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedByDescending { it.price }
                                    .subList(0, currentMaxVal)
                            )
                        } else {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedBy { it.price }.subList(0, currentMaxVal)
                            )
                        }
                    }

                    SortingType.NAME -> {
                        if (sortDirection) {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedByDescending { it.instrumentName }
                                    .subList(0, currentMaxVal)
                            )
                        } else {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedBy { it.instrumentName }
                                    .subList(0, currentMaxVal)
                            )
                        }
                    }

                    SortingType.SIDE -> {
                        if (sortDirection) {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedByDescending { it.side }
                                    .subList(0, currentMaxVal)
                            )
                        } else {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedBy { it.side }.subList(0, currentMaxVal)
                            )
                        }
                    }

                    else -> {
                        if (sortDirection) {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedByDescending { it.timeStamp }
                                    .subList(0, currentMaxVal)
                            )
                        } else {
                            adapter.updateDeals(
                                dealsIsSorted,
                                fullList.flatten().sortedBy { it.timeStamp }
                                    .subList(0, currentMaxVal)
                            )
                        }
                    }
                }
            }
        }

        binding.upSortBtn.setOnClickListener {
            dealsIsSorted = true
            setBackStatus(
                Color.GREEN,
                Color.GRAY,
                true
            )
            when (sortType) {
                SortingType.AMOUNT -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.amount }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.amount }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                SortingType.PRICE -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.price }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.price }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                SortingType.NAME -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.instrumentName }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.instrumentName }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                SortingType.SIDE -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.side }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.side }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                else -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.timeStamp }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.timeStamp }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }
            }
        }

        binding.downSortBtn.setOnClickListener {
            dealsIsSorted = true
            setBackStatus(
                Color.GRAY,
                Color.RED,
                false
            )
            when (sortType) {
                SortingType.AMOUNT -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.amount }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.amount }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                SortingType.PRICE -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.price }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.price }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                SortingType.NAME -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.instrumentName }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.instrumentName }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                SortingType.SIDE -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.side }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.side }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }

                else -> {
                    if (sortDirection) {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedByDescending { it.timeStamp }
                                .subList(0, currentMaxVal.value!!)
                        )
                    } else {
                        adapter.updateDeals(
                            dealsIsSorted,
                            fullList.flatten().sortedBy { it.timeStamp }
                                .subList(0, currentMaxVal.value!!)
                        )
                    }
                }
            }
        }

        binding.sortAmountBtn.setOnClickListener {
            dealsIsSorted = true
            if (sortDirection) {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedByDescending { it.amount }
                        .subList(0, currentMaxVal.value!!)
                )
            } else {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedBy { it.amount }.subList(0, currentMaxVal.value!!)
                )
            }
            setSortStatus(
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.RED,
                Color.BLACK,
                SortingType.AMOUNT
            )
        }

        binding.sortSideBtn.setOnClickListener {
            dealsIsSorted = true
            if (sortDirection) {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedByDescending { it.side }
                        .subList(0, currentMaxVal.value!!)
                )
            } else {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedBy { it.side }.subList(0, currentMaxVal.value!!)
                )
            }
            setSortStatus(
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.RED,
                SortingType.SIDE
            )
        }

        binding.sortPriceBtn.setOnClickListener {
            dealsIsSorted = true
            if (sortDirection) {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedByDescending { it.price }
                        .subList(0, currentMaxVal.value!!)
                )
            } else {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedBy { it.price }.subList(0, currentMaxVal.value!!)
                )
            }
            setSortStatus(
                Color.BLACK,
                Color.BLACK,
                Color.RED,
                Color.BLACK,
                Color.BLACK,
                SortingType.PRICE
            )
        }

        binding.sortNameBtn.setOnClickListener {
            dealsIsSorted = true
            if (sortDirection) {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedByDescending { it.instrumentName }
                        .subList(0, currentMaxVal.value!!)
                )
            } else {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedBy { it.instrumentName }
                        .subList(0, currentMaxVal.value!!)
                )
            }
            setSortStatus(
                Color.BLACK,
                Color.RED,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                SortingType.NAME
            )
        }

        binding.sortDateBtn.setOnClickListener {
            dealsIsSorted = true
            if (sortDirection) {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedByDescending { it.timeStamp }
                        .subList(0, currentMaxVal.value!!)
                )
            } else {
                adapter.updateDeals(
                    dealsIsSorted,
                    fullList.flatten().sortedBy { it.timeStamp }.subList(0, currentMaxVal.value!!)
                )
            }
            setSortStatus(
                Color.RED,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                SortingType.DATE
            )
        }
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
        status: SortingType
    ) {
        binding.icSort1.setColorFilter(color1)
        binding.icSort2.setColorFilter(color2)
        binding.icSort3.setColorFilter(color3)
        binding.icSort4.setColorFilter(color4)
        binding.icSort5.setColorFilter(color5)
        sortType = status
    }
}
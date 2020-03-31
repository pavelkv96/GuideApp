package com.grsu.guideapp.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.R
import com.grsu.guideapp.adapters.OrderAdapter
import com.grsu.guideapp.base.Result
import timber.log.Timber

class OrderFragment : Fragment(), (View, Int) -> Unit, Observer<Result<List<CheckedItem>>> {

    private val model: OrderViewModel by viewModels()

    private lateinit var progress: ProgressBar
    private lateinit var message: MaterialTextView
    private lateinit var filterButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderAdapter = OrderAdapter(listener = this) { isChecked, position ->
            orderAdapter.getItem(position)?.also { it.isCheck = isChecked }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        view?.also {
            progress = it.findViewById(R.id.pb_fragment_order_progress)
            message = it.findViewById(R.id.mtv_fragment_order_message)
            filterButton = it.findViewById(R.id.fab_fragment_order_filter)
            recyclerView = it.findViewById(R.id.rv_fragment_order_list)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orderAdapter
        recyclerView.setHasFixedSize(true)

        filterButton.setOnClickListener {
            orderAdapter.getAllChecked().forEach {
                Timber.e("checked item ${it.item.id}")
            }
        }

        model.getListLiveData().observe(this, this)
        return view
    }

    override fun invoke(view: View, position: Int): Unit = run { orderAdapter.update(position) }

    override fun onChanged(it: Result<List<CheckedItem>>?) {
        when (it) {
            is Result.Success -> {
                filterButton.show()
                message.visibility = View.GONE
                message.text = ""
                progress.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                orderAdapter.submitList(it.data)
            }
            is Result.Loading -> {
                filterButton.hide()
                message.visibility = View.GONE
                message.text = ""
                progress.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                orderAdapter.submitList(listOf())
            }
            is Result.Error -> {
                filterButton.hide()
                message.text = it.error
                message.visibility = View.VISIBLE
                progress.visibility = View.GONE
                recyclerView.visibility = View.GONE
                orderAdapter.submitList(listOf())
            }
        }
    }
}
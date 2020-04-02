package com.grsu.guideapp.ui.fragments.objects

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.adapters.ObjectAdapter
import com.grsu.guideapp.utils.base.Result
import com.grsu.guideapp.data.local.database.vo.ObjectItemVO
import com.grsu.guideapp.utils.extensions.hide
import com.grsu.guideapp.utils.extensions.isKeyboardOpen
import com.grsu.guideapp.utils.extensions.navigate
import com.grsu.guideapp.utils.extensions.show

class ObjectFragment : Fragment(), ((View, Int) -> Unit), Observer<Result<List<ObjectItemVO>>>, OnQueryTextListener {

    private lateinit var objectAdapter: ObjectAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textError: TextView
    private lateinit var progress: ProgressBar
    private lateinit var model: ObjectViewModel

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        objectAdapter = ObjectAdapter(listener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        setHasOptionsMenu(true)
        val id = arguments?.let { ObjectFragmentArgs.fromBundle(it).catalogId } ?: -1

        model = ViewModelProvider(this, ModelFactory(id))[ObjectViewModel::class.java]

        recyclerView = view.findViewById(R.id.rv_fragment_catalog)
        textError = view.findViewById(R.id.tv_fragment_catalog_text_error)
        progress = view.findViewById(R.id.pb_fragment_catalog_progress)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = objectAdapter
        }

        model.getData().observe(this, this)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search)?.let {
            searchView = it.actionView as SearchView
            searchView?.isIconified = model.getQuery().isEmpty()
            searchView?.setQuery(model.getQuery(), true)
            searchView?.setOnQueryTextListener(this)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId && isKeyboardOpen()) {
            searchView?.clearFocus()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun invoke(view: View, position: Int) {
        objectAdapter.getItem(position)?.also {
            searchView?.clearFocus()
            navigate(ObjectFragmentDirections.actionObjectsToObjectDetails(it.id.toInt(), it.name, it.image))
        }
    }

    override fun onChanged(result: Result<List<ObjectItemVO>>?) {
        when (result) {
            is Result.Success -> {
                objectAdapter.submitList(result.data)
                recyclerView.show()
                textError.hide()
                progress.hide()
            }
            is Result.Loading -> {
                recyclerView.hide()
                textError.hide()
                progress.show()
            }
            is Result.Error -> {
                recyclerView.hide()
                textError.show()
                progress.hide()
                textError.text = result.error
            }
        }
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        model.search(newText ?: "")
        return false
    }
}
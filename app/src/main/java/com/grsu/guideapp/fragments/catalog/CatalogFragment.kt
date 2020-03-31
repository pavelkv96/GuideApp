package com.grsu.guideapp.fragments.catalog

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grsu.guideapp.R
import com.grsu.guideapp.activities.SharedViewModel
import com.grsu.guideapp.adapters.TypesAdapter
import com.grsu.guideapp.base.Result
import com.grsu.guideapp.database.vo.TypeItemVO
import com.grsu.guideapp.utils.extensions.hide
import com.grsu.guideapp.utils.extensions.navigate
import com.grsu.guideapp.utils.extensions.show
import timber.log.Timber

class CatalogFragment : Fragment(), ((View, Int) -> Unit), Observer<Result<List<TypeItemVO>>> {

    private var typesAdapter: TypesAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var textError: TextView
    private lateinit var progress: ProgressBar
    private lateinit var model: CatalogViewModel
    private lateinit var sharedModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typesAdapter = TypesAdapter(listener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        setHasOptionsMenu(true)


        model = ViewModelProvider(this)[CatalogViewModel::class.java]
        sharedModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            sharedModel.finishApplication()
        }

        recyclerView = view.findViewById(R.id.rv_fragment_catalog)
        textError = view.findViewById(R.id.tv_fragment_catalog_text_error)
        progress = view.findViewById(R.id.pb_fragment_catalog_progress)

        with(recyclerView) {
            setHasFixedSize(true)
            val manager = LinearLayoutManager(requireContext())
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(requireContext(), manager.orientation))
            adapter = typesAdapter
        }

        model.getData().observe(viewLifecycleOwner, this)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_routes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.action_list_routes == item.itemId) {
            navigate(R.id.action_catalog_to_routes)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun invoke(view: View, position: Int) {
        typesAdapter?.getItem(position)?.also {
            navigate(CatalogFragmentDirections.actionCatalogToObjects(it.id.toInt(), it.name))
        }
        Timber.e("Click item position = $position")
    }

    override fun onChanged(result: Result<List<TypeItemVO>>?) {
        when (result) {
            is Result.Success -> {
                typesAdapter?.submitList(result.data)
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

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }
}
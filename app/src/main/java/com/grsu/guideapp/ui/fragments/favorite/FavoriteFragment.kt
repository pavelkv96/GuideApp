package com.grsu.guideapp.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.vo.FavoriteVO
import com.grsu.guideapp.data.local.database.vo.PoiFavoriteVO
import com.grsu.guideapp.data.local.database.vo.RouteFavoriteVO
import com.grsu.guideapp.ui.activities.SharedViewModel
import com.grsu.guideapp.ui.adapters.FavoriteAdapter
import com.grsu.guideapp.utils.base.Result
import com.grsu.guideapp.utils.extensions.navigate

class FavoriteFragment : Fragment(), (View, Int) -> Unit, Observer<Result<List<FavoriteVO>>> {

    private val model: FavoriteViewModel by viewModels()
    private val sharedModel: SharedViewModel by activityViewModels()

    private lateinit var progress: ProgressBar
    private lateinit var message: MaterialTextView
    private lateinit var list: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = FavoriteAdapter(this)
        model.getFavorite()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { sharedModel.finishApplication() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        view?.also {
            progress = it.findViewById(R.id.pb_fragment_favorite_progress)
            message = it.findViewById(R.id.mtv_fragment_favorite_message)

            list = it.findViewById(R.id.rv_fragment_favorite_list)
            list.adapter = favoriteAdapter
            list.setHasFixedSize(true)
        }

        model.getFavoriteLiveData().observe(this, this)
        return view
    }

    override fun invoke(view: View, position: Int) {
        favoriteAdapter.getItem(position)?.also {
            if (it is RouteFavoriteVO) {
                //TODO open route screen
                Toast.makeText(requireContext(), "It is route ${it.name}", Toast.LENGTH_SHORT).show()
            } else if (it is PoiFavoriteVO) {
                navigate(FavoriteFragmentDirections.actionFavoriteToObjectDetails(it.id, it.name, it.photo))
//                Toast.makeText(requireContext(), "It is poi ${it.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onChanged(it: Result<List<FavoriteVO>>?) {
        when (it) {
            is Result.Success -> {
                message.visibility = View.GONE
                message.text = ""
                progress.visibility = View.GONE
                list.visibility = View.VISIBLE
                favoriteAdapter.submitList(it.data)
            }
            is Result.Loading -> {
                message.visibility = View.GONE
                message.text = ""
                progress.visibility = View.VISIBLE
                list.visibility = View.GONE
                favoriteAdapter.submitList(listOf())
            }
            is Result.Error -> {
                message.text = it.error
                message.visibility = View.VISIBLE
                progress.visibility = View.GONE
                list.visibility = View.GONE
                favoriteAdapter.submitList(listOf())
            }
        }
    }

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }
}
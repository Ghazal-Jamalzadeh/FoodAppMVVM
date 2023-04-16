package com.jmzd.ghazal.foodappmvvm.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmzd.ghazal.foodappmvvm.databinding.FragmentFavoritesBinding
import com.jmzd.ghazal.foodappmvvm.utils.isVisibleGone
import com.jmzd.ghazal.foodappmvvm.utils.setupRecyclerView
import com.jmzd.ghazal.foodappmvvm.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    //Binding
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    //Other
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding?.apply {
            //Load data
            viewModel.loadFavorites()
            viewModel.favoritesListData.observe(viewLifecycleOwner) {
                //it : DataStatus<List<FoodEntity>>!
                if (it.isEmpty) {
                    emptyLay.isVisibleGone(true, favoriteList)
//                    statusLay.disImg.setImageResource(R.drawable.box)
//                    statusLay.disTxt.text = getString(R.string.emptyList)
                } else {
                    emptyLay.isVisibleGone(false, favoriteList)
                    favoriteAdapter.setData(it.data!!)
                    favoriteList.setupRecyclerView(LinearLayoutManager(requireContext()), favoriteAdapter)

                    favoriteAdapter.setOnItemClickListener { food ->
                        val direction = FavoritesFragmentDirections.actionToDetailFragment(food.id)
                        findNavController().navigate(direction)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
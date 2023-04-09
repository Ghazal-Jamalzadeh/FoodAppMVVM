package com.jmzd.ghazal.foodappmvvm.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.jmzd.ghazal.foodappmvvm.databinding.FragmentHomeBinding
import com.jmzd.ghazal.foodappmvvm.ui.home.adapters.CategoriesAdapter
import com.jmzd.ghazal.foodappmvvm.utils.MyResponse
import com.jmzd.ghazal.foodappmvvm.utils.setupListWithAdapter
import com.jmzd.ghazal.foodappmvvm.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.jmzd.ghazal.foodappmvvm.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    //Other
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            //Random food
            viewModel.loadFoodRandom()
            viewModel.randomFoodData.observe(viewLifecycleOwner) {
                // it : List<ResponseFoodsList.Meal>!
                it[0].let { meal ->
                    //it : ResponseFoodsList.Meal
                    headerImg.load(meal.strMealThumb) {
                        crossfade(true)
                        crossfade(500)
                    }
                }
            }
            //Filters
            viewModel.loadFilterList()
            viewModel.filtersListData.observe(viewLifecycleOwner) {
                //it : MutableList<Char>!
                filterSpinner.setupListWithAdapter(it) { letter : String ->
//                    viewModel.loadFoodsList(letter)
                }
            }
            //Category
            viewModel.loadCategoriesList()
            viewModel.categoriesListData.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        homeCategoryLoading.isVisible(true, categoryList)
                    }
                    MyResponse.Status.SUCCESS -> {
                        homeCategoryLoading.isVisible(false, categoryList)
                        categoriesAdapter.setData(it.data!!.categories)
                        categoryList.setupRecyclerView(
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                            categoriesAdapter
                        )
                    }
                    MyResponse.Status.ERROR -> {
                        homeCategoryLoading.isVisible(false, categoryList)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            categoriesAdapter.setOnItemClickListener {
//                viewModel.loadFoodByCategory(it.strCategory.toString())
            }
        }

    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

}
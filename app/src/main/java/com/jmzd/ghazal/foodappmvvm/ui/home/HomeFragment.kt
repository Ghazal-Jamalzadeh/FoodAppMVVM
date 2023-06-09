package com.jmzd.ghazal.foodappmvvm.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.jmzd.ghazal.foodappmvvm.R
import com.jmzd.ghazal.foodappmvvm.databinding.FragmentHomeBinding
import com.jmzd.ghazal.foodappmvvm.ui.home.adapters.CategoriesAdapter
import com.jmzd.ghazal.foodappmvvm.ui.home.adapters.FoodsAdapter
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

    @Inject
    lateinit var foodsAdapter: FoodsAdapter

    @Inject
    lateinit var connection: CheckConnection

    enum class PageState { EMPTY, NETWORK, NONE }

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
//            viewModel.loadFilterList()
            viewModel.filtersListData.observe(viewLifecycleOwner) {
                //it : MutableList<Char>!
                filterSpinner.setupListWithAdapter(it) { letter : String ->
                    if(viewModel.lastSelectedLetter != letter){
                        viewModel.lastSelectedLetter = letter
                    viewModel.loadFoodsList(letter)
                    }
                }
            }
            //Category
            /*viewModel.loadCategoriesList()*/
            viewModel.categoriesListData.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        homeCategoryLoading.isVisibleGone(true, categoryList)
                    }
                    MyResponse.Status.SUCCESS -> {
                        homeCategoryLoading.isVisibleGone(false, categoryList)
                        categoriesAdapter.setData(it.data!!.categories)
                        categoryList.setupRecyclerView(
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                            categoriesAdapter
                        )
                    }
                    MyResponse.Status.ERROR -> {
                        homeCategoryLoading.isVisibleGone(false, categoryList)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            categoriesAdapter.setOnItemClickListener {
                viewModel.loadFoodByCategory(it.strCategory.toString())
            }

            // Foods
            val rand = ('A'..'Z').random()
            Log.d(TAG, "random: $rand")
            viewModel.loadFoodsList(rand.toString())
            viewModel.foodsListData.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        homeFoodsLoading.isVisibleGone(true, foodsList)
                    }
                    MyResponse.Status.SUCCESS -> {
                        homeFoodsLoading.isVisibleGone(false, foodsList)
                        if (it.data!!.meals != null) {
                            if (it.data.meals!!.isNotEmpty()) {
                                checkConnectionOrEmpty(false, PageState.NONE)
                                foodsAdapter.setData(it.data.meals)
                                foodsList.setupRecyclerView(
                                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                                    foodsAdapter
                                )
                            }
                        } else {
                            checkConnectionOrEmpty(true, PageState.EMPTY)
                        }
                    }
                    MyResponse.Status.ERROR -> {
                        homeFoodsLoading.isVisibleGone(false, foodsList)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            foodsAdapter.setOnItemClickListener {
                val direction = HomeFragmentDirections.actionToDetailFragment(it.idMeal!!.toInt())
                findNavController().navigate(direction)
            }

            //Search
            searchEdt.addTextChangedListener{
                if (it.toString().length > 2) {
                    viewModel.loadFoodBySearch(it.toString())
                }
            }
            //Internet
            connection.observe(viewLifecycleOwner) {
                //it : Boolean!
                if (it) {
                    checkConnectionOrEmpty(false, PageState.NONE)
                } else {
                    checkConnectionOrEmpty(true, PageState.NETWORK)
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    private fun checkConnectionOrEmpty(isShownError: Boolean, state: PageState) {
        binding?.apply {
            if (isShownError) {
                homeDisLay.isVisibleInvisible(true, homeContent)
                when (state) {
                    PageState.EMPTY -> {
                        disconnectLay.disImg.setImageResource(R.drawable.box)
                        disconnectLay.disTxt.text = getString(R.string.emptyList)
                    }
                    PageState.NETWORK -> {
                        disconnectLay.disImg.setImageResource(R.drawable.disconnect)
                        disconnectLay.disTxt.text = getString(R.string.checkInternet)
                    }
                    else -> {}
                }
            } else {
                homeDisLay.isVisibleInvisible(false, homeContent)
            }
        }
    }

}
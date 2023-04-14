package com.jmzd.ghazal.foodappmvvm.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.gson.Gson
import com.jmzd.ghazal.foodappmvvm.R
import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseFoodsList
import com.jmzd.ghazal.foodappmvvm.databinding.FragmentDetailBinding
import com.jmzd.ghazal.foodappmvvm.ui.detail.player.PlayerActivity
import com.jmzd.ghazal.foodappmvvm.ui.home.HomeFragment
import com.jmzd.ghazal.foodappmvvm.utils.CheckConnection
import com.jmzd.ghazal.foodappmvvm.utils.MyResponse
import com.jmzd.ghazal.foodappmvvm.utils.VIDEO_ID
import com.jmzd.ghazal.foodappmvvm.utils.isVisibleGone
import com.jmzd.ghazal.foodappmvvm.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var connection: CheckConnection

/*    @Inject
    lateinit var entity: FoodEntity*/

    //Other
    private val args: DetailFragmentArgs by navArgs()
    private var foodId = 0
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init views
        binding?.apply {
            //Get data
            foodId = args.foodId
            //back
            detailBack.setOnClickListener { findNavController().navigateUp() }
            //Call api
            viewModel.loadFoodDetail(foodId)
            viewModel.foodDetailData.observe(viewLifecycleOwner) {
                //it : MyResponse<ResponseFoodList>!
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        detailLoading.isVisibleGone(true, detailContentLay)
                    }
                    MyResponse.Status.SUCCESS -> {
                        detailLoading.isVisibleGone(false, detailContentLay)
                        //Set data
                        it.data?.meals?.get(0)?.let { itMeal : ResponseFoodsList.Meal ->
                            //Entity
                        /*    entity.id = itMeal.idMeal!!.toInt()
                            entity.title = itMeal.strMeal.toString()
                            entity.img = itMeal.strMealThumb.toString()*/
                            //Set data
                            foodCoverImg.load(itMeal.strMealThumb) {
                                crossfade(true)
                                crossfade(500)
                            }
                            foodCategoryTxt.text = itMeal.strCategory
                            foodAreaTxt.text = itMeal.strArea
                            foodTitleTxt.text = itMeal.strMeal
                            foodDescTxt.text = itMeal.strInstructions
                            //Play
                            if (itMeal.strYoutube != null) {
                                foodPlayImg.visibility = View.VISIBLE
                                foodPlayImg.setOnClickListener {
                                    val videoId = itMeal.strYoutube.split("=")[1]
                                    Intent(requireContext(), PlayerActivity::class.java).also {
                                        it.putExtra(VIDEO_ID, videoId)
                                        startActivity(it)
                                    }
                                }
                            } else {
                                foodPlayImg.visibility = View.GONE
                            }
                            //Source
                            if (itMeal.strSource != null) {
                                foodSourceImg.visibility = View.VISIBLE
                                foodSourceImg.setOnClickListener {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(itMeal.strSource)))
                                }
                            } else {
                                foodSourceImg.visibility = View.GONE
                            }
                        }
                        //Json Array
                        val jsonData = JSONObject(Gson().toJson(it.data))
                        val meals = jsonData.getJSONArray("meals")
                        val meal = meals.getJSONObject(0)
                        //Ingredient
                        for (i in 1..15) {
                            val ingredient = meal.getString("strIngredient$i")
                            if (ingredient.isNullOrEmpty().not()) {
                                ingredientsTxt.append("$ingredient\n")
                            }
                        }
                        //Measure
                        for (i in 1..15) {
                            val measure = meal.getString("strMeasure$i")
                            if (measure.isNullOrEmpty().not()) {
                                measureTxt.append("$measure\n")
                            }
                        }
                    }
                    MyResponse.Status.ERROR -> {
                        detailLoading.isVisibleGone(false, detailContentLay)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    private fun checkConnectionOrEmpty(isShownError: Boolean, state: HomeFragment.PageState) {
        binding?.apply {
            if (isShownError) {
                homeDisLay.isVisibleGone(true, detailContentLay)
                when (state) {
                    HomeFragment.PageState.EMPTY -> {
                        disconnectLay.disImg.setImageResource(R.drawable.box)
                        disconnectLay.disTxt.text = getString(R.string.emptyList)
                    }
                    HomeFragment.PageState.NETWORK -> {
                        disconnectLay.disImg.setImageResource(R.drawable.disconnect)
                        disconnectLay.disTxt.text = getString(R.string.checkInternet)
                    }
                    else -> {}
                }
            } else {
                homeDisLay.isVisibleGone(false, detailContentLay)
            }
        }
    }

}
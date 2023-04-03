package com.jmzd.ghazal.foodappmvvm.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.jmzd.ghazal.foodappmvvm.R
import com.jmzd.ghazal.foodappmvvm.databinding.FragmentHomeBinding
import com.jmzd.ghazal.foodappmvvm.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    //Other
    private val viewModel: HomeViewModel by viewModels()

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
        }

    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

}
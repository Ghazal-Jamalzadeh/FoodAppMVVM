package com.jmzd.ghazal.foodappmvvm.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.jmzd.ghazal.foodappmvvm.R


fun Spinner.setupListWithAdapter(list: MutableList<out Any>, callback: (String) -> Unit) {

    /* چون اینجا ویو داریم نیازی به پاس دادن کانتکس نداریم و از کانتکس خود ویو استفاده میکنیم*/
    /* تنظیم لایه اختصاصی برای خود اسپینر و دراپ داون */
    val adapter = ArrayAdapter(context, R.layout.item_spinner, list)
    adapter.setDropDownViewResource(R.layout.item_spinner_list)
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            callback(list[p2].toString())
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

    }
}

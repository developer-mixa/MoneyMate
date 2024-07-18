package com.example.moneymate.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import com.example.moneymate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListeners()
    }

    private fun showCurrenciesMenu(view: View, currencyText: TextView){
        val popupMenu = PopupMenu(this@MainActivity, view)
        popupMenu.menu.apply {
            viewModel.getAllCurrencies().forEachIndexed { index, currency ->
                add(Menu.NONE, index, Menu.CATEGORY_SYSTEM, currency)
            }
        }

        popupMenu.setOnMenuItemClickListener { item ->
            currencyText.text = item.title
            true
        }

        popupMenu.show()
    }

    private fun onClickListeners() = with(binding) {
        reloadButton.setOnClickListener { }

        inputExchange.setOnClickListener { view -> showCurrenciesMenu(view, inputExchangeText) }
        outputExchange.setOnClickListener { view -> showCurrenciesMenu(view, outputExchangeText) }
    }

}
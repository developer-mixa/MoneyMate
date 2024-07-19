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
        observe()
    }

    private fun onClickListeners() = with(binding) {
        reloadButton.setOnClickListener {
            viewModel.convertCurrency(textInputText.text.toString().toFloat())
        }
        inputExchange.addShowingCurrenciesMenu { viewModel.setInputExchange(it) }
        outputExchange.addShowingCurrenciesMenu { viewModel.setOutputExchange(it) }
    }

    private fun observe() = with(binding){
        viewModel.exchange.observe(this@MainActivity) {
            inputExchangeText.text = it.inputExchange
            outputExchangeText.text = it.outputExchange
        }
        viewModel.exchangeValue.observe(this@MainActivity) {
            outputValueText.text = it.toString()
        }
    }

    private fun View.addShowingCurrenciesMenu(onTap: (String) -> Unit) = setOnClickListener {
        val popupMenu = PopupMenu(this@MainActivity, this)
        popupMenu.menu.apply {
            viewModel.getAllCurrencies().forEachIndexed { index, currency ->
                add(Menu.NONE, index, Menu.CATEGORY_SYSTEM, currency)
            }
        }

        popupMenu.setOnMenuItemClickListener { item ->
            onTap(item.title.toString())
            true
        }

        popupMenu.show()
    }

}
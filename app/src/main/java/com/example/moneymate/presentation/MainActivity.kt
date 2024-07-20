package com.example.moneymate.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import com.example.moneymate.R
import com.example.moneymate.databinding.ActivityMainBinding
import com.example.moneymate.presentation.models.ErrorContainer
import com.example.moneymate.presentation.models.PendingContainer
import com.example.moneymate.presentation.models.takeSuccess
import com.google.android.material.snackbar.Snackbar
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

    private fun getInputValue() : Float = with(binding){
        return textInputText.text.toString().toFloat()
    }

    private fun onClickListeners() = with(binding) {
        reloadButton.setOnClickListener {
            viewModel.convertCurrency(getInputValue())
        }

        swipeButton.setOnClickListener {
            viewModel.swipe()
            viewModel.convertCurrency(getInputValue())
        }

        inputExchange.addShowingCurrenciesMenu { viewModel.setInputExchange(it) }
        outputExchange.addShowingCurrenciesMenu { viewModel.setOutputExchange(it) }
    }

    private fun observe() = with(binding){
        viewModel.exchange.observe(this@MainActivity) {
            inputExchangeText.text = it.inputExchange
            outputExchangeText.text = it.outputExchange
        }
        viewModel.exchangeValue.observe(this@MainActivity) { result ->

            progressBar.isVisible = result is PendingContainer
            outputValueText.text = (result.takeSuccess() ?: getString(R.string.load_text)).toString()
            if (result is ErrorContainer){
                showSnackbar(getString(R.string.error_try_to_refresh, result.error.message))
            }
            if (result is PendingContainer){
                showSnackbar(getString(R.string.currency_is_transferring))
            }
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

    private fun showSnackbar(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}
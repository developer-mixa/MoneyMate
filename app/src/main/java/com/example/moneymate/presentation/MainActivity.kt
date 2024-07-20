package com.example.moneymate.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import com.example.moneymate.R
import com.example.moneymate.databinding.ActivityMainBinding
import com.example.moneymate.databinding.ChooseCurrencyDialogBinding
import com.example.moneymate.presentation.models.ErrorContainer
import com.example.moneymate.presentation.models.PendingContainer
import com.example.moneymate.presentation.models.SuccessContainer
import com.example.moneymate.presentation.models.takeSuccess
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainActivityViewModel>()

    private var currenciesAdapter: ArrayAdapter<String>? = null

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

        buttonRefresh.setOnClickListener {
            viewModel.getAllowCurrencies()
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

        viewModel.allowCurrencies.observe(this@MainActivity) { result ->

            if(result is SuccessContainer){
                setupAdapter(result.value)
            }

            mainProgressBar.isVisible = result is PendingContainer
            errorContainer.isVisible = result is ErrorContainer
            mainContainer.isVisible = result is SuccessContainer
        }

    }

    private fun View.addShowingCurrenciesMenu(onTap: (String) -> Unit) = setOnClickListener {
        showCurrencyDialog(onTap)
    }

    private fun setupAdapter(currencies: List<String>){
        currenciesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            currencies
        )
    }

    private fun showCurrencyDialog(onTap: (String) -> Unit){
        val dialogBinding = ChooseCurrencyDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.currenciesListView.adapter = currenciesAdapter

        dialogBinding.currenciesListView.setOnItemClickListener { _, _, position, _ ->
            dialog.dismiss()
            val currency = currenciesAdapter?.getItem(position) ?: return@setOnItemClickListener
            onTap(currency)
        }

        dialogBinding.root.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showSnackbar(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}
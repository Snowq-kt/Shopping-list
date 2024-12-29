package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import kotlin.properties.Delegates

class ShopItemActivity : AppCompatActivity() {

    private lateinit var countET: EditText
    private lateinit var nameET: EditText
    private lateinit var buttonDone: Button

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var mode: String
    private var extraId by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        observeViewModel()
        setupRightMode()
    }


    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
        viewModel.errorText.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRightMode() {
        mode = intent.getStringExtra(EXTRA_MODE).toString()
        when (mode) {
            ADDING_MODE -> addingMode()
            EDITING_MODE -> editingMode()
            else -> throw RuntimeException("The data is corrupted")
        }
    }

    private fun addingMode() {
        buttonDone.setOnClickListener {
            viewModel.addShopItem(nameET.text.toString(), countET.text.toString().toInt())
        }
    }

    private fun editingMode() {
        viewModel.getShopItem(extraId)
        viewModel.shopItem.observe(this) {
            countET.setText(it.count.toString())
            nameET.setText(it.name)
        }
        buttonDone.setOnClickListener {
            viewModel.changeShopItem(nameET.text.toString(), countET.text.toString().toInt())
        }
    }



    private fun initViews() {
        countET = findViewById(R.id.countET)
        nameET = findViewById(R.id.nameET)
        buttonDone = findViewById(R.id.buttonDone)
        extraId = intent.getIntExtra(EXTRA_ID, -1)
    }
    companion object {

        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_ID = "extra_id"
        private const val ADDING_MODE = "adding_mode"
        private const val EDITING_MODE = "editing_mode"

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, ADDING_MODE)
            return intent
        }

        fun newIntentEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EDITING_MODE)
            intent.putExtra(EXTRA_ID, shopItemId)
            return intent
        }
    }
}
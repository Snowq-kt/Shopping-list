package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
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

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingDoneListener {

    private var mode = ""
    private var extraId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        parseIntent()
        if (savedInstanceState == null) {
            launchFragment()
        }
    }

    private fun parseIntent() {
        val specMode = intent.getStringExtra(EXTRA_MODE)
        if (specMode != null) {
            mode = specMode
        }
        if (mode == EDITING_MODE) {
            extraId = intent.getIntExtra(EXTRA_ID, -1)
        }
    }

    private fun launchFragment() {
        val fragment = when (mode) {
            EDITING_MODE -> ShopItemFragment.newInstanceEditItem(extraId)
            ADDING_MODE  -> ShopItemFragment.newInstanceAddItem()
            else      -> throw RuntimeException("Unknown screen mode $mode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
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

    override fun onEditingDone() {
        finish()
    }
}
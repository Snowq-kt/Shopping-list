package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import kotlin.properties.Delegates

class ShopItemFragment : Fragment() {

    private lateinit var countET: EditText
    private lateinit var nameET: EditText
    private lateinit var buttonDone: Button

    lateinit var onEditingDoneListener: OnEditingDoneListener

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var mode: String
    private var extraId by Delegates.notNull<Int>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingDoneListener) {
            onEditingDoneListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRightMode()
        observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParam()
    }

    private fun parseParam() {
        val args = arguments
        mode = args?.getString(EXTRA_MODE) ?: ""
        extraId = args?.getInt(EXTRA_ID) ?: -1
    }

    interface OnEditingDoneListener {

        fun onEditingDone()
    }


    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingDoneListener.onEditingDone()
        }
        viewModel.errorText.observe(viewLifecycleOwner) {
            Log.d("ShopItemFragment", it)
        }
    }

    private fun setupRightMode() {
        when (mode) {
            ADDING_MODE -> addingMode()
            EDITING_MODE -> editingMode()
            else -> throw RuntimeException("The unknown mode: $mode")
        }
    }

    private fun addingMode() {
        buttonDone.setOnClickListener {
            viewModel.addShopItem(nameET.text.toString(), countET.text.toString().toInt())
        }
    }

    private fun editingMode() {
        viewModel.getShopItem(extraId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            countET.setText(it.count.toString())
            nameET.setText(it.name)
        }
        buttonDone.setOnClickListener {
            viewModel.changeShopItem(nameET.text.toString(), countET.text.toString().toInt())
        }
    }

    private fun initViews(view: View) {
        countET = view.findViewById(R.id.countET)
        nameET = view.findViewById(R.id.nameET)
        buttonDone = view.findViewById(R.id.buttonDone)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
    }
    companion object {

        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_ID = "extra_id"
        private const val ADDING_MODE = "adding_mode"
        private const val EDITING_MODE = "editing_mode"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MODE, ADDING_MODE)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_MODE, EDITING_MODE)
                    putInt(EXTRA_ID, shopItemId)
                }
            }
        }
    }
}
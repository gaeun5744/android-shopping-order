package woowacourse.shopping.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import woowacourse.shopping.R
import woowacourse.shopping.data.cart.CartRepositoryImpl
import woowacourse.shopping.data.order.OrderRepositoryImpl
import woowacourse.shopping.data.product.ProductRepositoryImpl
import woowacourse.shopping.data.recentproduct.RecentProductDatabase
import woowacourse.shopping.data.recentproduct.RecentProductRepositoryImpl
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.ui.cart.item.CartItemFragment
import woowacourse.shopping.ui.cart.recommend.RecommendFragment
import woowacourse.shopping.ui.cart.viewmodel.CartViewModel
import woowacourse.shopping.ui.cart.viewmodel.CartViewModelFactory

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels {
        CartViewModelFactory(
            ProductRepositoryImpl(),
            CartRepositoryImpl(),
            RecentProductRepositoryImpl.get(RecentProductDatabase.database().recentProductDao()),
            OrderRepositoryImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initToolbar()
        initFragment()

        onClickOrderBtn()
        onClickTotalCheckBox()

        observeIsOrder()
    }

    private fun initFragment() {
        supportFragmentManager.findFragmentById(R.id.fcv_cart) ?: supportFragmentManager.commit {
            replace(R.id.fcv_cart, CartItemFragment())
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun initToolbar() {
        binding.toolbarCart.setNavigationOnClickListener {
            finish()
        }
    }

    private fun onClickOrderBtn() {
        binding.layoutOrder.btnOrder.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.fcv_cart) is CartItemFragment) {
                supportFragmentManager.commit {
                    add(R.id.fcv_cart, RecommendFragment())
                    addToBackStack(null)
                }
            } else if (supportFragmentManager.findFragmentById(R.id.fcv_cart) is RecommendFragment) {
                viewModel.order()
            }

        }
    }

    private fun observeIsOrder() {
        viewModel.isOrderSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, getString(R.string.order_success_message), Toast.LENGTH_SHORT)
                    .show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.order_fail_message), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onClickTotalCheckBox() {
        binding.cbCartItemTotal.setOnClickListener {
            viewModel.totalCheckBoxCheck((it as CheckBox).isChecked)
        }
    }


    companion object {
        fun startActivity(context: Context) =
            Intent(context, CartActivity::class.java).run {
                context.startActivity(this)
            }
    }
}

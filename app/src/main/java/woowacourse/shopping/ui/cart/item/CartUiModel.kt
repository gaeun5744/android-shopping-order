package woowacourse.shopping.ui.cart.item

import woowacourse.shopping.model.Quantity

data class CartUiModel(
    val id: Long,
    val productId: Long,
    val name: String,
    val price: Int,
    val quantity: Quantity,
    val imageUrl: String,
    val isChecked: Boolean = false,
    val isLoading: Boolean = true,
) {
    val totalPrice = price * quantity.value
}

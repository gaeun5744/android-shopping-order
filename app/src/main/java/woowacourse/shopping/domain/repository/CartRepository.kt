package woowacourse.shopping.domain.repository

import woowacourse.shopping.domain.model.CartWithProduct
import woowacourse.shopping.domain.result.Result

interface CartRepository {
    suspend fun getCartItem(productId: Long): Result<CartWithProduct>

    suspend fun getAllCartItems(): Result<List<CartWithProduct>>

    suspend fun postCartItems(
        productId: Long,
        quantity: Int,
    ): Result<Unit>

    suspend fun deleteCartItem(id: Long): Result<Unit>

    suspend fun getCartItemsCount(): Result<Int>

    suspend fun patchCartItem(
        id: Long,
        quantity: Int,
    ): Result<Unit>

    suspend fun addProductToCart(
        productId: Long,
        quantity: Int,
    ): Result<Unit>
}

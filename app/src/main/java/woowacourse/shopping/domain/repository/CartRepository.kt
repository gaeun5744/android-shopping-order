package woowacourse.shopping.domain.repository

import woowacourse.shopping.domain.model.CartWithProduct
import woowacourse.shopping.domain.result.Result

interface CartRepository {
    suspend fun cartItem(productId: Long): CartWithProduct

    suspend fun cartItemOrNull(productId: Long): CartWithProduct?

    suspend fun cartItemResponse(productId: Long): Result<CartWithProduct>

    suspend fun allCartItems(): List<CartWithProduct>

    suspend fun allCartItemsResponse(): Result<List<CartWithProduct>>

    suspend fun postCartItems(
        productId: Long,
        quantity: Int,
    ): Result<Unit>

    suspend fun deleteCartItem(id: Long): Result<Unit>

    suspend fun cartItemsCount(): Int

    suspend fun cartItemsCountOrNull(): Int?

    suspend fun cartItemsCountResponse(): Result<Int>

    suspend fun patchCartItem(
        id: Long,
        quantity: Int,
    ): Result<Unit>

    suspend fun addProductToCart(
        productId: Long,
        quantity: Int,
    ): Result<Unit>
}

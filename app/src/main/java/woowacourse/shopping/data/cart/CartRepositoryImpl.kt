package woowacourse.shopping.data.cart

import woowacourse.shopping.data.dto.request.RequestCartItemPostDto
import woowacourse.shopping.data.dto.request.RequestCartItemsPatchDto
import woowacourse.shopping.data.dto.response.ResponseCartItemCountsGetDto
import woowacourse.shopping.data.dto.response.ResponseCartItemsGetDto
import woowacourse.shopping.data.service.ApiFactory
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.Quantity
import kotlin.concurrent.thread

class CartRepositoryImpl : CartRepository {
    override fun getAllCartItems(): List<Cart> {
        var cartsDto: ResponseCartItemsGetDto? = null
        val size = getCartItemCounts()
        thread {
            cartsDto = ApiFactory.getCartItems(0, size)
        }.join()
        val carts = cartsDto ?: error("장바구니 정보를 불러올 수 없습니다.")
        return carts.content.map {
            Cart(id = it.id, productId = it.product.id, quantity = Quantity(it.quantity))
        }
    }

    override fun getAllCartItemsWithProduct(): List<CartWithProduct> {
        var cartsDto: ResponseCartItemsGetDto? = null
        val size = getCartItemCounts()
        thread {
            cartsDto = ApiFactory.getCartItems(0, size)
        }.join()
        val carts = cartsDto ?: error("장바구니 정보를 불러올 수 없습니다.")

        return carts.content.map {
            CartWithProduct(
                it.id,
                it.product.toDomain(),
                Quantity(it.quantity),
            )
        }
    }

    override fun postCartItems(
        productId: Long,
        quantity: Int,
    ) {
        thread {
            ApiFactory.postCartItems(
                RequestCartItemPostDto(
                    productId = productId,
                    quantity = quantity,
                ),
            )
        }.join()
    }

    override fun deleteCartItem(id: Long) {
        thread {
            ApiFactory.deleteCartItems(id)
        }.join()
    }

    override fun getCartItemCounts(): Int {
        var cartCountDto: ResponseCartItemCountsGetDto? = null
        thread {
            cartCountDto = ApiFactory.getCartItemCounts()
        }.join()
        val count = cartCountDto ?: error("장바구니 아이템 수량을 조회할 수 없습니다.")
        return count.quantity
    }

    override fun patchCartItem(
        id: Long,
        quantity: Int,
    ) {
        thread {
            ApiFactory.patchCartItems(id = id, request = RequestCartItemsPatchDto(quantity))
        }.join()
    }

    override fun addProductToCart(
        productId: Long,
        quantity: Int,
    ) {
        val cart: Cart? = getAllCartItems().firstOrNull { it.productId == productId }
        if (cart == null) {
            postCartItems(productId, quantity)
            return
        }
        patchCartItem(cart.id, cart.quantity.value + quantity)
    }

    private fun ResponseCartItemsGetDto.Product.toDomain() =
        Product(
            id = this.id,
            imageUrl = this.imageUrl,
            name = this.name,
            price = this.price,
            category = this.category,
        )
}

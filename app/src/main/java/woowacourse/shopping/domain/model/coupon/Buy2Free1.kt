package woowacourse.shopping.domain.model.coupon

import woowacourse.shopping.domain.model.ProductWithQuantity
import java.time.LocalDate

data class Buy2Free1(
    override val id: Long,
    override val code: String,
    override val description: String,
    override val expirationDate: LocalDate,
    override val discountType: String,
    val buyQuantity: Int,
    val getQuantity: Int,
) : Coupon {
    private val applyCount = buyQuantity + getQuantity

    override fun canUse(products: List<ProductWithQuantity>): Boolean {
        val isProductWithQuantity3 = products.any { it.quantity.value == applyCount }
        val isNotExpired = LocalDate.now().isBefore(expirationDate)
        return isProductWithQuantity3 && isNotExpired

    }

    override fun discountPrice(products: List<ProductWithQuantity>): Int {
        if (!canUse(products)) error("$description 쿠폰을 적용할 수 없습니다.")
        val productsQuantity3 = products.filter { it.quantity.value == applyCount }
        return productsQuantity3.maxOf { it.product.price }
    }
}

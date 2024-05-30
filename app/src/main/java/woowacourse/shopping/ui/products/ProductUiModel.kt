package woowacourse.shopping.ui.products

import woowacourse.shopping.model.Product

sealed interface ProductUiModel

data class ProductWithQuantityUiModel(
    val product: Product,
    val quantity: Int,
    val isLoading: Boolean = true,
) : ProductUiModel

data object LoadingUiModel : ProductUiModel
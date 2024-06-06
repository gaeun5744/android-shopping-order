package woowacourse.shopping.data.datasource

import woowacourse.shopping.data.remote.dto.response.ResponseProductIdGetDto
import woowacourse.shopping.data.remote.dto.response.ResponseProductsGetDto

interface ProductDataSource {
    suspend fun getProductsByOffset(
        page: Int,
        size: Int,
    ): ResponseProductsGetDto

    suspend fun getProductsById(id: Long): ResponseProductIdGetDto
}
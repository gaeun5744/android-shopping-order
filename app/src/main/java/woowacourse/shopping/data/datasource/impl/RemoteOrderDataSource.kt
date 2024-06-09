package woowacourse.shopping.data.datasource.impl

import woowacourse.shopping.data.datasource.OrderDataSource
import woowacourse.shopping.data.remote.api.ApiResponse
import woowacourse.shopping.data.remote.api.ShoppingRetrofit
import woowacourse.shopping.data.remote.api.handleApi
import woowacourse.shopping.data.remote.dto.request.RequestOrderPostDto

class RemoteOrderDataSource : OrderDataSource {
    override suspend fun postOrder(request: RequestOrderPostDto): ApiResponse<Unit> =
        handleApi {
            ShoppingRetrofit.orderService.postOrders(request)
        }
}

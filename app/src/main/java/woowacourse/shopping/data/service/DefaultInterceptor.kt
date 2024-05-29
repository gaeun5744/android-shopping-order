package woowacourse.shopping.data.service

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class DefaultInterceptor(username: String, password: String) : Interceptor {
    private var credential: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credential).build()
        return chain.proceed(request)
    }
}

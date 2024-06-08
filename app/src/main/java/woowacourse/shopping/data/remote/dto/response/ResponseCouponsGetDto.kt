package woowacourse.shopping.data.remote.dto.response

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializable(with = ModuleSerializer::class)
sealed class Coupon {
    abstract val id: Long
    abstract val code: String
    abstract val description: String

    @Serializable(with = LocalDateSerializer::class)
    abstract val expirationDate: LocalDate
    abstract val discountType: String
}

@Serializable
data class FixedDiscountCoupon(
    override val id: Long,
    override val code: String,
    override val description: String,
    @Serializable(with = LocalDateSerializer::class)
    override val expirationDate: LocalDate,
    val discount: Int,
    val minimumAmount: Int,
    override val discountType: String = "fixed",
) : Coupon()

@Serializable
data class BuyXGetYCoupon(
    override val id: Long,
    override val code: String,
    override val description: String,
    @Serializable(with = LocalDateSerializer::class)
    override val expirationDate: LocalDate,
    val buyQuantity: Int,
    val getQuantity: Int,
    override val discountType: String = "buyXgetY",
) : Coupon()

@Serializable
data class FreeShippingCoupon(
    override val id: Long,
    override val code: String,
    override val description: String,
    @Serializable(with = LocalDateSerializer::class)
    override val expirationDate: LocalDate,
    val minimumAmount: Int,
    override val discountType: String = "freeShipping",
) : Coupon()

@Serializable
data class PercentageDiscountCoupon(
    override val id: Long,
    override val code: String,
    override val description: String,
    @Serializable(with = LocalDateSerializer::class)
    override val expirationDate: LocalDate,
    val discount: Int,
    val availableTime: AvailableTime,
    override val discountType: String = "percentage",
) : Coupon()

@Serializable
data class AvailableTime(
    @Serializable(with = LocalTimeSerializer::class)
    val start: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    val end: LocalTime,
)

@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

@Serializer(forClass = LocalTime::class)
object LocalTimeSerializer : KSerializer<LocalTime> {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString(), formatter)
    }
}

object ModuleSerializer : JsonContentPolymorphicSerializer<Coupon>(Coupon::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Coupon> {
        return when (element.jsonObject["discountType"]?.jsonPrimitive?.content) {
            "fixed" -> FixedDiscountCoupon.serializer()
            "buyXgetY" -> BuyXGetYCoupon.serializer()
            "percentage" -> PercentageDiscountCoupon.serializer()
            "freeShipping" -> FreeShippingCoupon.serializer()
            else -> throw Exception("Unknown Module: key 'discountType' not found or does not matches any module type")
        }
    }
}

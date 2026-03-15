package cz.creeperface.hytale.uimanager.codec

import com.hypixel.hytale.codec.ExtraInfo
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.util.RawJsonReader

class CustomPageBuilderCodec : BuilderCodec<Any>(
    builder(Any::class.java) {

    }
) {

    override fun decodeJson(reader: RawJsonReader, extraInfo: ExtraInfo) {

    }
}
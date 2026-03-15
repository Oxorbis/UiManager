package cz.creeperface.hytale.uimanager.asset

import com.hypixel.hytale.server.core.asset.common.CommonAsset
import java.util.concurrent.CompletableFuture

class DynamicAsset(
    name: String,
    val bytes: ByteArray
) : CommonAsset(name, bytes) {

    override fun getBlob0(): CompletableFuture<ByteArray> = CompletableFuture.completedFuture(bytes)
}
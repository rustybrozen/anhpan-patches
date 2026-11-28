package app.template.patches.zalo

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.deleteResource
import app.morphe.patcher.patch.listApkEntries
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patcher.patch.resourcePatch
import java.io.RandomAccessFile

private const val LIBRARY_PATH = "lib/arm64-v8a/libnative_utils.so"
private const val INIT_BRANCH_OFFSET_261101 = 0x26964L
private const val CONFIG_CALL_OFFSET_261101 = 0x26998L
private const val EXIT_CALL_OFFSET_261101 = 0x2ac48L

private val ORIGINAL_INIT_BRANCH = byteArrayOf(0x00, 0x01, 0x00, 0x36)
private val ORIGINAL_CONFIG_CALL_261101 = byteArrayOf(0x72, 0x10, 0x00, 0x94.toByte())
private val ORIGINAL_EXIT_CALL = byteArrayOf(0x00, 0x01, 0x3f, 0xd6.toByte())
private val ARM64_NOP = byteArrayOf(0x1f, 0x20, 0x03, 0xd5.toByte())

private val keepArm64OnlyResourcePatch = resourcePatch(
    description = "Removes non-arm64 native libraries so the patched arm64 library is used.",
) {
    execute {
        val libraries = listApkEntries("lib/")
        if (LIBRARY_PATH !in libraries) return@execute
        libraries
            .filter { it.startsWith("lib/") && it != "lib/" && !it.startsWith("lib/arm64-v8a/") }
            .forEach { deleteResource(it) }
    }
}

@Suppress("unused")
val anhpanStartupFixPatch = rawResourcePatch(
    name = "Allow patched app to start",
    description = "Prevents Zalo from crashing on startup after patching (arm64 only).",
    default = true,
) {
    compatibleWith(
        Compatibility(
            name = "Zalo",
            packageName = "com.zing.zalo",
            apkFileType = ApkFileType.APKM,
            appIconColor = 0x0068FF,
            targets = listOf(AppTarget(version = "26.11.01")),
        ),
    )

    dependsOn(keepArm64OnlyResourcePatch)

    execute {
        val libFile = get(LIBRARY_PATH, true)
        if (!libFile.exists()) return@execute

        RandomAccessFile(libFile, "rw").use { file ->
            fun readAt(offset: Long, size: Int): ByteArray {
                file.seek(offset)
                val buf = ByteArray(size)
                file.readFully(buf)
                return buf
            }

            val configBytes = readAt(CONFIG_CALL_OFFSET_261101, ORIGINAL_CONFIG_CALL_261101.size)
            if (!configBytes.contentEquals(ORIGINAL_CONFIG_CALL_261101)) return@use

            val initBytes = readAt(INIT_BRANCH_OFFSET_261101, ORIGINAL_INIT_BRANCH.size)
            if (initBytes.contentEquals(ORIGINAL_INIT_BRANCH)) {
                file.seek(INIT_BRANCH_OFFSET_261101)
                file.write(ARM64_NOP)
            }

            val exitBytes = readAt(EXIT_CALL_OFFSET_261101, ORIGINAL_EXIT_CALL.size)
            if (exitBytes.contentEquals(ORIGINAL_EXIT_CALL)) {
                file.seek(EXIT_CALL_OFFSET_261101)
                file.write(ARM64_NOP)
            }
        }
    }
}

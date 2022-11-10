package co.sentinel.dvpn.core.store

import android.content.Context
import com.wireguard.config.BadConfigException
import com.wireguard.config.Config
import timber.log.Timber
import java.io.*
import java.nio.charset.StandardCharsets

/**
 * Configuration store that uses a `wg-quick`-style file for each configured tunnel.
 * TODO replace throws with error handling where needed.
 */
class FileConfigStore(private val context: Context) : ConfigStore {

    @Throws(IOException::class)
    override fun create(name: String, config: Config): Config {
        Timber.d("Creating configuration for tunnel $name")
        val file = fileFor(name)
        if (!file.createNewFile())
            throw IOException("Config file already exists error.")
        FileOutputStream(file, false).use {
            it.write(
                config.toWgQuickString().toByteArray(StandardCharsets.UTF_8)
            )
        }
        return config
    }

    @Throws(IOException::class)
    override fun delete(name: String) {
        Timber.d("Deleting configuration for tunnel $name")
        val file = fileFor(name)
        if (!file.delete())
            throw IOException("Config delete error.")
    }

    override fun enumerate(): Set<String> {
        return context.fileList()
            .filter { it.endsWith(".conf") }
            .map { it.substring(0, it.length - ".conf".length) }
            .toSet()
    }

    private fun fileFor(name: String): File {
        return File(context.filesDir, "$name.conf")
    }

    @Throws(BadConfigException::class, IOException::class)
    override fun load(name: String): Config {
        FileInputStream(fileFor(name)).use { stream -> return Config.parse(stream) }
    }

    @Throws(IOException::class)
    override fun rename(name: String, replacement: String) {
        Timber.d("Renaming configuration for tunnel $name to $replacement")
        val file = fileFor(name)
        val replacementFile = fileFor(replacement)
        if (!replacementFile.createNewFile()) throw IOException("Config already exists.")
        if (!file.renameTo(replacementFile)) {
            if (!replacementFile.delete()) Timber.w("Couldn't delete marker file for new name $replacement")
            throw IOException("Config rename error")
        }
    }

    @Throws(IOException::class)
    override fun save(name: String, config: Config): Config {
        Timber.d("Saving configuration for tunnel $name")
        val file = fileFor(name)
        if (!file.isFile)
            throw FileNotFoundException("Config not found.")
        FileOutputStream(file, false).use { stream ->
            stream.write(
                config.toWgQuickString().toByteArray(StandardCharsets.UTF_8)
            )
        }
        return config
    }
}

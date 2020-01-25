package nl.rdd.snakeyamlkt

import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.api.YamlOutputStreamWriter
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset


/**
 * Alias for a map as returned by snakeyaml-engine.
 * All keys are stringified to ensure type-safety.
 */
typealias YamlMap = Map<String, Any>;


/** Default implementation of [YamlOutputStreamWriter] that throws all encountered [IOException]'s */
class YamlOutputStreamWriterThrowing(
	os: OutputStream,
	cs: Charset = Charset.defaultCharset()
): YamlOutputStreamWriter(os, cs) {
	override fun processIOException(e: IOException) = throw e;
}


val DefaultYamlLoader by lazy {
	val settings = LoadSettings.builder();
	Load(settings.build())
}
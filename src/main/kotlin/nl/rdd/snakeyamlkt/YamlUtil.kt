package nl.rdd.snakeyamlkt

import org.snakeyaml.engine.v2.api.Dump
import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.DumpSettingsBuilder
import org.snakeyaml.engine.v2.api.YamlOutputStreamWriter
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset


/** Default implementation of [YamlOutputStreamWriter] that throws all encountered [IOException]'s */
class YamlOutputStreamWriterThrowing(
	os: OutputStream,
	cs: Charset = Charset.defaultCharset()
): YamlOutputStreamWriter(os, cs) {
	override fun processIOException(e: IOException) = throw e;
}


@Suppress("FunctionName")
fun YamlDumper(builder: DumpSettingsBuilder.() -> Unit = {}): Dump {
	return Dump(DumpSettings.builder().apply(builder).build());
}

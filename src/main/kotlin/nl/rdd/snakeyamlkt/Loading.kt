package nl.rdd.snakeyamlkt

import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.api.LoadSettingsBuilder
import java.io.InputStream
import java.io.Reader


/**
 * Typesafe wrapper around snakeyaml-engine's [Load].
 * Instead of raw objects this loader returns type-safe [YamlObject]'s.
 * @see YamlObject
 */
class YamlLoader(
	settings: LoadSettings
): Load(settings) {

	constructor(builder: LoadSettingsBuilder.() -> Unit = {}):
		this(LoadSettings.builder().apply(builder).build())


	/** Used to wrap snakeyaml-engine's [YamlIterator] in order to wrap all elements in a [YamlObject] */
	private fun <I, O> Iterable<I>.mapped(transformer: (I) -> O): Iterable<O> {
		return object : Iterable<O> { // an iterable... (to say compatible)
			override fun iterator(): Iterator<O> { // that wraps an iterator...
				return object : Iterator<O> {
					// that delegates its methods...
					override fun hasNext() = this@mapped.iterator().hasNext();
					// with a transformation for next
					override fun next() = transformer(this@mapped.iterator().next());
				}
			}
		}
	}


	/**
	 * Parse the only YAML document in a stream.
	 *
	 * @param yamlStream - data to load from (BOM is respected to detect encoding and removed from the data)
	 * @return parsed [YamlObject]
	 * @throws org.snakeyaml.engine.v2.exceptions.YamlEngineException if the YAML is not valid
	 */
	override fun loadFromInputStream(yamlStream: InputStream) = YamlObject(super.loadFromInputStream(yamlStream));

	/**
	 * Parse the only YAML document from a reader.
	 *
	 * @param yamlReader - data to load from (BOM must not be present)
	 * @return parsed [YamlObject]
	 * @throws org.snakeyaml.engine.v2.exceptions.YamlEngineException if the YAML is not valid
	 */
	override fun loadFromReader(yamlReader: Reader) = YamlObject(super.loadFromReader(yamlReader));

	/**
	 * Parse the only YAML document from a string.
	 *
	 * @param yaml - YAML data to load from (BOM must not be present)
	 * @return parsed [YamlObject]
	 * @throws org.snakeyaml.engine.v2.exceptions.YamlEngineException if the YAML is not valid
	 */
	override fun loadFromString(yaml: String) = YamlObject(super.loadFromString(yaml));


	/**
	 * Parse all YAML documents in a stream.
	 * The documents are parsed only when the iterator is invoked.
	 *
	 * @param yamlStream - stream to load from (BOM is respected to detect encoding and removed from the data)
	 * @return an Iterable over the parsed [YamlObject]'s
	 */
	override fun loadAllFromInputStream(yamlStream: InputStream): Iterable<YamlObject> {
		return super.loadAllFromInputStream(yamlStream).mapped { YamlObject(it) };
	}

	/**
	 * Parse all YAML documents from a reader.
	 * The documents are parsed only when the iterator is invoked.
	 * 
	 * @param yamlReader - reader to load from (BOM must not be present)
	 * @return an Iterable over the parsed [YamlObject]'s
	 */
	override fun loadAllFromReader(yamlReader: Reader): Iterable<YamlObject> {
		return super.loadAllFromReader(yamlReader).mapped { YamlObject(it) };
	}

	/**
	 * Parse all YAML documents from a string.
	 * The documents are parsed only when the iterator is invoked.
	 * 
	 * @param yaml - string to load from (BOM must not be present)
	 * @return an Iterable over the parsed [YamlObject]'s
	 */
	override fun loadAllFromString(yaml: String): Iterable<YamlObject> {
		return super.loadAllFromString(yaml).mapped { YamlObject(it) };
	}

}

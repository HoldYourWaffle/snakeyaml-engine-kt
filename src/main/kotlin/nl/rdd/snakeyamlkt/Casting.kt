package nl.rdd.snakeyamlkt


/**
 * @param keyTransformer 'Caster' for the keys, a simple cast by default
 * @param valueTransformer 'Caster' for the values, a simple cast by default
 * @return A safely casted Map<nK, nV>
 */
inline fun <K, V, reified nK, reified nV> Map<K, V>.castMap(
	//FIXME noinline because of weird reified bug(?)
	noinline keyTransformer: (K) -> nK = { it as nK },
	noinline valueTransformer: (V) -> nV = { it as nV }
): Map<nK, nV> {
	val casted = mutableMapOf<nK, nV>();
	for (e in this) {
		casted[keyTransformer(e.key)] = valueTransformer(e.value);
	}
	return casted;
}


/**
 * @param transformer 'Caster' for the elements, a simple cast by default
 * @return A safely casted List<nE>
 */
inline fun <E, reified nE> List<E>.castList(
	transformer: (E) -> nE = { it as nE }
): List<nE> = this.map(transformer);

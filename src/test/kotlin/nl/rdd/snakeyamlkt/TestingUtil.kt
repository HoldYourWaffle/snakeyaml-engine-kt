package nl.rdd.snakeyamlkt


internal val fixture = YamlLoader().loadAllFromInputStream(
	TestCasting::class.java.getResourceAsStream("../../../test.yaml")
).toList();

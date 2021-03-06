@Test
public void passQueryAsJSONStringTest() throws Exception {
    assertAcked(prepareCreate("test").setSettings(SETTING_NUMBER_OF_SHARDS,
         1));

    client().prepareIndex("test", "type1", "1")
        .setSource("field1", "value1_1", "field2", "value2_1")
        .setRefresh(true).get();

    WrapperQueryBuilder wrapper
         = new WrapperQueryBuilder("{ \"term\" : { \"field1\" : \"value1_1\" } }");
    assertHitCount(client().prepareCount()
        .setQuery(wrapper).get(), 1l);

    BoolQueryBuilder bool = boolQuery().must(wrapper).must(new TermQueryBuilder(
        "field2", "value2_1"));
    assertHitCount(client().prepareCount().setQuery(bool).get(), 1l);
}

@Test
public void passQueryAsJSONStringTest() throws Exception {
    client().admin().indices().prepareCreate("test").setSettings(
        ImmutableSettings.settingsBuilder().put("index.number_of_shards", 1)).execute().actionGet();

    client().prepareIndex("test", "type1", "1")
        .setSource("field1", "value1_1", "field2", "value2_1")
        .setRefresh(true).execute().actionGet();

    WrapperQueryBuilder wrapper
         = new WrapperQueryBuilder("{ \"term\" : { \"field1\" : \"value1_1\" } }");
    CountResponse countResponse = client().prepareCount()
        .setQuery(wrapper).execute().actionGet();
    assertHitCount(countResponse, 1l);

    BoolQueryBuilder bool = new BoolQueryBuilder();
    bool.must(wrapper);
    bool.must(new TermQueryBuilder(
        "field2", "value2_1"));

    countResponse = client().prepareCount().setQuery(wrapper).execute().actionGet();
    assertHitCount(countResponse, 1l);
}
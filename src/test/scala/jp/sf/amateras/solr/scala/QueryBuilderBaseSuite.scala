package jp.sf.amateras.solr.scala

import org.scalatest.FunSuite
import jp.sf.amateras.solr.scala.query.DefaultExpressionParser

class QueryBuilderBaseSuite extends FunSuite {
  
  test("copy"){
    val server = SolrServerFactory.dummy(request => ())
    implicit val parser = new DefaultExpressionParser()
    val queryBuilder = new TestQueryBuilder()
    val copied = queryBuilder.id("contentId").highlight("content", 200, "<b>", "</b>")
    
    assert(copied.getId == "contentId")
    assert(copied.getHilightingField == "content")
    assert(copied.getQuery.getHighlight == true)
    assert(copied.getQuery.getHighlightFields().length == 1)
    assert(copied.getQuery.getHighlightFields()(0) == "content")
    assert(copied.getQuery.getHighlightSimplePre() == "<b>")
    assert(copied.getQuery.getHighlightSimplePost() == "</b>")
  }
  
  class TestQueryBuilder extends QueryBuilderBase[TestQueryBuilder]{
    protected def createCopy: TestQueryBuilder = new TestQueryBuilder()
    def getId = this.id
    def getQuery = this.solrQuery
    def getHilightingField = this.highlightField
  }
  

}
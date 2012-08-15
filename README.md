solr-scala-client
=================

The simple [Apache Solr](http://lucene.apache.org/solr/) client for Scala.
This is based on the SolrJ and provides optimal interface for Scala.

Add the following dependency into your build.sbt to use solr-scala-client.

    resolvers += "amateras-repo" at "http://amateras.sourceforge.jp/mvn/"

    libraryDependencies += "jp.sf.amateras.solr.scala" %% "solr-scala-client" % "0.0.2"

This is a simplest example to show usage of solr-scala-client.

    import jp.sf.amateras.solr.scala._

    val client = new SolrClient("http://localhost:8983/solr")

    // register
    client
      .add(Map("id"->"001", "manu" -> "Lenovo", "name" -> "ThinkPad X201s"))
      .add(Map("id"->"002", "manu" -> "Lenovo", "name" -> "ThinkPad X220"))
      .add(Map("id"->"003", "manu" -> "Lenovo", "name" -> "ThinkPad X121e"))
      .commit

    // query
    val result: List[Map[String, Any]] =
      client.query("name: %name%")
        .fields("id", "manu", "name")
        .sortBy("id", Order.asc)
        .getResultAsMap(Map("name" -> "ThinkPad"))

    result.documents.foreach { doc: Map[String, Any] =>
      println("id: " + doc("id"))
      println("  manu: " + doc("manu"))
      println("  name: " + doc("name"))
    }

It's also possible to use the case class as the search result and parameters instead of Map.

    // query
    val result: List[Product] =
      client.query("name: %name%")
        .fields("id", "manu", "name")
        .sortBy("id", Order.asc)
        .getResultAs[Product](Param(name = "ThinkPad"))

    result.documents.foreach { doc: Product =>
      println("id: " + product.id)
      println("  manu: " + product.manu)
      println("  name: " + product.name)
    }

solr-scala-client is not released yet. It's still under development.

This project has been built continuously by [BuildHive](https://buildhive.cloudbees.com/view/My%20Repositories/job/takezoe/job/solr-scala-client/).

TODO
--------

* Detailed query configuration

Release Notes
--------
### 0.0.3 - 16 Aug 2012

* Added case class support in update operations.
* Added commit() method to SolrClient.

### 0.0.2 - 27 May 2012

* Added initializer which configures SolrClient.
* Added basic authentication support as initializer.
* Added facet search support.
* Added case class support as query results and query parameters.

### 0.0.1 - 4 May 2012

* Initial public release.

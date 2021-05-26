package com.github.takezoe.solr.scala

import org.apache.solr.client.solrj.response.UpdateResponse
import org.apache.solr.client.solrj.{SolrClient => ApacheSolrClient}
import org.apache.solr.common.SolrInputDocument
import collection.JavaConverters._

class BatchRegister(server: ApacheSolrClient, collection: Option[String], docs: Map[String, Any]*) {
  add(docs: _*)

  def add(docs: Any*): BatchRegister = {
    val solrDocs = CaseClassMapper.toMapArray(docs: _*).map { doc =>
      val solrDoc = new SolrInputDocument
      doc.collect { case (key, value) =>
        solrDoc.addField(key, value)
      }
      solrDoc
    }.toSeq

    collection match {
        case Some(c) => server.add(c, solrDocs.asJava)
        case None => server.add(solrDocs.asJava)
      }
    this
  }

  // def add(docs: Any*): BatchRegister = {
  //   CaseClassMapper.toMapArray(docs: _*).foreach { doc =>
  //     val solrDoc = new SolrInputDocument
  //     doc.collect { case (key, value) =>
  //       solrDoc.addField(key, value)
  //     }
  //     collection match {
  //       case Some(c) => server.add(c, solrDoc)
  //       case None => server.add(solrDoc)
  //     }
  //   }
  //   this
  // }

  def commit(): UpdateResponse = server.commit

  def commit(collection: String): UpdateResponse = server.commit(collection)

  def rollback(): UpdateResponse = server.rollback

}

package com.julia.dagger

import com.julia.dagger.DagRunner.{Node, extractMap, findRootNodes, printChildNodes}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object DagRunner {

  implicit val jsonDefaultFormats: DefaultFormats.type = DefaultFormats

  case class Node(start: Option[Boolean], edges: Map[String, Int])

  /**
    * Extract a mapping of a node to neighbor nodes
    *
    * @param json input
    * @return mapping of a node to neighbor nodes
    */
  def extractMap(json: String): Map[String, Node] = {
    parse(json).extract[Map[String, Node]]
  }

  /**
    * Given a map, provide a list of root node keys
    *
    * @param map input
    * @return a list of root node keys
    */
  def findRootNodes(map: Map[String, Node]): immutable.Iterable[String] = {
    map.collect {
      case (str, Node(Some(true), _)) => str
    }
  }

  /**
    * Determine if a specific node has children or not
    *
    * @param nodeId node id of interest
    * @param map input map to look through
    * @return whether or not a node has a child
    */
  def hasChildNode(nodeId: String, map: Map[String, Node]): Boolean = {
    map(nodeId).edges.nonEmpty
  }

  def printChildNodes(node: String, map: Map[String, Node]): Unit = {
    for ((nodeId, time) <- map(node).edges) yield {
      val future = futureChild(nodeId, time)
      if (hasChildNode(nodeId, map)) {
        future.onComplete {
          case Success(_) => printChildNodes(nodeId, map)
          case Failure(_) =>
        }
      }
    }
  }

  def futureChild(node: String, time: Int): Future[Unit] = {
    Future {
      Thread.sleep(time * 1000) // convert to ms
      println(node)
    }
  }

}

class DagRunner(val json: String) {
  def main = {
    val extractedMap: Map[String, Node] = extractMap(json)
    val rootNodes: immutable.Iterable[String] = findRootNodes(extractedMap)
    rootNodes.foreach { rootNode =>
      {
        println(rootNode)
        printChildNodes(rootNode, extractedMap)
      }
    }
  }
}
package com.julia.dagger

import com.julia.dagger.DagRunner.{Node, extractMap, findRootNodes, hasChildNode}
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
    * @param nodeId node ID of interest
    * @param map    input map to look through
    * @return whether or not a node has a child
    */
  def hasChildNode(nodeId: String, map: Map[String, Node]): Boolean = {
    map(nodeId).edges.nonEmpty
  }

}

class DagRunner(val json: String) {
  var nodesAlreadyPrinted: Set[String] = Set.empty

  def runGraph(): Unit = {
    val extractedMap: Map[String, Node] = extractMap(json)
    val rootNodes: immutable.Iterable[String] = findRootNodes(extractedMap)
    rootNodes.foreach { rootNode =>
      {
        println(rootNode)
        printChildNodes(rootNode, extractedMap)
      }
    }
  }

  /**
    * Given a root node print all its children
    *
    * @param rootNode ID of root node
    * @param map      input map
    */
  def printChildNodes(rootNode: String, map: Map[String, Node]): Unit = {
    for ((nodeId, time) <- map(rootNode).edges) yield {
      val future = futureChild(nodeId, time)
      if (hasChildNode(nodeId, map)) {
        future.onComplete {
          case Success(_) => printChildNodes(nodeId, map)
          case Failure(_) =>
        }
      }
    }
  }

  /**
    * Set a future for child node
    *
    * @param node node ID
    * @param time time after which to print child node
    */
  def futureChild(node: String, time: Int): Future[Unit] = {
    Future {
      Thread.sleep(time * 1000) // convert to ms
      // do not print nodes that have already been printed
      if (!nodesAlreadyPrinted.contains(node)) {
        println(node)
        nodesAlreadyPrinted += node
      }
    }
  }
}

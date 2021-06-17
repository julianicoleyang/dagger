package com.julia.dagger

import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, jvalue2extractable}
import org.mockito.Mockito
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

object DagRunnerTest {
  implicit val jsonDefaultFormats: DefaultFormats.type = DefaultFormats

  val zeroRootNodes: String =
    """
      |{
      | "A": {"edges": {"B": 5, "C": 7}},
      | "B": {"edges": {}},
      | "C": {"edges": {}}
      |}
      |""".stripMargin

  val twoRootNodes: String =
    """
      |{
      | "A": {"start": true, "edges": {"B": 5, "C": 7}},
      | "B": {"edges": {"D": 1}},
      | "C": {"edges": {}},
      | "D": {"edges": {}},
      | "E": {"start": true, "edges": {"B": 6, "F": 1, "G": 9}},
      | "F": {"edges": {}},
      | "G": {"edges": {}}
      |}
      |""".stripMargin

  val extractedMap: Map[String, DagRunner.Node] = parse(twoRootNodes).extract[Map[String, DagRunner.Node]]
}

class DagRunnerTest extends Mockito with Matchers with AnyFlatSpecLike {

  import DagRunner._
  import DagRunnerTest._

  implicit val jsonDefaultFormats: DefaultFormats.type = DefaultFormats

  behavior of "findRootNodes"

  it should "find all root nodes" in {
    val map = extractMap(twoRootNodes)
    findRootNodes(map) shouldEqual List("E", "A")
  }

  it should "if no root nodes exist" in {
    val map = extractMap(zeroRootNodes)
    findRootNodes(map) shouldEqual List.empty
  }

  behavior of "hasChildNode"

  it should "return true if node has children" in {
    hasChildNode("A", extractedMap) shouldEqual true
  }

  it should "return false if node has children" in {
    hasChildNode("C", extractedMap) shouldEqual false
  }
}

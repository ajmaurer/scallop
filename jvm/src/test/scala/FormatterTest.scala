package org.rogach.scallop

import org.scalatest.{ FunSuite, Matchers }

class FormatterTest extends FunSuite with Matchers {
  test ("exact wrapping") {
    // Two lines' worth of dots, two dots to start (one dot should be at the very end of the line).
    val dots = ".." +: Seq.fill(79)(".")
    val formatted = Formatter.wrap(dots, 80)
    formatted should have length (2)
    formatted(0) should have length (80)
    formatted(0) should equal ((".." +: Seq.fill(39)(".")).mkString(" "))
    formatted(1) should have length (80)
    formatted(1) should equal (Seq.fill(40)(".").mkString(" ") + " ")
  }

  test ("one-off wrapping") {
    // Two lines' worth of dots.
    val dots = Seq.fill(80)(".")
    val formatted = Formatter.wrap(dots, 80)
    formatted should have length (2)
    formatted(0) should have length (80)
    formatted(0) should equal (Seq.fill(40)(".").mkString(" ") + " ")
    formatted(1) should have length (80)
    formatted(1) should equal (Seq.fill(40)(".").mkString(" ") + " ")
  }

  test ("exactly-fitting argument formatting") {
    val args = List(
      Some(HelpInfo("-a, --apples", "* * * *", () => None))
    )
    val formatted = Formatter.format(args, Some(20), false)
    formatted.split("\n").foreach { line =>
      line should have length(20)
    }
    formatted should equal ("""  -a, --apples   * *
                              |                 * *""".stripMargin)
  }

  test ("almost-exactly fitting argument formatting") {
    val args = List(
      Some(HelpInfo("-a, --apple", "* * * *", () => None))
    )
    val formatted = Formatter.format(args, Some(20), false)
    // Should format with a trailing whitespace to exactly 20 chars.
    formatted.split("\n").foreach { line =>
      line should have length(20)
    }
    // Note the trailing spaces on the first line.
    formatted shouldEqual "  -a, --apple   * * \n                * * "
  }

  test ("long-only argument formatting") {
    val args = List(
      Some(HelpInfo("--apples", "* * * *", () => None))
    )
    val formatted = Formatter.format(args, Some(20), false)
    formatted.split("\n").foreach { line =>
      line should have length(20)
    }
    formatted should equal ("""      --apples   * *
                              |                 * *""".stripMargin)
  }
}

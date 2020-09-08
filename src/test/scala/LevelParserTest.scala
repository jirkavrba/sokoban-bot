import dev.vrba.sokobanbot.game.level.parser._
import dev.vrba.sokobanbot.game.util.Location
import org.scalatest.funsuite.AnyFunSuite

class LevelParserTest extends AnyFunSuite {
  test("Parses correctly parses valid levels") {
    val first = LevelParser.parse(
      """|....WWWWW..........
         |....W...W..........
         |....WB..W..........
         |..WWW..BWW.........
         |..W..B.B.W.........
         |WWW.W.WW.W...WWWWWW
         |W...W.WW.WWWWW..TTW
         |W.B..B..........TTW
         |WWWWW.WWW.WPWW..TTW
         |....W.....WWWWWWWWW
         |....WWWWWWW........""".stripMargin)

    // No error encountered
    assert(first.isRight)
    first.foreach(
      l => {
        assert(l.player == Location(11, 8))
        assert(l.boxes == Set(Location(5, 2), Location(5, 7), Location(7, 4), Location(5, 4), Location(2, 7), Location(7, 3)))
      }
    )

    val second = LevelParser.parse(
      """|WWWWWWWWWWWW..
         |WTT..W.....WWW
         |WTT..W.B..B..W
         |WTT..WBWWWW..W
         |WTT....P.WW..W
         |WTT..W.W..B.WW
         |WWWWWW.WWB.B.W
         |..W.B..B.B.B.W
         |..W....W.....W
         |..WWWWWWWWWWWW""".stripMargin)

    assert(second.isRight)
    second.foreach(
      l => {
        assert(l.player == Location(7, 4))
        assert(l.boxes == Set(Location(9, 7), Location(11, 6), Location(10, 5), Location(11, 7), Location(10, 2), Location(9, 6), Location(4, 7), Location(6, 3), Location(7, 7), Location(7, 2)))
      }
    )

    // TODO: Add more tests for valid levels?
  }

  test("Cannot parse unknown literals and returns correct error") {
    val level = LevelParser.parse("WXWWW")
    assert(level.isLeft)
    assert(level.swap.getOrElse().isInstanceOf[UnknownLiteral.type])
  }

  test("Cannot parse empty input and returns correct error") {
    val level = LevelParser.parse("")
    assert(level.isLeft)
    assert(level.swap.getOrElse().isInstanceOf[EmptyFile.type])
  }

  test("Cannot parse non-rectangle input and returns correct error") {
    val level = LevelParser.parse(
      """|......
         |.W...P...
         |...W....
         |.......""".stripMargin)
    assert(level.isLeft)
    assert(level.swap.getOrElse().isInstanceOf[OutOfBounds.type])
  }

  test("Cannot parse input with no boxes and returns correct error") {
    {
      val level = LevelParser.parse(
        """|.........
           |.W...P...
           |...W.....
           |.........""".stripMargin)
      assert(level.isLeft)
      assert(level.swap.getOrElse().isInstanceOf[NoTargetsAndBoxes.type])
    }

    {
      val level = LevelParser.parse(
        """|......T..
           |.W...P...
           |...W.....
           |.........""".stripMargin)
      assert(level.isLeft)
      assert(level.swap.getOrElse().isInstanceOf[NoTargetsAndBoxes.type])
    }
  }

  test("Cannot parse input with mismatched number of boxes and targets and returns correct error") {
    {
      val level = LevelParser.parse(
        """|..B...B..
           |.W.T.P...
           |...W.....
           |......B..""".stripMargin)
      assert(level.isLeft)
      assert(level.swap.getOrElse().isInstanceOf[MismatchedBoxesAndTargets.type])
    }

    {
      val level = LevelParser.parse(
        """|..T...T..
           |.W.T.P...
           |...W.....
           |......B..""".stripMargin)
      assert(level.isLeft)
      assert(level.swap.getOrElse().isInstanceOf[MismatchedBoxesAndTargets.type])
    }
  }

  test("Cannot parse input with multiple player locations and returns correct error") {
    val level = LevelParser.parse(
      """|..T...B..
         |.W.T.P...
         |...W.....
         |..P...B..""".stripMargin)
    assert(level.isLeft)
    assert(level.swap.getOrElse().isInstanceOf[DuplicatedPlayerLocation.type])
  }

  test("Cannot parse input without player location and returns correct error") {
    val level = LevelParser.parse(
      """|..T...B..
         |.W.T.....
         |...W.....
         |..W...B..""".stripMargin)
    assert(level.isLeft)
    assert(level.swap.getOrElse().isInstanceOf[NoPlayerLocation.type])
  }
}


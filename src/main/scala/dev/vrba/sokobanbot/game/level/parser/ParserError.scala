package dev.vrba.sokobanbot.game.level.parser

sealed trait ParserError

case object UnknownLiteral            extends ParserError
case object EmptyFile                 extends ParserError
case object OutOfBounds               extends ParserError
case object DuplicatedPlayerLocation  extends ParserError
case object MismatchedBoxesAndTargets extends ParserError
case object NoTargetsAndBoxes         extends ParserError
case object NoPlayerLocation          extends ParserError
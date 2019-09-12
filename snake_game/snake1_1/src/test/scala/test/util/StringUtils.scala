// DO NOT MODIFY FOR BASIC SUBMISSION

package test.util

// the generic code in this file is
// currently used in the testing infrastructure
object StringUtils {


  def twoColumnTable(headerA: String, headerB: String,
                     multiLineContentA: String,
                     multiLineContentB: String,
                     divider: String = " | "): String = {
    val columnALines = headerA +: multiLinesStringToLines(multiLineContentA)
    val columnBLines = headerB +: multiLinesStringToLines(multiLineContentB)
    val maxWidthA = widthOfStringLines(columnALines)
    val maxWidthB = widthOfStringLines(columnBLines)

    val nrLines = columnALines.length max columnBLines.length
    val columnAPadded = ensureStringLinesDimensions(columnALines, nrLines, maxWidthA)
    val columnBPadded = ensureStringLinesDimensions(columnBLines, nrLines, maxWidthB)

    val headerAPadded = columnAPadded.head
    val headerBPadded = columnBPadded.head
    val contentAPadded = columnAPadded.tail
    val contentBPadded = columnBPadded.tail
    val header = sideBySide(headerAPadded, divider, headerBPadded)
    val width = maxWidthA + divider.length + maxWidthB
    val hLine = horizontalLineOfWidth(width)
    val content = (contentAPadded, contentBPadded).zipped.map(sideBySide(_, divider, _))

    (header +: hLine +: content).mkString("\n")
  }

  def horizontalLineOfWidth(width: Int, hlineChar: Char = '-'): String = hlineChar.toString * width

  def withHeader(header: String, content: String): String = {
    val contentLines = multiLinesStringToLines(content)
    val width = header.length max contentLines.length
    val hLine = horizontalLineOfWidth(width)
    (header +: hLine +: contentLines).mkString("\n")
  }

  def sideBySide(a: String, inbetween: String, b: String): String = a + inbetween + b

  def ensureStringLinesDimensions(lines: Seq[String],
                                  nrLines: Int,
                                  width: Int,
                                  pad: Char = ' '): Seq[String] = {
    val paddedLines = lines.map(ensureWidthOfString(_, width))
    ensureStringLinesHeight(paddedLines, nrLines)
  }

  def widthOfStringLines(lines: Seq[String]): Int =
    lines.map(_.length).max

  def ensureWidthOfString(s: String, width: Int, pad: Char = ' '): String =
    s ++ (pad.toString * (width - s.length))

  def padLinesToMaxWidth(lines: Seq[String]): Seq[String] = {
    val width = widthOfStringLines(lines)
    lines.map(ensureWidthOfString(_, width))
  }

  def ensureStringLinesHeight(lines: Seq[String],
                              height: Int,
                              pad: Char = ' '): Seq[String] = {
    val width = widthOfStringLines(lines)
    val nrEmptyLines = (height - lines.length ) max 0
    val emptyLine = pad.toString * width
    val emptyLines = Seq.fill(nrEmptyLines)(emptyLine)
    lines ++ emptyLines
  }

  def widthOfMultilineString(string: String): Int =
    widthOfStringLines(multiLinesStringToLines(string))

  def nrLinesInString(s: String): Int = multiLinesStringToLines(s).length

  def multiLinesStringToLines(s: String): Seq[String] = s.split("\n").toSeq

  /**
    * Prints a string as a Scala multiline string.
    * {{{
    *  "line1\nsecondline\nthirdline" becomes:
    *  """line
    *     |secondline
    *     |thirdline"""
    * }}}
    *
    *
    */
  def asIndentendMultilineString(s: String, indentWidth: Int): String = {
    val indent: String = (" " * indentWidth) + "|"
    val lines: Seq[String] = multiLinesStringToLines(s)
    val indentedLines: Seq[String] = lines.tail.map(indent + _)
    val multilineStringQuote = "\"\"\""
    val indentFirst = " " * (indentWidth - multilineStringQuote.length)

    indentFirst + multilineStringQuote + lines.head + "\n" +
      indentedLines.mkString("\n") + multilineStringQuote
  }

}

package dataset

import java.text.SimpleDateFormat
import java.util.SimpleTimeZone
import java.util.Date
import dataset.util.Commit.Commit
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}

import scala.io.Source
import scala.math.Ordering.Implicits._

/**
 * PART 5C - Mining Software Repositories
 *
 * Use your knowledge of functional programming to complete the following function.
 * You are recommended to use library functions when possible.
 *
 * The data is provided as a list of `Commit`s. This case class can be found in util/Commit.scala.
 * When asked for dates, use the `commit.commit.committer.date` field.
 *
 * This part is worth 7 points.
 */
object Part5C {
  implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)
  val source: List[Commit] = Source.fromResource("1000_commits.json").getLines().map(Serialization.read[Commit]).toList

  /** Q31 (7p)
   *
   * A day has different parts:
   * Morning 5 am to 12 pm (noon)
   * Afternoon 12 pm to 5 pm.
   * Evening 5 pm to 9 pm.
   * Night 9 pm to 4 am.
   *
   * Which part of the day was the most productive in terms of commits ?
   * Return a tuple with the part of the day and the number of commits
   *
   * Hint: for the time, use `SimpleDateFormat` and `SimpleTimeZone`.
   */
  def mostProductivePart(input: List[Commit]): (String, Int)  = {
    val sdf = new SimpleDateFormat("HH");
    sdf.setTimeZone(new SimpleTimeZone(0, "UTC"));
    List(("morning", input.map(x => sdf.format(x.commit.committer.date).toInt).count(x => x >= 5 && x < 12)),
        ("afternoon", input.map(x => sdf.format(x.commit.committer.date).toInt).count(x => x >= 12 && x < 17)),
        ("evening", input.map(x => sdf.format(x.commit.committer.date).toInt).count(x => x >= 17 && x < 21)),
        ("night", input.map(x => sdf.format(x.commit.committer.date).toInt).count(x => x >= 21 || x < 5))).maxBy(_._2)
  };


  // END OF PART 5C & END OF THE LAB ^_^
  // Hope you enjoyed it and good luck with the next assignment!
}
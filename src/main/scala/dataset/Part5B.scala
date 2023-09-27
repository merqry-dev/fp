package dataset

import dataset.util.XMLDatafile.Badge
import scala.io.Source

/**
 * PART 5A - DATASET2 / StackOverFlow Badges
 *
 * In this assignment you will be asked to finish reading in a not quite xml file
 * The file is one big list of lines such asked
 *
 * <row Id="1" UserId="3" Name="Autobiographer" Date="2012-03-06T18:53:16.300" Class="3" TagBased="False" />
 *
 * For this assignment you first have to prep your data a bit, and then it's 
 * on to answering questions. This part is worth 9 points
 */
object Part5B {

  /** Q28 (3p)
   * Included is a first example of reading in a file:
   * `sourceAsListString` generates a `List[String]`
   *
   * We would like you to finish `source` converting this into a List of
   * case class Badge, i.e. make sure the return type for source is
   * `List[Badge], you can find Badge in the util folder
   */
  val sourceAsListString = Source.fromResource("First6200Badges.xml").getLines.toList.drop(2).dropRight(1)


  val source: List[Badge] = Source.fromResource("First6200Badges.xml").getLines().toList.drop(2).dropRight(1)
    .map(line => {
      val fields = line.split("\"").toList
      Badge(fields(1).toInt, fields(3).toInt, fields(5), fields(7), fields(9).toInt, fields(11).toBoolean)
    })

  /**
   * Again you can use this to get some output
   */
  def main(args: Array[String]): Unit = {
    println(showResults(sourceAsListString))
  }

  def showResults(input: List[String]): Unit = input.foreach(println)


  /** Q29 (3p)
   *
   * What is the easiest attainable badge? Output a tuple of its name and nr
   */
  def easiestAttainableBadge(input: List[Badge]): (String, Int) = input.groupBy(_.name).mapValues(_.size).maxBy(_._2);

  /** Q30 (3p)
   *
   * Return a tuple of tuples of the least productive and most productive
   * year, together with the nr of badges earned
   */
  def yearOverview(input: List[Badge]): ((Int, Int), (Int, Int)) = {
    val inter = input.groupBy(a => a.badgeDate.split("-")(0)).toList.sortBy(a => a._2.size)

    ((inter.head._1.toInt, inter.head._2.size), (inter.last._1.toInt, inter.last._2.size))
  }
  // END OF PART 5B

}

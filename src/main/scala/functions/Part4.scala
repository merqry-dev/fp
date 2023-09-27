package functions

/* PART 4 - MORE FUNCTIONS
 * 
 * This part is worth 35 points
 *
 * In summary, what you've seen so far is:
 * - basic function and lambda definitions
 * - pattern matching on Lists and case classes
 * - the use of API methods
 * - daisy chaining method calls just like Unix pipelines
 *
 * Next up is a few more examples I'd like to show you before you start
 * answering the more advanced questions
 *
 * 1. SUM
 * 
 * Remember the pattern matching on sum? 
 *
 * 		def sum(xs: List[Int]) : Int = xs match {
 *  		case List() => 0
 * 			case h :: t => h + sum(t)
 *		}
 *
 * Here are a few other ways of writing it:
 * 
 * def sum2(xs: List[Int]) : Int = if (xs.isEmpty) 0 else xs.head + sum2(xs.tail)
 * def sumAPI1(xs: List[Int]) = xs.foldLeft(0)(_ + _)
 * def sumAPI2(xs: List[Int]) = xs.foldRight(0)(_ + _)
 * def sumAPI3(xs: List[Int]) = xs.sum  
 * 
 * def sumIterative(xs: List[Int]) : Int = {
 *   var sum  = 0;
 * 	 for (x <- xs) {
 *     sum = sum + x
 *   }
 *   sum
 * }
 * STOP!!! STOP!!! STOP!!! Remember no var?, the for expression is fine, it's 
 * just that this method mutates the `sum` var and we don't allow that.
 *
 * 2. Folds
 *
 * the foldLeft/foldRight are an interesting bunch. They can be very powerful
 * what the foldLeft does in this example is take from left two right, two 
 * elements from the list and feed it as parameters into the function.
 * In this case `_ + _` is the shorthand for ( (x: Int, y:Int) => x + y) 
 * Some people call these reduce, but that is not correct. While similar
 * please see the following outputs (I've left out the `println`):
 * 
 * // foldLeft
 * List.range(0,5).foldLeft(0)(_ + _) 					// 10 with a shorthand lambda
 * List.range(0,5).foldLeft(0)((x:Int, y:Int) => x+y) 	// 10
 * List(1).foldLeft(0)((x:Int, y:Int) => x+y) 			// 1
 * List().foldLeft(0)((x:Int, y:Int) => x+y) 			// 0
 *
 * // compiler will warn about overloaded method and compiler doesn't know 
 * // what to pick, so use the fully written lambda to prevent such warnings
 * List().foldLeft(0)(_ + _)) 
 * 
 * // reduceLeft
 * // type is needed or else warning, but with type still an exception
 * List[Int]().reduceLeft(_+_) 							
 *
 * List(7).reduceLeft(_+_) 								// 7
 * List(1,4,5).reduceLeft(_+_) 							// 10
 *
 * As you can see there are some minor, but tricky differences
 * Another one to note is if your function isn't associative, then 
 * foldLeft (going from left to right), will give you another result than 
 * foldRight (going from right to left). You're highly recommended to look up
 * some more examples.
 *
 * 3. Flattening/Options
 * 
 * Next I'd like to show you a few things that can happen when working with
 * Options, some of which aren't quite straightforward at first.
 *
 * Say you have a variable called results of type List[Option[Int]] and want 
 * an easy way to filter out the Nones:
 * 
 * val results = List(Some(6), Some(8), None, None, Some(9))
 *
 * results.flatten 			 // List(6,8,9) the Options are 'unwrapped', i.e. Some peeled off and None thrown out
 * results.flatMap(x => x) 	 // does the same (first calls map, then flattens the result)
 * results.flatMap(identity) // syntactic sugar for `x => x`
 * 
 * while this is very cool to see Option unwrapped, below are a few more examples
 * which show that working with Option sometimes can be a bit challenging.
 *
 * Let's say you have a competition and with some different rounds.
 * The administration kept track of contestants name age and hobby as a 
 * Tuple3(String, Int, String). The finale is aired on tv and the host
 * only cares about the name and age of the contestants. He has a list of 
 * the persons who qualify for the final. If a contestant did not make it through
 * the last round, `None` is registered. The final contestants look like this:
 * 
 * val contestants = List(
 *                   Some(("Richard", 26, "windsurfing")), 
 *                   None, 
 *                   Some(("Amy", 22, "bmx"))
 *	                 )
 *
 * If the tv show host only wants their name and age:
 *
 * 	contestants.flatMap(x => if (x.isDefined) Some(x.get._1, x.get._2) else None)// List((Richard,26), (Amy,22))
 * 
 * other things the host could do:
 *
 * val richard = contestants(0).get 											// (Richard, 26, windsurfing)
 * //val oops = contestants(1).get 												// exception, because of None.get
 * val contestant = if (contestants(1).isDefined) contestants(1).get else None 	// None
 * 
 * BEWARE OF TRANSFORMATIONS!!
 *
 * // if contestants failed the earlier rounds, they get transformed from `None`
 * // to "failedContestant"
 * 
 * 	println(contestants.flatMap(y => y match {
 *   	case None => "failedContestant"					
 *   	case Some((name, age, hobby)) => Some(name, age) 
 * 	}))
 * 	// results in List((Richard,26), f, a, i, l, e, d, C, o, n, t, e, s, t, a, n, t, (Amy,22))
 * 
 * Forgetting Some in case Some results in a compiler warning; Scala can't 
 * flatMap the (name, age), but the trickier issue is the String in the current
 * implementation. Strings can be flatmapped resulting in the characters 
 * printed out separately. Beware of this and try to build your solution 
 * step by step!
 *
 * 4. Case Classes
 * 
 * There is one last thing I'd like to show you before getting to the questions
 * Simple definition of a case class and one instance:
 *
 * case class Book(isbn13 : String, translations: List[String], title: String)
 * val lotr = Book("9780395647394", List("EN", "NL", "FR"), "Lord of the Rings, Part 2: The Two Towers")
 * 
 * if you want the list of languages into which it was translated call:
 * val translatedTo = lotr.translations 
 * 
 * i.e. you can use the .fieldName of the case class to get that specific field!
 * 
 * You can read more about case classes here: 
 * - https://docs.scala-lang.org/tour/case-classes.html
 * - https://docs.scala-lang.org/overviews/scala-book/case-classes.html
 * 
 * 5. Tuples and indexing
 * 
 * Last thing for me to tell you is an example with tuples. Scala supports up 
 * to Tuple22 and you can index each element of a tuple with ._indexnr
 *
 * val ingredient = ("Sugar", 25)
 * println(ingredient._2) // outputs 25
 *
 * You can also nest these, for example:
 *
 * val recipe = (("tangerine", 2.0),("celery", 0.25),("cucumber", 1.0))
 *
 * println(recipe._1) 		// (tangerine,2.0) 
 * println(recipe._2._2) 	// 0.25
 *
 * so far so good, but...    are you ready? ... the type of recipe is...
 * 
 * Tuple3[Tuple2[String, Double], Tuple2[String, Double],Tuple2[String, Double]]
 * 
 * yes, that is mindboggling for something so simple...
 * So if you ever need to work with tuples, sometimes it's good to just take
 * one element, write it out or sort it out and see how to get to the element
 * you are interested in. Scastie (online Scala interpreter) is a good scratchpad
 * to try your scribbling out on...
 *
 * That's about the end of me ranting and trying to show you some nice Scala
 * tips and tricks. Good luck with part 4 and part 5!
 */
object Part4 {
  // PART 4 - MORE FUNCTIONS - EXERCISES

  /** Q16 (2p)
   * Reverse a list using the List method foldLeft
   *
   * Example:
   * reverseUsingFold(List.range(-3,2)) // List(1, 0, -1, -2, -3)
   */
  def reverseUsingFold[A](nrs: List[A]): List[A] = nrs.foldLeft(List[A]())((a,b) => b :: a)

  /** Q17 (5p)
   * find the second largest element in the list of Ints, if it exists
   *
   * @param xs : the List[Int] to search
   * @return an Option[Int], i.e. Some(x: Int) that is the next largest element in the list
   *         or None if it does not exist
   *
   *         Examples:
   *         secondLargest(List(1,2,3,4,5,6)) 	// Some(5)
   *         secondLargest(List(1,1,1,1)) 		// None
   */
  // TODO: implement method using Pattern Matching, API calls to standard API or a combination (Hint Hint..)
  // no imports are necessary
  def secondLargest(xs: List[Int]): Option[Int] = ???

  /** Q18 (5p)
   * Count the number of occurrences of each distinct number
   * Example:
   * countNumbers(List(1,2,1,2,1,2,3,4,4,4,4,4,4,4,4)) // Map(1 -> 3, 2 -> 3, 3 -> 1, 4 -> 8)
   */
  def countNumbers(xs: List[Int]): Map[Int, Int] = Map(xs map {a => (a, xs.count(b => b == a))} : _*)

  /** Q19 (2p)
   * Ai... 13 is always a bad number... A hacker infiltrated the Scala Deployment server and hacked the API
   * The hacker removed the function `distinct` from the API. You have been asked to create a workaround
   * using the standard API... and only for two points... cheapskates...
   *
   * @param xs the list to process
   * @return a list with all the duplicates filtered out
   *
   *         Example:
   *         distinctAPI(List(1,2,1,2,1,2,3,4,4,4,4,4,4,4,4)) // List(1,2,3,4)
   */
  // TODO: implement `distinct` without using .distinct on the list
  def distinctAPI[A](xs: List[A]): List[A] = ???

  /** Q20 (3p)
   * Special partition takes all numbers, adds one to the negative numbers and checks
   * if all those together have the same absolute value as all the positive numbers
   *
   * Example:
   * specialPartition(List(List(-4,1,2), List(-4,-4,-6,1,2,4,4), List(-4,-4, -6, 1,2,8))) // true
   */
  // TODO: implement using API methods
  def specialPartition(xs: List[List[Int]]): Boolean = { //!!
    val temp1 = xs.foldLeft(List[Int]())((a,b) => List.concat(a,b)).filter(a => a<0)
    val num = temp1.size
    val temp2 = (temp1.sum + num).abs

    temp2 == xs.foldLeft(List[Int]())((a,b) => List.concat(a,b)).filter(a => a>0).sum
  }

  val tudScrabblePlayers: List[(String, List[(String, Int)])] = List(
    ("Ola", List(("tree", 4), ("plant", 12), ("water", 10), ("earth", 9), ("heaven", 1))),
    ("Liudas", List(("hop", 3), ("editor", 7), ("mileage", 8), ("load", 4), ("quintuple", 16))),
    ("Roald", List(("language", 8), ("three", 5), ("hip", 3))),
    ("Burçu", List(("beverage", 11), ("institution", 14), ("player", 8), ("feedback", 7), ("positive", 9), ("login", 5))),
    ("Georgios", List(("functional", 9), ("program", 7), ("facebook", 6)))
  )

  /** Q21 (5p)
   * It's finally Friday and you and your friends are going to play a game of Scrabble
   * from the given `scrabblePlayers` determine the good players.
   * A good player has put down only words of length larger than 3.
   *
   * in `tudScrabblePlayers` Ola, Burçu, and Georgios are good players.
   */
  // TODO: implement this method
  def determineGoodPlayers(scrabblePlayers: List[(String, List[(String, Int)])]): List[String] = {

    scrabblePlayers.filter(a => a._2.filter(b => b._1.length > 3).size > 0 && a._2.filter(b => b._1.length > 3).size == a._2.size).map(a => a._1)
  }

  /** Q22 (3p)
   * determine from good Scrabble players who has an average value per word of over 7
   * return their name and the average value of words put down
   * for the above example we expect:
   * List((Ola,7.2), (Burçu,9.0), (Georgios,7.333333333333333))
   */
  def avgOver7(scrabblePlayers: List[(String, List[(String, Int)])]): List[(String, Double)] = ???

  /** Q23 (5p)
   * Given an anonymised list of students who sport and another list of
   * all student results where the anonymised names relate, i.e. student "a"
   * on the sport list is the same student as student "a" in the list with
   * results from all students....
   *
   * find the students who sport, who showed up to ALL their exams AND passed
   * all of them (grade >= 6)
   * val asr = List(// list of student tuples with (name, course, Option[result])
   * ("a", "bdp", Some(8)), ("a", "ci", Some(9)), ("a", "dm", None),
   * ("b", "bdp", Some(7)), ("b", "ci", Some(7)), ("b", "dm", Some(7)),
   * ("c", "bdp", Some(6)), ("c", "ci", Some(3)), ("c", "dm", Some(9)),
   * ("d", "bdp", Some(4)), ("d", "ci", Some(9)), ("d", "dm", Some(3)),
   * ("e", "bdp", Some(7)), ("e", "ci", Some(8)), ("e", "dm", Some(8)),
   * ("f", "bdp", Some(6)), ("f", "ci", Some(6)), ("f", "dm", Some(8)),
   * ("g", "bdp", Some(6)), ("g", "ci", Some(6)), ("g", "dm", Some(9))
   * )
   *
   * val ss = List("a", "d", "f","g") // only "f"/"g" would be considered 'good'
   */
  def goodSportStudents(studentResults: List[(String, String, Option[Int])], sportStudents: List[String]): List[String] = {

    sportStudents.filter(a => studentResults.filter(b => b._1 == a && b._3.isDefined && b._3.get >= 6) == studentResults.filter(b => b._1 == a))
  }

  /** Q24 (5p)
   *
   * The institution of the sporting students from the previous questions
   * would like to know what the average grade of each of the good sporting
   * students is.
   *
   * The expected output for this question with the previous students (from Q22)
   * would be: List((f,6.666666666666667), (g,7.0))
   */
  def avgGradeGoodSportStudents(studentResults: List[(String, String, Option[Int])], sportStudents: List[String]): List[(String, Double)] = ???

  // END OF PART 4
}
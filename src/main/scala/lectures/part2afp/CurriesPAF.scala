package lectures.part2afp

object CurriesPAF extends App {
  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))

  println(superAdder(3)(5)) // curried function - received multiple parameter list

  // Method!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  // Function value
  // We want to use function for HOF. Method cant be used
  val add4: Int => Int = curriedAdder(4)

  // lifting = ETA-EXPANSION
  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1

  List(1, 2, 3).map(inc) // ETA-expansion
  List(1, 2, 3).map(x => inc(x)) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int . This _ is valid for Scala 2 but for Scala 3, this is deprecated

  //Exercise
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!

  val add7_1 = (x: Int) => simpleAddFunction(7, x) // simplest
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int)
  val add7_3 = curriedAdder(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax
  val add7_5 = simpleAddMethod(7, _: Int) //alternative syntax for turning methods into function values
  // y => simpleAddMethod(7, y)

  //Validation
  println("add7_1", add7_1(5))
  println("add7_2", add7_2(5))
  println("add7_3", add7_3(5))
  println("add7_4", add7_4(5))
  println("add7_5", add7_5(5))
  println("add7_6", add7_6(5))

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c

  val insertName = concatenator("Hello, I'm ", _: String, ",how are you?") // x: String => concatenator(hello, x, howareyou)
  println(insertName("Daniel"))

  val insertName1 = x => concatenator("Hello, I'm ", x, ",how are you?") // x: String => concatenator(hello, x, howareyou)
  println(insertName1("Superman"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x,y) => concatenator("Hello, ",x,y)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  // Exercises
  /*
      1. Process a list of numbers and return their string representations with different formats
          Use the %4.2f, %8.6f and %14.12f with a curried formatter function.

   */
  println("%4.2f".format(Math.PI))
  println("%8.6f".format(Math.PI))

  def curriedFormatter(s: String)(number: Double): String = s.format(number)

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(preciseFormat))

  println(numbers.map(curriedFormatter("%16.14f"))) // compiler does sweet eta-expansion for us

  /*
      2. Difference between
          - functions vs methods
          - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1

  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42

  def parenMethod(): Int = 42

  /*
      calling byName and byFunction
      - int
      - method
      - parenMethod
      - lambda
      - PAF
   */

  byName(23) // ok
  byName(method) // ok
  byName(parenMethod())
  byName(parenMethod) // ok but beware ==> byName(parentMethod()) . Scala3 will not work though
  //  byName(() => 42) // not ok
  byName((() => 42) ()) // ok
  //  byName(parenMethod _) // not ok

  //  byFunction(45) // not ok
  //  byFunction(method) // not ok!!! does not do ETA-explansion
  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works, but warning - unnecessary
}

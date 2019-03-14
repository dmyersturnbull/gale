package kokellab.gale

import java.nio.ByteBuffer

import breeze.stats.distributions.RandBasis

object Gale {

	def eval(s: String) = {}

	def main(args: Array[String]): Unit = {
		import scopt.OParser
		val builder = OParser.builder[Args]
		val parser1 = {
			import builder._
			OParser.sequence(
				programName("gale"),
				head("Gale command-line interpreter", "version 1.0"),
				arg[String]("expression")
					.action((x, c) => c.copy(expression = x))
					.required()
					.valueName("<string>")
					.text("The expression to evaluate"),
				opt[Int]("to")
					.action((x, c) => c.copy(end = x))
    				.required()
					.valueName("<long (default=100)>")
					.text("Last time t, in milliseconds"),
				opt[Int]("from")
					.action((x, c) => c.copy(start = x))
					.valueName("<long (default=0)>")
					.text("First time t, in milliseconds"),
				opt[Int]('s', "seed")
					.action((x, c) => c.copy(seed = x))
    				.valueName("<int (default=0)>")
					.text("Seed for random functions"),
				opt[Double]('d', "default")
					.action((x, c) => c.copy(defaultValue = x))
					.valueName("<double (default=−∞)>")
					.text("This value is assumed if an if condition is not met"),
				opt[Double]('o', "out-of-bounds")
					.action((x, c) => c.copy(outOfBoundsValue = x))
					.valueName("<double (default=NaN)>")
					.text("This value is used when accessing an out-of-bounds value"),
				opt[String]('x', "tolerance")
					.action((x, c) => c.copy(tolerance = x))
					.valueName("<string (default=\"1E-10\")>")
					.text("Tolerance for use in equivalence comparison operators"),
				opt[Int]('r', "round")
					.action((x, c) => c.copy(round = x))
					.valueName("<int>")
					.text("Round the output to some number of digits using half-even rounding."),
				opt[String]('f', "format")
					.action((x, c) => c.copy(format = x))
					.valueName("<'text'/'hex'/'raw'/'table' (default=text)>")
					.text("How to format the output to stdout"),
				version('v', "version"),
				help('h', "help")
			)
		}

		OParser.parse(parser1, args, Args()) match {
			case Some(a) =>
				val ans = calc(a)
				output(a, ans.toArray)
			case _ => // error message was already shown
		}
	}

	private def calc(a: Args): TraversableOnce[Double] = {
		val ans = TimeSeriesGrammar.build(a.expression, a.start, a.end, d=>d, Some(RandBasis.withSeed(a.seed)), a.defaultValue)
		if (a.round > -1) {
			ans map (d => BigDecimal(d).setScale(a.round, BigDecimal.RoundingMode.HALF_EVEN).doubleValue)
		} else ans
	}

	private def output(a: Args, ans: Array[Double]): Unit = {
		if (a.format == "text") {
			println(ans mkString " ")
		} else if (a.format == "hex") {
			val x: TraversableOnce[String] = doublesToBytes(ans) map("%02X" format _)
			print(x.mkString(""))
		} else if (a.format == "raw") {
			System.out.write(doublesToBytes(ans).toArray)
		} else if (a.format == "table") {
			val s: String = if (a.round == 0) ans map (_.toInt) mkString "\t" else ans mkString "\t"
			println("--"*s.length)
			println(ans.indices mkString "\t")
			println(s)
			println("--"*s.length)
		} else {
			System.err.println(s"Output format '${a.format}' not understood. Falling back to text.")
			println(ans mkString " ")
		}
	}

	private def doublesToBytes(values: TraversableOnce[Double]): TraversableOnce[Byte] =
		values flatMap (value => ByteBuffer.allocate(8).putDouble(value).array())

	case class Args(expression: String = "", start: Int = 0, end: Int = 100, seed: Int = 0, defaultValue: Double = Double.NegativeInfinity, outOfBoundsValue: Double = Double.NaN, round: Int = -1, tolerance: String = "1E10", format: String = "text")
}

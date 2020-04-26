import org.jetbrains.kotlin.incremental.dumpCollection

val myNumbers: List<Int> = listOf(2, 3, 4, 6, 23, 90)
myNumbers.dumpCollection()

val mySmallNumbers = myNumbers.filter { it < 10 }
mySmallNumbers.dumpCollection()


val mySquaredNumbers = mySmallNumbers.map { it * it }
mySquaredNumbers.dumpCollection()







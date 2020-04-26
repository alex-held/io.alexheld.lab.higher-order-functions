fun reverseAndDisplay(str: String, action: (String) ->String) {
    val result =  action(str)
    println(result)
}

reverseAndDisplay("ABCDEFGHIJK") {
    it.reversed()
}
val list = listOf(1, 2, 3, 4, 5)

fun<T> List<T>.doStuff(action: (List<T>) -> Unit) {
    action(this)
}

list.doStuff {
    println("The number is: $it")
    println(it.size)
    println(it.first())
    println(it.last())
}


/*
In this case we specify that [() -> Unit] is an extension function
on [List<T>] and the receiver of that is [List<T>].
 */
fun<T> List<T>.doStuffEnhanced(action: List<T>.() -> Unit) {
    action(this)

    // This means that we can use  [() -> Unit] as an
    // extension function on [List<T>]
    this.action()

    // And since we are the receiver we can also omit this
    action()
}


list.doStuffEnhanced {
    println("The number is: $this")
    println(size)
    println(first())
    println(last())
}







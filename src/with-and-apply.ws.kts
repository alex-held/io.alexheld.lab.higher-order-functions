class Person(var name: String, var age: Int) {
    fun execute() = println("Running...")
}

class Person(val name: String, val age: Int) {
    fun run()= ("Running...")
}

val person = Person("Alex", 23)

 with(person) {
     println(name)
     println(age)
 }

person.apply {
    name = "Peter"
    age = 11
}.execute()

println(person.name)
println(person.age)


val k = person
println("20")




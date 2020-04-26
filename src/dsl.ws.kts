import java.time.LocalDateTime

data class Talk(
    val topic: String,
    val speaker: String,
    val time: LocalDateTime,
    val type: TalkType = TalkType.CONFERENCE
)

enum class TalkType {
    CONFERENCE, KEYNOTE
}



open class Conference(private val name: String, private val location: String) {

    private val schedule = mutableListOf<Talk>()

    fun addTalk(talk: Talk) {
        schedule.add(talk)
    }

    open val talks: List<Talk>
        get() = schedule

    override fun toString(): String {
        return """
    $name \tConference \t@$location
    ${talks.joinToString("\n")}
    """
    }

}


/*
One way to build the domain object [Conference] would be the old school way.
 */
val conference = Conference("Devoxx UK 2019", "London")
val talk1 = Talk("Topic 1", "Speaker 1", LocalDateTime.parse("2018-05-07T12:00"))
val talk2 = Talk("Topic 2", "Speaker 2", LocalDateTime.parse("2018-05-07T15:00"))
conference.addTalk(talk1)
conference.addTalk(talk2)
conference.toString()


/*
We can use [.apply()] to make this more expressive
but we will go a step deeper the rabbi-hole
 */

conference {
    name = "Devoxx UK 2019"
    location = "London"

    talks {
        conferenceTalk("Kotlin 101", "Speaker 1", LocalDateTime.parse("2018-05-07T12:00"))
        keynoteTalk("Humar that works ", "Speaker 2", LocalDateTime.parse("2018-05-07T15:00"))

        conferenceTalk at "2018-05-08T12:00" by "Speaker 3" titled "The cloud"
        keynoteTalk at "2018-05-08T15:00" by "Speaker 4" titled "Infrastructure as a Service"


        +Talk("Security 101", "Speaker 5", LocalDateTime.parse("2018-05-09T12:00"))
    }

    talks.conferenceTalk("Why javascript sucks", "Speaker 6", LocalDateTime.parse("2018-05-08T15:00"))
}.toString()


/*
 Provides the entry point of the [conferenceDSL]
 */
inline fun conference(config: ConferenceDSL.() -> Unit): Conference {
    val dsl = ConferenceDSL().apply(config)

    return Conference(dsl.name, dsl.location).apply {
        dsl.talksList.forEach(this::addTalk)
    }
}


/*
Marks the DSL as a DSL
 */
annotation class ConferenceDslMarker


/*
The DSL to build the [Conference]
 */
@ConferenceDslMarker
class ConferenceDSL {


    private val _talksList = mutableListOf<Talk>()
    val talksList: List<Talk> get() = _talksList

    lateinit var name: String
    lateinit var location: String

    val talks = TalksConfigDsl()

    /*
        The DSL to build the [Talk]
    */
    @ConferenceDslMarker
    inner class TalksConfigDsl {

        private val _talksList = this@ConferenceDSL._talksList

        operator fun invoke(config: TalksConfigDsl.() -> Unit) = this.apply(config)


        fun conferenceTalk(topic: String, speaker: String, time: LocalDateTime) = _talksList.add(Talk(topic, speaker, time, TalkType.CONFERENCE))


        fun keynoteTalk(topic: String, speaker: String, time: LocalDateTime) = _talksList.add(Talk(topic, speaker, time, TalkType.KEYNOTE))


        val conferenceTalk get() = EmptyTalk(TalkType.CONFERENCE)
        val keynoteTalk get() = EmptyTalk(TalkType.KEYNOTE)


        inner class EmptyTalk(val talkType: TalkType) {
            infix fun at(timeString: String): TimedTalk = TimedTalk(this, LocalDateTime.parse(timeString))
        }

        inner class TimedTalk(val previous: EmptyTalk, val timeString: LocalDateTime) {
            infix fun by(speaker: String): TimedAndAuthoredTalk = TimedAndAuthoredTalk(this, speaker)
        }

        inner class TimedAndAuthoredTalk(private val timedTalk: TimedTalk, private val speaker: String) {
            infix fun titled(topic: String) = _talksList
                .add(Talk(topic,
                    speaker,
                    timedTalk.timeString,
                    timedTalk.previous.talkType
                )
                )
        }

        operator fun Talk.unaryPlus() = _talksList.add(this)
    }
}






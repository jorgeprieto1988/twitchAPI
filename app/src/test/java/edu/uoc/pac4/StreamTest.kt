package edu.uoc.pac4

import edu.uoc.pac4.data.streams.Stream
import kotlinx.serialization.SerialName
import org.junit.Test

class StreamTest {

    @Test
    internal fun checkGetSizedImage(){
        val stream = Stream(
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                ""
        )
        val result = stream.getSizedImage("loquesea{width}{height}", 1000, 200)
        assert(!result.contains("{width}") && !result.contains("{height}"))
        assert(result.contains("1000") && result.contains("200"))


    }
}
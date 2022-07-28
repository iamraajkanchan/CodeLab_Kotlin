package channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Channel - Channel Basics
 * */
class ChannelIntroduction {
    companion object {
        @JvmStatic
        fun main(array: Array<String>) = runBlocking {
            val channel = Channel<Int>()
            launch {
                for (i in 1..5) channel.send(i * i)
            }
            repeat(5) {
                println(channel.receive())
            }
            println("Done")
        }
    }
    /*
    * Output
    * 1
    * 4
    * 9
    * 16
    * 25
    * Done
    * */
}

/**
 * Channel - Closing and Iteration over channels
 * */
class ClosingAndIterationOverChannels {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val channel = Channel<Int>()
            launch {
                for (i in 1..5) channel.send(i * i)
                channel.close()
            }
            for (y in channel) println(y)
            println("Done")
        }
    }
    /*
    * 1
    * 4
    * 9
    * 16
    * 25
    * Done
    * */
}

/**
 * Channel - Build Channel Producers
 * */
class ChannelProducersIntroduction {
    companion object {
        @JvmStatic
        fun main(array: Array<String>) = runBlocking {
            val squares = produceSquares()
            squares.consumeEach { println(it) }
            println("Done")
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
            for (i in 1..5) send(i * i)
        }
    }
    /*
    * Output
    * 1
    * 4
    * 9
    * 16
    * 25
    * Done
    * */
}
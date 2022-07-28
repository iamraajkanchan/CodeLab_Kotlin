package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

/**
 * Channels - Channel Basics
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
 * Channels - Closing and Iteration over channels
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
 * Channels - Build Channel Producers
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

/**
 * Channels - Pipelines
 * */
class PipelinesIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val numbers = produceNumbers()
            val squares = squares(numbers)
            repeat(10) {
                print("${squares.receive()} - ")
            }
            println("Done")
            coroutineContext.cancelChildren()
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun CoroutineScope.produceNumbers() = produce<Int> {
            var x = 1
            while (true) send(x++)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun CoroutineScope.squares(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
            for (x in numbers) send(x * x)
        }
    }
    /*
    * Output
    * 1 - 4 - 9 - 16 - 25 - 36 - 49 - 64 - 81 - 100 - Done
    * */
}
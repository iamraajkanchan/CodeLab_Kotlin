package channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

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

/**
 * Channels - Prime numbers with Pipeline
 * */
class PipeLineForPrimeNumbers {
    companion object {
        @JvmStatic
        fun main(array: Array<String>) = runBlocking {
            val numbers = produceFrom(2)
            val primeNumbers = filter(numbers)
            repeat(5) {
                println(primeNumbers.receive())
            }
            println("Done")
            coroutineContext.cancelChildren()
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun CoroutineScope.produceFrom(start: Int) = produce<Int> {
            var x = start
            while (true) send(x++)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
            for (x in numbers) if (x % 2 != 0) send(x)
        }
    }
    /*
    * Output
    * 3
    * 5
    * 7
    * 9
    * 11
    * Done
    * */
}

/**
 * Channels - Pipeline with Iterator
 * */
class PipeLineWithIteratorIntroduction {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val numbers = produceNumbers()
            repeat(5) {
                println(numbers.next())
            }
            println("Done")
        }

        private fun produceNumbers(): Iterator<Int> = iterator {
            var x = 1
            while (true) yield(x++)
        }
    }
}

/**
 * Channels - Fan Out
 * */
class FanOutExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val numbers = produceNumbers()
            repeat(5) { i ->
                launchProcessor(i, numbers)
            }
            delay(1000L)
            numbers.cancel()
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun CoroutineScope.produceNumbers(): ReceiveChannel<Int> = produce {
            var x = 1
            while (true) {
                send(x++)
                delay(100L)
            }
        }

        private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
            for (msg in channel) {
                println("Processor $id received $msg")
            }
        }
    }
    /*
    * Output
    * Processor 0 received 1
    * Processor 0 received 2
    * Processor 1 received 3
    * Processor 2 received 4
    * Processor 3 received 5
    * Processor 4 received 6
    * Processor 0 received 7
    * Processor 1 received 8
    * Processor 2 received 9
    * */
}

/**
 * Channels - Fan In
 * */
class FanInExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val channel = Channel<String>()
            launch { sendString(channel, "Chinki", 500) }
            launch { sendString(channel, "Minki", 600) }
            repeat(6) {
                println(channel.receive())
            }
            coroutineContext.cancelChildren()
        }

        private suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
            while (true) {
                delay(time)
                channel.send(s)
            }
        }
    }
    /*
    * Chinki
    * Minki
    * Chinki
    * Minki
    * Chinki
    * Minki
    * */
}

/**
 * Channels - Buffered Channels
 * */
class BufferedChannelExample {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val channel = Channel<Int>(4) // Setting buffer for this channel
            val sender = launch {
                repeat(10) {
                    println("Sending $it")
                    channel.send(it)
                }
            }
            delay(1000)
            sender.cancelAndJoin()
        }
    }
    /*
    * Output
    * Sending 0
    * Sending 1
    * Sending 2
    * Sending 3
    * Sending 4
    * */
}

/**
 * Channels - Channels are fair
 * */
data class Ball(var hits: Int)
class ChannelFairProperty {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val table = Channel<Ball>()
            launch { player("ping", table) }
            launch { player("pong", table) }
            table.send(Ball(0))
            delay(1000)
            coroutineContext.cancelChildren()
        }

        private suspend fun player(name: String, table: Channel<Ball>) {
            for (ball in table) {
                ball.hits++
                println("$name $ball")
                delay(150L)
                table.send(ball)
            }
        }
    }
    /*
    * Output
    * ping Ball(hits=1)
    * pong Ball(hits=2)
    * ping Ball(hits=3)
    * pong Ball(hits=4)
    * ping Ball(hits=5)
    * pong Ball(hits=6)
    * */
}

/**
 * Channels - Ticker Channel
 * */
class TickerChannelExample {
    companion object {
        @OptIn(ObsoleteCoroutinesApi::class)
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val tickerChannel = ticker(100, 0)

            var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
            println("Initial element is available immediately : $nextElement")

            nextElement = withTimeoutOrNull(50) { tickerChannel.receive() }
            println("Next element is not ready in 50ms : $nextElement")

            nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
            println("Next element is ready in 100ms: $nextElement")

            println("Consumer pauses for 150ms")
            delay(150)

            nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
            println("Next element is available immediately after large consumer delay : $nextElement")

            nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
            println("Next element is ready in 50ms after consume pause in 150ms: $nextElement")

            tickerChannel.cancel()
        }
    }
    /*
    * Output
    * Initial element is available immediately : kotlin.Unit
    * Next element is not ready in 50ms : null
    * Next element is ready in 100ms: kotlin.Unit
    * Consumer pauses for 150ms
    * Next element is available immediately after large consumer delay : kotlin.Unit
    * Next element is ready in 50ms after consume pause in 150ms: kotlin.Unit
    * */
}
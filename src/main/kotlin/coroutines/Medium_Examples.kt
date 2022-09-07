package coroutines

import kotlinx.coroutines.*

/**
 * Awaiting Multiple Coroutines
 * Reference https://proandroiddev.com/awaiting-multiple-coroutines-the-clean-way-75469f8df160
 * */
class AwaitingMultipleCoroutines {

    private suspend fun downloadBooks() {
        println("Downloading Books...")
        delay(1000L)
    }

    private suspend fun downloadMovies() {
        println("Downloading Movies...")
        delay(2000L)
    }

    private suspend fun downloadSongs() {
        println("Downloading Songs...")
        delay(1500L)
    }

    private suspend fun download() = coroutineScope {
        /*
        val downloadJobs = listOf(
            launch { downloadBooks() },
            launch { downloadSongs() },
            launch { downloadMovies() }
        )
        downloadJobs.joinAll()
        */

        awaitAll(
            ::downloadBooks,
            ::downloadMovies,
            ::downloadSongs
        )
    }

    private suspend fun awaitAll(vararg blocks: suspend () -> Unit) = coroutineScope {
        blocks.forEach {
            launch(Dispatchers.IO) {
                println("Thread Name: ${Thread.currentThread().name}")
                it.invoke()
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val example = AwaitingMultipleCoroutines()
            runBlocking {
                launch {
                    example.download()
                }
            }
        }
    }
    /*
    * Output
    * Thread Name: DefaultDispatcher-worker-2
    * Thread Name: DefaultDispatcher-worker-1
    * Downloading Movies...
    * Downloading Books...
    * Thread Name: DefaultDispatcher-worker-4
    * Downloading Songs...
    * */
}
package com.example.osgi.work

import co.paralleluniverse.fibers.CustomFiberWriter
import co.paralleluniverse.fibers.Fiber
import co.paralleluniverse.fibers.Suspendable
import co.paralleluniverse.strands.SuspendableCallable
import com.example.osgi.api.Greetings
import java.util.concurrent.CompletableFuture
import java.util.function.Function

class Sleeper(
    private val greetings: Greetings,

    @Transient
    private val checkpoint: CompletableFuture<ByteArray>,

    @Transient
    private val factory: Function<CompletableFuture<ByteArray>, CustomFiberWriter>
) : SuspendableCallable<String> {
    @Suspendable
    override fun run(): String {
        Fiber.parkAndCustomSerialize(factory.apply(checkpoint))

        val worker = Fiber.currentFiber()
        return greetings.greet(worker.name)
    }
}

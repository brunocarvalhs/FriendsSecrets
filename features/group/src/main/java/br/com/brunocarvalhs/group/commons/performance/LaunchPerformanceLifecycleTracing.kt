package br.com.brunocarvalhs.group.commons.performance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.firebase.perf.FirebasePerformance

@Composable
fun LaunchPerformanceLifecycleTracing(route: String) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, key2 = route) {
        val observer = LifecycleEventObserver { _, event ->
            val traceName = "${route}_${event.name.lowercase()}"
            val trace = FirebasePerformance.getInstance().newTrace(traceName)
            trace.start()
            trace.stop()
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

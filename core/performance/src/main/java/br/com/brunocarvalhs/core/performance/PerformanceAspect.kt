package br.com.brunocarvalhs.core.performance

import android.os.Looper
import android.os.SystemClock
import br.com.brunocarvalhs.core.performance.annotation.Performance
import com.google.firebase.perf.FirebasePerformance
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import timber.log.Timber

@Aspect
class PerformanceAspect {

    @Around("@annotation(performanceAnnotation)")
    fun interceptPerformance(
        joinPoint: ProceedingJoinPoint,
        performanceAnnotation: Performance
    ): Any? {
        val traceName = performanceAnnotation.name
        val trace = FirebasePerformance.getInstance().newTrace(traceName)

        val startTime = SystemClock.elapsedRealtime()
        val isMainThread = Looper.myLooper() == Looper.getMainLooper()

        trace.start()

        trace.putAttribute(
            THREAD_ATTRIBUTE,
            if (isMainThread) MAIN_THREAD_TAG else BACKGROUND_THREAD_TAG
        )
        trace.putAttribute(CLASS_ATTRIBUTE, joinPoint.signature.declaringType.simpleName)

        return try {
            joinPoint.proceed()
        } finally {
            trace.stop()

            val duration = SystemClock.elapsedRealtime() - startTime

            if (isMainThread && duration > JANK_THRESHOLD_MS) {
                Timber.w("⚠️ JANK DETECTADO: Método [${joinPoint.signature.name}] levou ${duration}ms na Main Thread!")
            }

            val usedMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                .freeMemory()) / CONVERT_MEMORY / CONVERT_MEMORY
            trace.putMetric(MEM_USAGE_ATTRIBUTE, usedMem)
        }
    }

    companion object {
        private const val JANK_THRESHOLD_MS = 16
        private const val MAIN_THREAD_TAG = "Main Thread"
        private const val BACKGROUND_THREAD_TAG = "Background Thread"
        private const val CLASS_ATTRIBUTE = "class"
        private const val THREAD_ATTRIBUTE = "thread"
        private const val MEM_USAGE_ATTRIBUTE = "mem_usage_mb"
        private const val CONVERT_MEMORY = 1024
    }
}

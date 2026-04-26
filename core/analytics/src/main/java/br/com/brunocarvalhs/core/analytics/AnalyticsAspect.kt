package br.com.brunocarvalhs.core.analytics

import br.com.brunocarvalhs.core.analytics.annotation.Analytics
import br.com.brunocarvalhs.core.analytics.extensions.ConvertParameters
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import timber.log.Timber

@Aspect
class AnalyticsAspect {

    @Before("@annotation(analyticsAnnotation)")
    fun interceptAnalytics(joinPoint: JoinPoint, analyticsAnnotation: Analytics) {
        try {
            val eventName: String = analyticsAnnotation.event.name
            val params = ConvertParameters.toBundle(
                params = analyticsAnnotation.params.associate { it.key.key to it.value }
            )
            Timber.d("Log Analytics: Evento $eventName com params $params")
            Firebase.analytics.logEvent(
                eventName,
                params
            )
        } catch (e: Exception) {
            Timber.e(e, "Erro ao disparar evento para o Firebase")
        }
    }
}

package br.com.brunocarvalhs.friendssecrets.core.analytics

import br.com.brunocarvalhs.friendssecrets.core.analytics.annotation.Analytics
import br.com.brunocarvalhs.friendssecrets.core.analytics.annotation.Param
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
        val eventName: String = analyticsAnnotation.event
        val params: Array<Param> = analyticsAnnotation.params
        Timber.d("Log Analytics: Evento $eventName com params $params")
        try {
            AnalyticsManager.logEvent(eventName, params)
        } catch (e: Exception) {
            Timber.e(e, "Erro ao disparar evento para o Firebase")
        }
    }
}
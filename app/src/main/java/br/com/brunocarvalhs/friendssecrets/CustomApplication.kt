package br.com.brunocarvalhs.friendssecrets

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class with Hilt dependency injection setup.
 *
 * The @HiltAndroidApp annotation triggers Hilt's code generation and
 * creates the application-level component that provides dependencies
 * to all screens and components in the application.
 *
 * This class initializes the application and sets up strict mode for debugging
 * to catch potential performance issues during development.
 */
@HiltAndroidApp
class CustomApplication : Application()

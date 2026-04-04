package br.com.brunocarvalhs.group.create

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.group.create.app.presentation.contacts.ContactsScreen
import br.com.brunocarvalhs.group.create.app.presentation.contacts.ContactsViewModel
import br.com.brunocarvalhs.group.create.app.presentation.forms.FormsScreen
import br.com.brunocarvalhs.group.create.app.presentation.forms.FormsViewModel
import br.com.brunocarvalhs.group.create.commons.navigation.ContactsRouter
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter
import br.com.brunocarvalhs.group.create.commons.providers.GroupCreateToggles
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupCreateInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupCreateInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupCreateRouter>(startDestination = ContactsRouter) {
            composable<FormsRouter>(typeMap = FormsRouter.typeMap) {
                val viewModel = hiltViewModel<FormsViewModel>()
                FormsScreen(
                    navController = builder.navController,
                    viewModel = viewModel,
                )
            }

            composable<ContactsRouter> {
                val viewModel = hiltViewModel<ContactsViewModel>()
                ContactsScreen(
                    navController = builder.navController,
                    viewModel = viewModel,
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var toggle: GroupCreateToggles? = null

        @AddTrace(name = "GroupCreateInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "GroupCreateInitializer.Builder.toggleManager", enabled = true)
        fun toggle(toggleManager: GroupCreateToggles) = apply {
            this.toggle = toggleManager
        }

        @AddTrace(name = "GroupCreateInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupCreateInitializer =
            GroupCreateInitializer(this).also { it.build(navGraphBuilder) }
    }
}
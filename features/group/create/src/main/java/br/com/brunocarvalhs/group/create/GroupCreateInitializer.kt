package br.com.brunocarvalhs.group.create

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.group.create.app.presentation.contacts.ContactsScreen
import br.com.brunocarvalhs.group.create.app.presentation.contacts.ContactsViewModel
import br.com.brunocarvalhs.group.create.app.presentation.editForm.EditFormsScreen
import br.com.brunocarvalhs.group.create.app.presentation.editForm.EditFormsViewModel
import br.com.brunocarvalhs.group.create.app.presentation.forms.FormsScreen
import br.com.brunocarvalhs.group.create.app.presentation.forms.FormsViewModel
import br.com.brunocarvalhs.group.create.commons.navigation.ContactsRouter
import br.com.brunocarvalhs.group.create.commons.navigation.EditFormsRouter
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import br.com.brunocarvalhs.group.create.commons.navigation.GroupCreateRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupCreateInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupCreateInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupCreateRouter>(startDestination = ContactsRouter) {
            composable<FormsRouter>(typeMap = FormsRouter.typeMap) {
                val viewModel = hiltViewModel<FormsViewModel>()
                FormsScreen(
                    viewModel = viewModel,
                    onFinish = { builder.onFinish?.invoke(it) },
                    onBack = { builder.navController.popBackStack() }
                )
            }

            composable<EditFormsRouter>(typeMap = EditFormsRouter.typeMap) {
                val viewModel = hiltViewModel<EditFormsViewModel>()
                EditFormsScreen(
                    viewModel = viewModel,
                    onBack = { builder.navController.popBackStack() }
                )
            }

            composable<ContactsRouter> {
                val viewModel = hiltViewModel<ContactsViewModel>()
                ContactsScreen(
                    viewModel = viewModel,
                    onBack = { builder.navController.popBackStack() },
                    onNext = { builder.navController.navigate(it) }
                )
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onFinish: ((String) -> Unit)? = null

        @AddTrace(name = "GroupCreateInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply {
            this.navController = navController
        }

        @AddTrace(name = "GroupCreateInitializer.Builder.onFinish", enabled = true)
        fun onFinish(onFinish: (String) -> Unit) = apply {
            this.onFinish = onFinish
        }

        @AddTrace(name = "GroupCreateInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): GroupCreateInitializer =
            GroupCreateInitializer(this).also { it.build(navGraphBuilder) }
    }
}

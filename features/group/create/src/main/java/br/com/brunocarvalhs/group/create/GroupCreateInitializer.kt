package br.com.brunocarvalhs.group.create

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.ContactsRouter
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.EditFormsGraph
import br.com.brunocarvalhs.friendssecrets.core.navigation.routers.GroupCreateGraph
import br.com.brunocarvalhs.group.create.app.presentation.contacts.ContactsScreen
import br.com.brunocarvalhs.group.create.app.presentation.contacts.ContactsViewModel
import br.com.brunocarvalhs.group.create.app.presentation.editForm.EditFormsScreen
import br.com.brunocarvalhs.group.create.app.presentation.editForm.EditFormsViewModel
import br.com.brunocarvalhs.group.create.app.presentation.forms.FormsScreen
import br.com.brunocarvalhs.group.create.app.presentation.forms.FormsViewModel
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import com.google.firebase.perf.metrics.AddTrace
import kotlin.properties.Delegates

class GroupCreateInitializer(private val builder: Builder) {

    @AddTrace(name = "GroupCreateInitializer.build", enabled = true)
    fun build(navGraphBuilder: NavGraphBuilder) {
        return navGraphBuilder.navigation<GroupCreateGraph>(startDestination = ContactsRouter()) {
            composable<FormsRouter>(typeMap = FormsRouter.typeMap) {
                val viewModel = hiltViewModel<FormsViewModel>()
                FormsScreen(
                    viewModel = viewModel,
                    onFinish = { builder.onFinish?.invoke(it) },
                    onBack = { builder.navController.popBackStack() }
                )
            }

            composable<EditFormsGraph>(typeMap = EditFormsGraph.typeMap) {
                val viewModel = hiltViewModel<EditFormsViewModel>()
                EditFormsScreen(
                    viewModel = viewModel,
                    onBack = { builder.navController.popBackStack() }
                )
            }

            composable<ContactsRouter>(typeMap = ContactsRouter.typeMap) {
                val viewModel = hiltViewModel<ContactsViewModel>()
                ContactsScreen(
                    viewModel = viewModel,
                    onBack = { builder.navController.popBackStack() },
                    onNext = { route ->
                        builder.navController.navigate(route) {
                            if (route is EditFormsGraph) {
                                popUpTo<ContactsRouter> {
                                    inclusive = true
                                }
                            }
                        }
                    },
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

package br.com.brunocarvalhs.group.create

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.brunocarvalhs.friendssecrets.core.navigation.CommonNavigator
import br.com.brunocarvalhs.friendssecrets.core.navigation.FeatureInitializer
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
import javax.inject.Inject

class GroupCreateInitializerImpl @Inject constructor(
    private val navigator: CommonNavigator
) : FeatureInitializer {

    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.navigation<GroupCreateRouter>(startDestination = ContactsRouter) {
            composable<FormsRouter>(typeMap = FormsRouter.typeMap) {
                val viewModel = hiltViewModel<FormsViewModel>()
                FormsScreen(
                    viewModel = viewModel,
                    onFinish = {
                        navigator.navigateToGroupList(
                            navController = navController,
                            popUpTo = GroupCreateRouter::class,
                            inclusive = true
                        )
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable<EditFormsRouter>(typeMap = EditFormsRouter.typeMap) {
                val viewModel = hiltViewModel<EditFormsViewModel>()
                EditFormsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable<ContactsRouter> {
                val viewModel = hiltViewModel<ContactsViewModel>()
                ContactsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNext = { navController.navigate(it) }
                )
            }
        }
    }
}
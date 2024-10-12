package br.com.brunocarvalhs.friendssecrets.presentation.views.home

sealed interface HomeIntent {
    data object FetchGroups : HomeIntent
    data object ReadFriendSecret: HomeIntent
}
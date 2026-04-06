package br.com.brunocarvalhs.group.create.app.domain.services

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import com.google.firebase.perf.metrics.AddTrace

interface ContactService {
    @AddTrace(name = "ContactServiceImpl.getContacts", enabled = true)
    fun getContacts(): List<ContactModel>
}
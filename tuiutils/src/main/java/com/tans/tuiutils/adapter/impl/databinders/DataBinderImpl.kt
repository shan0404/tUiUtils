package com.tans.tuiutils.adapter.impl.databinders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tans.tuiutils.adapter.AdapterBuilder
import com.tans.tuiutils.adapter.DataBinder

open class DataBinderImpl<Data : Any>(
    private val bindDataNoPayload: (data: Data, view: View, positionInDataSource: Int) -> Unit
) : DataBinder<Data> {

    override val payloadDataBinders: MutableMap<Any,  ((data: Data, view: View, positionInDataSource: Int) -> Unit)?> = mutableMapOf()

    override fun bindData(data: Data, view: View, positionInDataSource: Int) {
        bindDataNoPayload(data, view, positionInDataSource)
        for ((_, payloadBinder) in payloadDataBinders) {
            if (payloadBinder != null) {
                payloadBinder(data, view, positionInDataSource)
            }
        }
    }

    override fun bindPayloadData(data: Data, view: View, positionInDataSource: Int, payloads: List<Any>) {
        for (payload in payloads) {
            val binder = payloadDataBinders[payload]
            if (binder != null) {
                binder(data, view, positionInDataSource)
            } else {
                error("No binder for $payload payload.")
            }
        }
    }

    override var attachedBuilder: AdapterBuilder<Data>? = null
    override var attachedRecyclerView: RecyclerView? = null

}
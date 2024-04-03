package com.tans.tuiutils.adapter

import androidx.annotation.MainThread

interface DataSource<Data : Any> : AdapterBuilderLife<Data> {

    var lastSubmitDataList: List<Data>

    @MainThread
    fun submitDataList(data: List<Data>, callback: Runnable? = null) {
        val builder = attachedBuilder
        if (builder == null) {
            error("Attached builder is null.")
        } else {
            builder.requestSubmitDataList(this, data) {
                lastSubmitDataList = data
                callback?.run()
            }
        }
    }

    fun areDataItemsTheSame(d1: Data, d2: Data): Boolean

    fun areDataItemsContentTheSame(d1: Data, d2: Data): Boolean

    fun getDataItemsChangePayload(d1: Data, d2: Data): Any?

    fun getData(positionInDataSource: Int): Data?

    fun getDataSourceSize(): Int = lastSubmitDataList.size

    fun tryGetDataClass(): Class<Data>? = lastSubmitDataList.getOrNull(0)?.javaClass
}
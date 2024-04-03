package com.tans.tuiutils.demo.mediastore

import android.os.Bundle
import android.view.View
import com.tans.tuiutils.adapter.impl.builders.SimpleAdapterBuilder
import com.tans.tuiutils.adapter.impl.databinders.DataBinderImpl
import com.tans.tuiutils.adapter.impl.datasources.DataSourceImpl
import com.tans.tuiutils.adapter.impl.viewcreatators.SingleItemViewCreator
import com.tans.tuiutils.demo.R
import com.tans.tuiutils.demo.databinding.AudioItemLayoutBinding
import com.tans.tuiutils.demo.databinding.FragmentAudiosBinding
import com.tans.tuiutils.fragment.BaseCoroutineStateFragment
import com.tans.tuiutils.mediastore.MediaStoreAudio
import com.tans.tuiutils.mediastore.queryAudioFromMediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AudiosFragment : BaseCoroutineStateFragment<AudiosFragment.Companion.State>(State()) {

    override val layoutId: Int = R.layout.fragment_audios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("${this@AudiosFragment::class.java.simpleName} onCreate(): $savedInstanceState")
    }

    override fun CoroutineScope.firstLaunchInitDataCoroutine() {
        println("${this@AudiosFragment::class.java.simpleName} firstLaunchInitDataCoroutine()")
        launch {
            val audios = this@AudiosFragment.queryAudioFromMediaStore()
            updateState { it.copy(audios = audios) }
        }
    }

    override fun CoroutineScope.bindContentViewCoroutine(contentView: View) {
        println("${this@AudiosFragment::class.java.simpleName} bindContentViewCoroutine()")
        val viewBinding = FragmentAudiosBinding.bind(contentView)
        val dataSource = DataSourceImpl<MediaStoreAudio>()
        val adapter = SimpleAdapterBuilder<MediaStoreAudio>(
            itemViewCreator = SingleItemViewCreator(R.layout.audio_item_layout),
            dataBinder = DataBinderImpl { data, view, _ ->
                val itemViewBinding = AudioItemLayoutBinding.bind(view)
                itemViewBinding.musicTitleTv.text = "${data.title}-${data.artist}"
            },
            dataSource = dataSource
        ).build()
        viewBinding.audiosRv.adapter = adapter
        renderStateNewCoroutine({ it.audios }) {
            dataSource.submitDataList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        println("${this@AudiosFragment::class.java.simpleName} onResume()")
    }

    override fun onPause() {
        super.onPause()
        println("${this@AudiosFragment::class.java.simpleName} onPause()")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("${this@AudiosFragment::class.java.simpleName} onDestroy()")
    }

    companion object {
        data class State(val audios: List<MediaStoreAudio> = emptyList())
    }
}
package com.hidayah.iptv.player.view

import android.annotation.SuppressLint
import android.text.Spanned
import android.text.SpannedString
import android.util.Log
import com.egeniq.androidtvprogramguide.ProgramGuideFragment
import com.egeniq.androidtvprogramguide.entity.ProgramGuideChannel
import com.egeniq.androidtvprogramguide.entity.ProgramGuideSchedule
import com.hidayah.iptv.player.model.epg.ChannelsItem
import com.hidayah.iptv.player.model.epg.DataItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class EpgFragment() : ProgramGuideFragment<DataItem>() {
    interface OnScheduleChangeListener {
        fun onScheduleClicked(dataItem: DataItem?)
        fun onScheduleSelected(dataItem: DataItem?)
    }

    companion object {
        private val TAG = EpgFragment::class.java.name
    }

    data class SimpleChannel(
            override val id: String,
            override val name: Spanned?,
            override val imageUrl: String?,
            override val channel_number: String?) : ProgramGuideChannel

    private lateinit var dataItemList: ArrayList<ChannelsItem>
    private lateinit var onScheduleChangeListener: OnScheduleChangeListener
    private lateinit var simpleChannelList: ArrayList<EpgFragment.SimpleChannel>

    //     fun setChannelData(dataItemList: ArrayList<ChannelsItem>, onScheduleChangeListener: OnScheduleChangeListener){
//        this.dataItemList = dataItemList
//        this.onScheduleChangeListener = onScheduleChangeListener
//    }
    fun setChannelData(simpleChannelList: ArrayList<EpgFragment.SimpleChannel>, dataItemList: ArrayList<ChannelsItem>, onScheduleChangeListener: OnScheduleChangeListener) {
        this.dataItemList = dataItemList
        this.onScheduleChangeListener = onScheduleChangeListener
        this.simpleChannelList = simpleChannelList
    }

    override fun onScheduleClicked(programGuideSchedule: ProgramGuideSchedule<DataItem>) {
        val innerSchedule = programGuideSchedule.program
        if (innerSchedule == null) {
            // If this happens, then our data source gives partial info
            Log.w(TAG, "Unable to open schedule: $innerSchedule")
            return
        }

        onScheduleChangeListener.onScheduleClicked(innerSchedule)
        /*val todayDate = ZonedDateTime.now()
        val programStartTime= ZonedDateTime.parse(innerSchedule.startTime)
        val date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(programStartTime)
        val time = DateTimeFormatter.ofPattern("hh:mm").format(programStartTime)

        if (programGuideSchedule.isCurrentProgram) {
            Toast.makeText(context, "Open live player", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "${innerSchedule.title} is coming up at $time", Toast.LENGTH_LONG).show()
        }*/
        // Example of how a program can be updated. You could also change the underlying program.
        updateProgram(programGuideSchedule.copy(displayTitle = programGuideSchedule.displayTitle + " [clicked]"))
    }

    override fun onScheduleSelected(programGuideSchedule: ProgramGuideSchedule<DataItem>?) {
        if (programGuideSchedule == null) {
            //Log.w(TAG, "Unable to open schedule: $innerSchedule")
            return
        }
        val innerSchedule = programGuideSchedule.program
        onScheduleChangeListener.onScheduleSelected(innerSchedule)
    }

    override fun isTopMenuVisible(): Boolean {
        return false
    }

    @SuppressLint("CheckResult")
    override fun requestingProgramGuideFor(localDate: LocalDate) {
        setState(State.Loading)
        Single.fromCallable {
//            val channels: ArrayList<SimpleChannel> = arrayListOf()
//            for (item in dataItemList) {
//                channels.add(SimpleChannel("" + item.channelId, SpannedString(item.name), item.logoUrl, "" + item.channelId))
//            }

            val channelMap = mutableMapOf<String, List<ProgramGuideSchedule<DataItem>>>()
            var count = 0
            dataItemList.forEach { channel ->
                Log.d("---EPG", "channel")
                val scheduleList = mutableListOf<ProgramGuideSchedule<DataItem>>()
                channel.data.forEach { schedule ->
                    Log.d("---EPG", "data")
                    val startTime = ZonedDateTime.parse(schedule.startTime)
                    val endTime = ZonedDateTime.parse(schedule.endTime)
                    //TODO set ID here
                    val id = Random.nextLong(100L)
                    schedule.parenChannelId = channel.channelId
                    scheduleList.add(createSchedule(id, schedule, startTime, endTime))
                }
                // scheduleList.length = 10
                channelMap["" + channel.channelId] = scheduleList
            }
            return@fromCallable Pair(simpleChannelList, channelMap)
        }.delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setData(it.first, it.second, localDate)
                    if (it.first.isEmpty() || it.second.isEmpty()) {
                        setState(State.Error("No channels loaded."))
                    } else {
                        setState(State.Content)
                    }
                }, {
                    Log.e(TAG, "Unable to load example data!", it)
                })
    }

    private fun createSchedule(id: Long, schedule: DataItem, startTime: ZonedDateTime, endTime: ZonedDateTime): ProgramGuideSchedule<DataItem> {
        val metadata = DateTimeFormatter.ofPattern("'Starts at' HH:mm").format(startTime)
        schedule.metadata = metadata

        return ProgramGuideSchedule.createScheduleWithProgram(
                id,
                startTime.toInstant(),
                endTime.toInstant(),
                true,
                schedule.title,
                schedule
        )
    }

    private fun randomTimeBetween(min: ZonedDateTime, max: ZonedDateTime): ZonedDateTime {
        val randomEpoch = Random.nextLong(min.toEpochSecond(), max.toEpochSecond())
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(randomEpoch), ZoneOffset.UTC)
    }


    override fun requestRefresh() {
        // You can refresh other data here as well.
        requestingProgramGuideFor(currentDate)
    }


}

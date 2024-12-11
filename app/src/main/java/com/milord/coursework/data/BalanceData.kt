package com.milord.coursework.data

import com.milord.coursework.utils.api.ReadingType
import com.milord.coursework.utils.api.StoreReadingsRequest
import java.util.ArrayList

data class BalanceData(var balance : Double = 0.0, var electricity : Double = 0.0, var coldWater : Double = 0.0, var hotWater : Double = 0.0, var houseHeating : Double = 0.0, var maintenance : Double = 0.0, var cap : Double = 0.0)
{
    fun getReadings(): ArrayList<StoreReadingsRequest>
    {
        val readings = ArrayList<StoreReadingsRequest>()
        readings.add(StoreReadingsRequest(ReadingType.ELECTRICITY.ordinal, electricity))
        readings.add(StoreReadingsRequest(ReadingType.COLD_WATER.ordinal, coldWater))
        readings.add(StoreReadingsRequest(ReadingType.HOT_WATER.ordinal, hotWater))
        readings.add(StoreReadingsRequest(ReadingType.HOUSE_HEATING.ordinal, houseHeating))
        readings.add(StoreReadingsRequest(ReadingType.MAINTENANCE.ordinal, maintenance))
        return readings
    }
}

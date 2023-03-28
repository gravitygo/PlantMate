package com.plantmate.plantmate.DAO

import com.github.mikephil.charting.data.Entry
import com.plantmate.plantmate.objects.Plant
import kotlin.random.Random

class DataHelper {
    companion object{
        fun generateData():HashMap<String, ArrayList<Plant>>{
            var data : HashMap<String, ArrayList<Plant>> = HashMap<String, ArrayList<Plant>>()
            data["Cactaceae"] = generateArrayList()
            data["Asphodelaceae"] = generateArrayList()
            data["Araceae"] = generateArrayList()
            data["Rutaceae"] = generateArrayList()
            return data
        }
        fun dataGenerate(size:Int): List<Entry> {
            val entries = mutableListOf<Entry>()
            for(i in 0 until size)
                entries.add(Entry(i.toFloat(), Random.nextInt(100, 5001).toFloat()))

            return entries
        }
        fun generateArrayList():ArrayList<Plant>{
            var data: ArrayList<Plant> = ArrayList<Plant>()
            data.add(Plant(
                1,
                "Hi",
                "Cultivar1",
                "Scientific1",
                "asdf",
                1))
            data.add(Plant(
                2,
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2))
            data.add(Plant(
                1,
                "Hi",
                "Cultivar1",
                "Scientific1",
                "asdf",
                1))
            data.add(
                Plant(
                2,
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2)
            )
            data.add(Plant(
                2,
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2))
            data.add(Plant(
                2,
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2))
            data.add(Plant(
                2,
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2))
            data.add(Plant(
                2,
                "Hi2",
                "Cultivar2",
                "Scientific2",
                "asdf",
                2))
            return data
        }
    }
}
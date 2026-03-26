package com.example.myapp.data

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class KnmiRepository {

    private val api = Retrofit.Builder()
        .baseUrl("https://rdsa.knmi.nl/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(KnmiApi::class.java)

    suspend fun getQuakes(): List<EarthquakeNL> {
        return runCatching {
            parseRss(api.getEarthquakesRss())
        }.getOrElse { emptyList() }
    }

    private fun parseRss(rss: String): List<EarthquakeNL> {

        val itemRegex = Regex("<item>([\\s\\S]*?)</item>", RegexOption.IGNORE_CASE)

        return itemRegex.findAll(rss).map { itemMatch ->
            val item = itemMatch.groupValues[1]
            val title = extractTag(item, "title")

            EarthquakeNL(
                title = title,
                eventDate = extractDateFromTitle(title),
                description = extractTag(item, "description"),
                latitude = extractTag(item, "geo:lat"),
                longitude = extractTag(item, "geo:lon"),
                link = extractTag(item, "link"),
                id = extractTag(item, "guid")
            )
        }.toList()
    }

    private fun extractDateFromTitle(title: String): String {
        return title.split(",").firstOrNull()?.trim().orEmpty().ifBlank { "Onbekend" }
    }

    private fun extractTag(xmlFragment: String, tag: String): String {
        val regex = Regex("<$tag>([\\s\\S]*?)</$tag>", RegexOption.IGNORE_CASE)
        val value = regex.find(xmlFragment)?.groupValues?.get(1)?.trim().orEmpty()

        return value
            .replace("<![CDATA[", "")
            .replace("]]>", "")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .ifBlank { "Onbekend" }
    }
}

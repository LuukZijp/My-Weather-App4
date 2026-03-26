package com.example.myapp.data

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class UsgsRepository {

    private val api = Retrofit.Builder()
        .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(UsgsApi::class.java)

    suspend fun getGlobalQuakes(): List<EarthquakeGlobal> {
        val atom = api.getSignificantMonth()
        val entryRegex = Regex("<entry\\b[^>]*>([\\s\\S]*?)</entry>", RegexOption.IGNORE_CASE)

        return entryRegex.findAll(atom).map { entryMatch ->
            val entry = entryMatch.groupValues[1]
            val title = extractTag(entry, "title")
            val summaryHtml = extractTag(entry, "summary")

            EarthquakeGlobal(
                title = title,
                updated = extractTag(entry, "updated"),
                summary = stripHtml(summaryHtml),
                point = extractTag(entry, "georss:point"),
                link = extractAlternateLink(entry),
                magnitude = extractMagnitude(title),
                id = extractTag(entry, "id")
            )
        }.toList()
    }

    private fun extractTag(fragment: String, tag: String): String {
        val tagName = Regex.escape(tag)
        val regex = Regex("<$tagName[^>]*>([\\s\\S]*?)</$tagName>", RegexOption.IGNORE_CASE)
        return regex.find(fragment)?.groupValues?.get(1)?.trim().orEmpty()
            .replace("<![CDATA[", "")
            .replace("]]>", "")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
    }

    private fun extractAlternateLink(entry: String): String {
        val preferred = Regex(
            "<link[^>]*rel\\s*=\\s*\"alternate\"[^>]*href\\s*=\\s*\"([^\"]+)\"[^>]*/?>",
            RegexOption.IGNORE_CASE
        )
        val anyHref = Regex("<link[^>]*href\\s*=\\s*\"([^\"]+)\"[^>]*/?>", RegexOption.IGNORE_CASE)
        return preferred.find(entry)?.groupValues?.get(1)?.trim()
            ?: anyHref.find(entry)?.groupValues?.get(1)?.trim().orEmpty()
    }

    private fun extractMagnitude(title: String): Double? {
        val regex = Regex("M\\s+([0-9]+(?:\\.[0-9]+)?)")
        return regex.find(title)?.groupValues?.get(1)?.toDoubleOrNull()
    }

    private fun stripHtml(text: String): String {
        return text
            .replace(Regex("<[^>]*>"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}

package com.nido.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nido.utils.NetworkRequestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class PhotoData(val id: String, val secret: String, val server: String, val farm: String) {
    fun getPhotoUrl(): String = PHOTO_URL.format(farm, server, id, secret)

    companion object {
        private const val PHOTO_URL = "https://farm%s.static.flickr.com/%s/%s_%s_q.jpg"
    }
}

class MainActivityViewModel : ViewModel() {
    val photoData = MutableLiveData<MutableList<PhotoData>>()

    fun getImagesFromFlicker(searchQuery: String) {
        runBlocking { photoData.value =
            withContext(Dispatchers.Default) { getPhotoList(searchQuery) }
        }
    }

    private fun getPhotoList(searchQuery: String): MutableList<PhotoData> {
        val photosData: MutableList<PhotoData> = mutableListOf()
        NetworkRequestUtil.getDataFromNetwork(
            URL, mapOf(
                PARAM_API_KEY to API_KEY,
                PARAM_FORMAT to PARAM_FORMAT_VAL,
                PARAM_NO_JSON_CALLBACK to PARAM_NO_JSON_CALLBACK_VAL,
                PARAM_SAFE_SEARCH to PARAM_SAFE_SEARCH_VAL,
                PARAM_SEARCH_TEXT to searchQuery
            )
        )?.let {
            try {
                val jsonObject = JSONObject(it)
                val photosJson = jsonObject.getJSONObject("photos").getJSONArray("photo")
                for (i in 0 until photosJson.length()) {
                    val photoJson = photosJson.getJSONObject(i)
                    val photoData = PhotoData(
                        id = photoJson.getString("id"),
                        secret = photoJson.getString("secret"), server = photoJson.getString("server"),
                        farm = photoJson.getString("farm")
                    )
                    photosData.add(photoData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        return photosData
    }

    companion object {
        private const val URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search"
        private const val PARAM_API_KEY = "api_key"
//        private const val API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736"
        private const val API_KEY = "b616bee837e7b64a3aa8c9fc8f9176ae"
        private const val PARAM_FORMAT = "format"
        private const val PARAM_FORMAT_VAL = "json"
        private const val PARAM_NO_JSON_CALLBACK = "nojsoncallback"
        private const val PARAM_NO_JSON_CALLBACK_VAL = "1"
        private const val PARAM_SAFE_SEARCH = "safe_search"
        private const val PARAM_SAFE_SEARCH_VAL = "1"
        private const val PARAM_SEARCH_TEXT = "text"
    }
}
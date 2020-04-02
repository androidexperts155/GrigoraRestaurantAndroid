package com.rvtechnologies.grigorahq.complete_profile.pojo

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Kyra on 1/11/2016.
 */
class PlacePredictions_Pojo {

    @SerializedName("status")
    var status: String = ""

    @SerializedName("predictions")
    var predictions: ArrayList<Predictions> = ArrayList()

    inner class Predictions {

        @SerializedName("description")
        var description: String = ""

        @SerializedName("id")
        var id: String = ""

        @SerializedName("place_id")
        var place_id: String = ""
        @SerializedName("reference")
        var reference: String = ""

        @SerializedName("matched_substrings")
        var matched_substrings = ArrayList<MatchedSubstrings>()

        @SerializedName("structured_formatting")
        var structured_formatting: StructuredFormatting = StructuredFormatting()

        @SerializedName("terms")
        var terms = ArrayList<Terms>()
        @SerializedName("types")
        var types = ArrayList<String>()

        inner class MatchedSubstrings {
            @SerializedName("length")
            var length: String = ""

            @SerializedName("offset")
            var offset: String = ""
        }

        inner class StructuredFormatting {
            @SerializedName("main_text")
            var main_text: String = ""

            @SerializedName("main_text_matched_substrings")
            var main_text_matched_substrings = ArrayList<MainTextMatchedSubStrings>()
            @SerializedName("secondary_text")
            var secondary_text: String = ""

            inner class MainTextMatchedSubStrings {
                @SerializedName("length")
                var length: String = ""

                @SerializedName("offset")
                var offset: String = ""
            }
        }

        inner class Terms {
            @SerializedName("offset")
            var offset: String = ""

            @SerializedName("value")
            var value: String = ""
        }
    }

}

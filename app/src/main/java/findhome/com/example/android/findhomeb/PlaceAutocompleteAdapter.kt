package findhome.com.example.android.findhomeb

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filterable
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.common.api.GoogleApiClient
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.Filter
import com.google.android.gms.common.data.DataBufferUtils
import android.widget.Toast
import com.google.android.gms.location.places.Places
import java.util.concurrent.TimeUnit


class PlaceAutocompleteAdapter : ArrayAdapter<AutocompletePrediction>, Filterable {


    private val TAG = "PlaceAutoAdapter"
    private val STYLE_BOLD = StyleSpan(Typeface.BOLD)

    private var mResultList: ArrayList<AutocompletePrediction>? = null

    private lateinit var mGoogleApiClient: GoogleApiClient


    private lateinit var mBounds: LatLngBounds


    private lateinit var mPlaceFilter: AutocompleteFilter

    constructor(context: Context, mGoogleApiClient: GoogleApiClient, mBounds: LatLngBounds, mPlaceFilter: AutocompleteFilter) :
            super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1) {
        this.mGoogleApiClient = mGoogleApiClient
        this.mBounds = mBounds
        this.mPlaceFilter = mPlaceFilter
    }



    fun setBounds(bounds: LatLngBounds) {
        mBounds = bounds
    }


    override fun getCount(): Int {
        return mResultList!!.size
    }


    override fun getItem(position: Int): AutocompletePrediction? {
        return mResultList!![position]
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = super.getView(position, convertView, parent)

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        val item = getItem(position)

        val textView1 = row.findViewById(android.R.id.text1) as TextView
        val textView2 = row.findViewById(android.R.id.text2) as TextView
        textView1.text = item!!.getPrimaryText(STYLE_BOLD)
        textView2.text = item.getSecondaryText(STYLE_BOLD)

        return row
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                }
                Log.v("MYAUTORESULT",results.toString())
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated()
                }
            }

            override fun convertResultToString(resultValue: Any): CharSequence {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                return if (resultValue is AutocompletePrediction) {
                    resultValue.getFullText(null)
                } else {
                    super.convertResultToString(resultValue)
                }
            }
        }
    }


    private fun getAutocomplete(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        if (mGoogleApiClient.isConnected) {
            Log.i(TAG, "Starting autocomplete query for: $constraint")

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            val results = Places.GeoDataApi
                    .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                            mBounds, mPlaceFilter)

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            val autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS)

            // Confirm that the query completed successfully, otherwise return null
            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                Toast.makeText(context, "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString())
                autocompletePredictions.release()
                return null
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.count
                    + " predictions.")

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions)
        }
        Log.e(TAG, "Google API client is not connected for autocomplete query.")
        return null
    }

    internal inner class PlaceAutocomplete(var placeId: CharSequence, var description: CharSequence) {

        override fun toString(): String {
            return description.toString()
        }
    }
}
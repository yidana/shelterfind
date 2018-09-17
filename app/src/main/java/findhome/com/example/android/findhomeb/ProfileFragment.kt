package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.Toast
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R.drawable.roompic

import findhome.com.example.android.findhomeb.R.layout.fragment_profile
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profile_facility.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.entryFormoneFragment,null)

        }

        profile_account.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.accountSettingsFragment,null)

        }

        profile_feedback.setOnClickListener {


            Navigation.findNavController(it).navigate(R.id.feedBackFragment, null)

        }

        profile_settings.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.settingsFragment, null)
        }

         profile_notification.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.notificationFragment, null)
        }

       profile_manange.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.manageFragment, null)
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ProfileFragment()
    }
}

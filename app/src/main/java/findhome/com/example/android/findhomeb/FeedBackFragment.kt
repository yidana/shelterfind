package findhome.com.example.android.findhomeb


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_feed_back.*


class FeedBackFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_feed_back, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar) as Toolbar

        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.profileFragment, null)
        }

        img1.setOnClickListener {

            satisfaction.text="Dissatisfied"

        }


        img2.setOnClickListener {

            satisfaction.text="Normal"

        }


        img3.setOnClickListener {

            satisfaction.text="Satisfied"

        }


        img4.setOnClickListener {

            satisfaction.text="Very dissatisfied"

        }


        img5.setOnClickListener {

            satisfaction.text="Very Satisfied"

        }




    }

    companion object {

        @JvmStatic
        fun newInstance() =FeedBackFragment()
    }
}

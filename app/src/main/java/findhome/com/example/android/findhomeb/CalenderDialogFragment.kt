package findhome.com.example.android.findhomeb

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_calender.*
import kotlinx.android.synthetic.main.fragment_calender.view.*
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import java.util.*
import android.app.Activity






class CalenderDialogFragment: BottomSheetDialogFragment() {


    private  var listener: CalenderInteractionListener?=null

    fun newInstance(title: String,demoMode:DemoMode): CalenderDialogFragment {
        val frag =CalenderDialogFragment()
        val args = Bundle()
        args.putString("title", title)
        args.apply { putString(ARG_DEMO_MODE, demoMode.name) }
        frag.arguments = args
        return frag
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_calender, container,false)
    }





     interface CalenderInteractionListener {

         fun onDateInteraction(date: List<CalendarDate>)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val demoModeName = arguments?.getString(ARG_DEMO_MODE)
                ?: throw IllegalStateException()


        val demoMode = DemoMode.valueOf(demoModeName)


        val calendarView=view.calendar_view
        val calendar = Calendar.getInstance()

// Initial date
        calendar.set(2018, Calendar.JUNE, 1)
        val initialDate = CalendarDate(calendar.time)

// Minimum available date
        calendar.set(2018, Calendar.MAY, 15)
        val minDate = CalendarDate(calendar.time)

// Maximum available date
        calendar.set(2018, Calendar.JULY, 15)
        val maxDate = CalendarDate(calendar.time)





        if (savedInstanceState == null) {
            val selectedDates = when (demoMode.selectionMode) {
                CalendarView.SelectionMode.NON -> emptyList()
                CalendarView.SelectionMode.SINGLE -> singleSelectedDate()
                CalendarView.SelectionMode.MULTIPLE -> multipleSelectedDate()
                CalendarView.SelectionMode.RANGE -> rangeSelectedDate()
            }

            if (demoMode == DemoMode.LIMITED_DATES_SELECTION) {
                val mcalendar = Calendar.getInstance()
                mcalendar.set(2018, Calendar.JUNE, 1)
                val minitialDate = CalendarDate(calendar.time)

                calendar.set(2018, Calendar.MAY, 28)
                val mminDate = CalendarDate(calendar.time)

                calendar.set(2018, Calendar.JULY, 2)
                val mmaxDate = CalendarDate(calendar.time)

                calendarView.setupCalendar(
                        initialDate = minitialDate,
                        minDate = mminDate,
                        maxDate = mmaxDate,
                        selectionMode = CalendarView.SelectionMode.MULTIPLE
                )
            } else {
                calendarView.setupCalendar(
                        selectionMode = demoMode.selectionMode,
                        selectedDates = selectedDates
                )
            }
        }



        donebtn.setOnClickListener {
            dismiss()
        }



        // Set date click callback
        calendarView.onDateClickListener = {

            // Do something ...
            // for example get list of selected dates
            val selectedDates = calendarView.selectedDates

            listener?.onDateInteraction((selectedDates))
        }


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = activity as CalenderInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnHeadlineSelectedListener")
        }
    }


    enum class DemoMode(
            @StringRes val descriptionRes: Int,
            val selectionMode: CalendarView.SelectionMode
    ) {
        DISPLAY_ONLY(
                descriptionRes = R.string.demo_mode_no_selection,
                selectionMode = CalendarView.SelectionMode.NON
        ),
        SINGLE_SELECTION(
                descriptionRes = R.string.demo_mode_single_selection,
                selectionMode = CalendarView.SelectionMode.SINGLE
        ),
        MULTIPLE_SELECTION(
                descriptionRes = R.string.demo_mode_multiple_selection,
                selectionMode = CalendarView.SelectionMode.MULTIPLE
        ),
        RANGE_SELECTION(
                descriptionRes = R.string.demo_mode_range_selection,
                selectionMode = CalendarView.SelectionMode.RANGE
        ),
        LIMITED_DATES_SELECTION(
                descriptionRes = R.string.demo_mode_limited_selection,
                selectionMode = CalendarView.SelectionMode.MULTIPLE
        ),
        CUSTOM_EVENTS(
                descriptionRes = R.string.demo_mode_events,
                selectionMode = CalendarView.SelectionMode.NON
        ),
        CUSTOM_STYLE(
                descriptionRes = R.string.demo_mode_custom_style,
                selectionMode = CalendarView.SelectionMode.MULTIPLE
        ),
        DIALOG(
                descriptionRes = R.string.demo_mode_dialog,
                selectionMode = CalendarView.SelectionMode.MULTIPLE
        ),
        MOVE_TO_DATE(
                descriptionRes = R.string.demo_mode_move_to_date,
                selectionMode = CalendarView.SelectionMode.MULTIPLE
        )
    }




    private fun singleSelectedDate(): List<CalendarDate> {
        val calendar = Calendar.getInstance()
        calendar.set(2018, Calendar.JUNE, 18)
        return listOf(CalendarDate(calendar.time))
    }

    private fun multipleSelectedDate(): List<CalendarDate> {
        val calendar = Calendar.getInstance()
        val selectedDates = mutableListOf<CalendarDate>()

        calendar.set(2018, Calendar.JUNE, 13)
        selectedDates += CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JUNE, 16)
        selectedDates += CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JUNE, 19)
        selectedDates += CalendarDate(calendar.time)

        return selectedDates
    }

    private fun rangeSelectedDate(): List<CalendarDate> {
        val calendar = Calendar.getInstance()
        val selectedDates = mutableListOf<CalendarDate>()

        calendar.set(2018, Calendar.JUNE, 13)
        selectedDates += CalendarDate(calendar.time)

        calendar.set(2018, Calendar.JUNE, 18)
        selectedDates += CalendarDate(calendar.time)

        return selectedDates
    }


    companion object {
        private const val ARG_DEMO_MODE = "demo_mode"
    }
}
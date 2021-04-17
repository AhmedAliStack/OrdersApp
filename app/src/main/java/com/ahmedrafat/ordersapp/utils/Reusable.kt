package com.ahmedrafat.ordersapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.model.apimodels.ShopperModel
import com.ahmedrafat.ordersapp.model.datamodels.DateModel
import com.google.android.material.textfield.TextInputLayout
import java.lang.Double
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


private var loading: AlertDialog? = null

fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view: View? = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showLoading(context: Context) {
    loading = AlertDialog.Builder(context).setView(R.layout.dialog_loading).create()
    loading?.show()
}

fun dismissLoading() {
    if (loading != null)
        loading?.dismiss()
}

fun showCalender(
    context: Context?,
    title: String?,
    text_view_data: TextView,
    data1: DateModel,
    time: Boolean
) {
    val mDatePicker = DatePickerDialog(
        context!!, AlertDialog.THEME_HOLO_DARK,
        { datepicker: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val symbols =
                DecimalFormatSymbols(Locale.US)
            val mFormat = DecimalFormat("00", symbols)
            val data =
                (selectedYear.toString() + "-" + mFormat.format(java.lang.Double.valueOf((selectedMonth + 1).toDouble())) + "-"
                        + mFormat.format(java.lang.Double.valueOf(selectedDay.toDouble())))
            val dataP = DateModel(
                mFormat.format(Double.valueOf(selectedDay.toDouble())), mFormat.format(
                    Double.valueOf(
                        (selectedMonth + 1).toDouble()
                    )
                ), selectedYear.toString(), data
            )
            if (time) {
                showTimer(
                    context,
                    text_view_data,
                    dataP,
                    data
                )
            } else {
                data1.date_txt = data
                data1.day = mFormat.format(java.lang.Double.valueOf(selectedDay.toDouble()))
                data1.month =
                    mFormat.format(java.lang.Double.valueOf((selectedMonth + 1).toDouble()))
                data1.year = selectedYear.toString()
                text_view_data.text = data
            }
        }, data1.year.toInt(), data1.month.toInt() - 1, data1.day.toInt()
    )
    mDatePicker.datePicker.minDate = System.currentTimeMillis()
    mDatePicker.setTitle(title)
    mDatePicker.show()
}

fun validateInputs(inputs: ArrayList<TextInputLayout>): Boolean {
    for (item in inputs) {
        item.error = ""
        if (item.editText?.text.toString().trim().isEmpty()) {
            item.error = "This Field Required !"
            item.requestFocus()
            return false
        }
    }
    return true
}

fun validateItems(inputs: ArrayList<String>): Boolean {
    for (item in inputs) {
        if (item.isEmpty()) {
            return false
        }
    }
    return true
}

fun showTimer(context: Context, text_view_data: TextView, data1: DateModel, fullDate: String) {
    val mTimePicker: TimePickerDialog
    mTimePicker = TimePickerDialog(
        context, AlertDialog.THEME_HOLO_DARK,
        { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
            val symbols =
                DecimalFormatSymbols(Locale.US)
            val mFormat = DecimalFormat("00", symbols)
            val data =
                mFormat.format(java.lang.Double.valueOf(selectedHour.toDouble())) + ":" + mFormat.format(
                    java.lang.Double.valueOf(selectedMinute.toDouble())
                )
            data1.date_txt = data
            data1.day = "05"
            data1.month = mFormat.format(java.lang.Double.valueOf(selectedMinute.toDouble()))
            data1.year = mFormat.format(java.lang.Double.valueOf(selectedHour.toDouble()))
            text_view_data.text = "$fullDate $data"
        }, data1.year.toInt(), data1.month.toInt(), true
    ) //Yes 24 hour time

    mTimePicker.setTitle(context.getString(R.string.select_time))
    mTimePicker.show()
}

class ShopperAdapter(
    context: Context,
    resource: Int,
    private val objects: MutableList<ShopperModel>
) :
    ArrayAdapter<ShopperModel>(context, resource, objects) {

    init {
        objects.add(0, ShopperModel(0, context.getString(R.string.select_shopper)))
    }

    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): ShopperModel? {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.text = objects[position].name

        // And finally return your dynamic (or custom) view for each spinner item
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.text = objects[position].name

        return label
    }
}



package com.ahmedrafat.ordersapp.ui.addorderfragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.databinding.AddOrderFragmentBinding
import com.ahmedrafat.ordersapp.databinding.LocationDialogBinding
import com.ahmedrafat.ordersapp.databinding.SubmitReviewBinding
import com.ahmedrafat.ordersapp.model.REQUEST_CHECK_SETTINGS
import com.ahmedrafat.ordersapp.model.REQUEST_LOCATION
import com.ahmedrafat.ordersapp.model.datamodels.DateModel
import com.ahmedrafat.ordersapp.model.datamodels.ItemModel
import com.ahmedrafat.ordersapp.ui.addorderfragment.adpter.ItemsAdapter
import com.ahmedrafat.ordersapp.utils.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.lang.Double
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class AddOrderFragment : Fragment() {

    private lateinit var locationDialogBinding: LocationDialogBinding
    private lateinit var locationManager: LocationManager
    private lateinit var dateFrom: DateModel
    private val viewModel: AddOrderViewModel by viewModels()
    private lateinit var binding: AddOrderFragmentBinding
    private val itemsArray = ArrayList<ItemModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddOrderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getShoppers()

        initTimePicker()
        initLocation()
        initItems()

        binding.btSubmit.setOnClickListener {
            submitOrder()
        }

        observeResponse()
    }

    private fun submitOrder() {
        val userName = binding.tfName.editText?.text.toString().trim()
        val userPhone = binding.tfPhone.editText?.text.toString().trim()
        var shopper =
            if (binding.spShoppers.selectedItemPosition != 0) viewModel.shoppersModelMutableLiveData.value?.get(
                binding.spShoppers.selectedItemPosition
            )?.name.toString() else ""
        val deliverTime = binding.ReportsFragmentTvFrom.text.toString()
        val locationValue =
            if (binding.tvTypedLocation.text != getString(R.string.location)) binding.tvTypedLocation.text.toString() else ""


        if (validateInputs(arrayListOf(binding.tfName, binding.tfPhone)) && validateItems(
                arrayListOf(shopper, deliverTime, locationValue)
            ) && itemsArray.isNotEmpty()
        ) {
            val params = HashMap<String, String>()
            params["name"] = userName
            params["phone"] = userPhone
            params["shopper"] = shopper
            params["time"] = deliverTime
            viewModel.submitOrder(params, itemsArray)
        } else {
            Toast.makeText(
                requireContext(),
                "You have to fill the form to submit",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initItems() {
        val itemsAdapter = ItemsAdapter(itemsArray, object : ItemsAdapter.onDeleteListener {
            override fun onItemDelete(deletedItem: ItemModel, adapter: ItemsAdapter) {
                itemsArray.remove(deletedItem)
                adapter.notifyDataSetChanged()
            }
        })
        binding.rvItems.adapter = itemsAdapter
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.spItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val shopItems = resources.getStringArray(R.array.shopItems)
                    itemsArray.add(ItemModel(shopItems[position], 1))
                    itemsAdapter.notifyItemInserted(itemsArray.size)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun initLocation() {
        binding.btAddLocation.setOnClickListener {
            locationDialogBinding = LocationDialogBinding.inflate(LayoutInflater.from(context))
            val locationDialog: AlertDialog =
                AlertDialog.Builder(context).setView(locationDialogBinding.root).create()
            locationDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            locationDialog.show()

            locationDialogBinding.bnSetLocation.setOnClickListener {
                val typedLocation: String =
                    locationDialogBinding.tfLocation.editText?.text.toString().trim()
                if (typedLocation.isNotEmpty()) {
                    binding.tvTypedLocation.text = typedLocation
                    locationDialog.dismiss()
                } else {
                    locationDialogBinding.tfLocation.error = getString(R.string.enter_location)
                }
            }

            locationDialogBinding.bnCancel.setOnClickListener {
                locationDialog.dismiss()
            }

            locationDialogBinding.bnSetLocationGps.setOnClickListener {
                handleLocation()
            }
        }
    }

    private fun handleLocation() {
        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locPermission()
        } else {
            getAddress()
        }
    }

    private fun getAddress() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            locationDialogBinding.pbGetLocation.visibility = View.VISIBLE
            locationDialogBinding.bnSetLocationGps.visibility = View.GONE
            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location?.latitude != null) {
                addressFetch(location.latitude, location.longitude)
            } else
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 200, 100f
                ) {
                    addressFetch(it.latitude, it.longitude)
                }
        }
    }

    //fetch city from address
    private fun addressFetch(lattitude: kotlin.Double, longitude: kotlin.Double) {
        locationDialogBinding.pbGetLocation.visibility = View.GONE
        locationDialogBinding.bnSetLocationGps.visibility = View.VISIBLE
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(lattitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                locationDialogBinding.tfLocation.editText?.setText("$city - $state - $country")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_enable),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    getAddress()
                }
            }
        }
    }

    //open location dialog
    private fun locPermission() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        val mLocationSettingsRequest: LocationSettingsRequest
        builder.addLocationRequest(
            LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        )
        builder.setAlwaysShow(true)
        mLocationSettingsRequest = builder.build()
        val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(requireContext())
        mSettingsClient
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener {
                getAddress()
            }
            .addOnFailureListener { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val rae: ResolvableApiException = e as ResolvableApiException
                        rae.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                    } catch (sie: Exception) {
                        Log.e("GPS", "Unable to execute request.")
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                        "GPS",
                        "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                    )
                }
            }
            .addOnCanceledListener { Log.e("GPS", "checkLocationSettings -> onCanceled") }
    }

    private fun initTimePicker() {
        val symbols = DecimalFormatSymbols(Locale.US)
        val mFormat = DecimalFormat("00", symbols)
        val calander = Calendar.getInstance()
        val cDay = String.format(
            Locale("en"), mFormat.format(
                Double.valueOf(
                    Calendar.DAY_OF_MONTH.toDouble()
                )
            )
        )
        val cMonth = String.format(
            Locale("en"),
            mFormat.format(Double.valueOf((calander[Calendar.MONTH] + 1).toDouble()))
        )
        val cYear = calander[Calendar.YEAR].toString()

        val CfromDateTactDay = cYear + "-" + cMonth + "-" + (cDay.toInt() - 1)

        dateFrom = DateModel((cDay.toInt() - 1).toString(), cMonth, cYear, CfromDateTactDay)

        binding.ReportsFragmentLlFrom.setOnClickListener {
            showCalender(
                requireContext(),
                getString(R.string.time_to_be_delivered),
                binding.ReportsFragmentTvFrom,
                dateFrom,
                true
            )
        }
    }

    private fun observeResponse() {
        viewModel.shoppersModelMutableLiveData.observe(requireActivity(), {
            val arrayAdapter = ShopperAdapter(
                requireContext(), R.layout.dropdown_layout,
                it.toMutableList()
            )
            binding.spShoppers.adapter = arrayAdapter
        })

        viewModel.simulatedSubmitOrder.observe(requireActivity(), {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            showReview()

        })

        viewModel.reviewMutableLiveData.observe(requireActivity(),{
            Log.d("Review",it.toString())
            findNavController().popBackStack()
        })

        viewModel.loading.observe(requireActivity(), {
            if(it) showLoading(requireContext()) else dismissLoading()
        })

        viewModel.errorMutableLiveData.observe(requireActivity(),{
            Log.d("Error",it)
        })
    }

    private fun showReview() {
        val submitReviewBinding = SubmitReviewBinding.inflate(LayoutInflater.from(requireContext()))
        val reviewDialog = AlertDialog.Builder(requireContext()).setView(submitReviewBinding.root).create()
        reviewDialog.show()

        submitReviewBinding.btSubmitReview.setOnClickListener {
            val params = HashMap<String,String>()
            params["review"] = submitReviewBinding.tfReview.editText.toString().trim()
            params["rate"] = submitReviewBinding.rbRate.rating.toString()
            viewModel.submitRate(params)
            reviewDialog.dismiss()
        }
    }

}
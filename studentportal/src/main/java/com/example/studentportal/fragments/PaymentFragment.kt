package com.example.studentportal.fragments

import com.example.studentportal.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class PaymentFragment : Fragment(), PaymentResultWithDataListener,
    ExternalWalletListener {
    private var alertDialogBuilder: AlertDialog.Builder? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_payment, container, false)
        val button = view.findViewById<Button>(R.id.btn)
        alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder!!.setCancelable(false)
        alertDialogBuilder!!.setTitle("Payment Result")
        alertDialogBuilder!!.setPositiveButton(
            "Ok"
        ) { dialog: DialogInterface?, which: Int -> }
        button.setOnClickListener { v: View? -> startPayment() }
        return view
    }

    fun startPayment() {
        val co = Checkout()

        // Replace with your API key
        co.setKeyID("rzp_test_SUFjfCyI4tKbbw")
        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            options.put("send_sms_hash", true)
            options.put("allow_rotation", true)
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", "100"+"00")
            val preFill = JSONObject()
            preFill.put("email", "test@razorpay.com")
            preFill.put("contact", "9876543210")
            options.put("prefill", preFill)
            co.open(requireActivity(), options)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    override fun onExternalWalletSelected(s: String, paymentData: PaymentData) {
        try {
            alertDialogBuilder!!.setMessage(
                """
                    External Wallet Selected:
                    Payment Data: ${paymentData.data}
                    """.trimIndent()
            )
            alertDialogBuilder!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(s: String, paymentData: PaymentData) {
        try {
            alertDialogBuilder!!.setMessage(
                """
                    Payment Successful :
                    Payment ID: $s
                    Payment Data: ${paymentData.data}
                    """.trimIndent()
            )
            alertDialogBuilder!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentError(i: Int, s: String, paymentData: PaymentData) {
        try {
            alertDialogBuilder!!.setMessage(
                """
                    Payment Failed:
                    Payment Data: ${paymentData.data}
                    """.trimIndent()
            )
            alertDialogBuilder!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = PaymentFragment::class.java.simpleName
    }
}

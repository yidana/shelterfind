package findhome.com.example.android.findhomeb.uti


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.ProgressBar
import findhome.com.example.android.findhomeb.R


class LoadDialog
{
    private var mDialog: Dialog? = null
    var mProgressDialog: ProgressDialog? = null


    fun showDialog(context: Context) {
        mDialog = Dialog(context, R.style.NewDialog)
        mDialog!!.addContentView(
                ProgressBar(context),
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        )
        mDialog!!.setCancelable(true)
        mDialog!!.show()
    }

    fun dismissDialog() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    fun initProgressDialog(context: Context) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog!!.setMessage(context.getString(R.string.loading))
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.max = 100
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    }

    fun setProgress(i: Int) {
        mProgressDialog!!.progress = i
    }

    fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }
}
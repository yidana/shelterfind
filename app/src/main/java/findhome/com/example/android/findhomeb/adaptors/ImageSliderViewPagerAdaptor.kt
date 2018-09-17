package findhome.com.example.android.findhomeb.adaptors

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R


class ImageSliderViewPagerAdaptor(context: Context,myimage:ArrayList<String> ): PagerAdapter() {


    private val IMAGES=myimage
    private val  mcontext=context
    private val minflater=LayoutInflater.from(mcontext)

    override fun isViewFromObject(view: View, objects: Any): Boolean {
        return view == objects
    }


    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = minflater.inflate(R.layout.image_slider_layout, view, false)!!

        val imageView = imageLayout
                .findViewById(R.id.image_slide) as ImageView


        Picasso.get().load(Uri.parse( IMAGES[position])).fit().into( imageView)



        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun getCount(): Int {
       return IMAGES.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, objects: Any) {
        container.removeView(objects as View)
    }


}
package com.android.example.pw1

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.example.pw1.databinding.ActivityMain2Binding
import java.util.*


class MainActivity2 : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMain2Binding
    lateinit var imageList: List<Uri>

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        imageList = ArrayList<Uri>()

        viewPager = viewBinding.pager

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Images.Media._ID
        )

        val query = contentResolver.query(collection, projection, null, null, null)

        query?.use { cursor ->
            val idCollumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while(cursor.moveToNext()) {
                val id = cursor.getLong(idCollumn)

                val contentUri =  ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                imageList = imageList + contentUri
            }
        }



        viewPagerAdapter = ViewPagerAdapter(this, imageList)
        viewPager.adapter = viewPagerAdapter



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.gallery_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.take_images -> {
                val record = Intent(this, MainActivity::class.java)
                startActivity(record)
                true
            }
            R.id.delete -> {
                val collection =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        MediaStore.Images.Media.getContentUri(
                            MediaStore.VOLUME_EXTERNAL
                        )
                    } else {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                val projection = arrayOf(
                    MediaStore.Images.Media._ID
                )

                val cursor = contentResolver.query(collection, projection, null, null, null)

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                        val contentUri = ContentUris.withAppendedId(collection, id)
                        contentResolver.delete(contentUri, null, null)
                    }
                }

                val record = Intent(this, MainActivity2::class.java)
                startActivity(record)

                true
            }
            R.id.audio -> {
                val record = Intent(this, MainActivity3::class.java)
                startActivity(record)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


class ViewPagerAdapter (val context: Context, val imageList: List<Uri>) : PagerAdapter(){
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mlayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mlayoutInflater.inflate(R.layout.image_view, container, false)

        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView

        imageView.setImageURI(imageList.get(position))

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
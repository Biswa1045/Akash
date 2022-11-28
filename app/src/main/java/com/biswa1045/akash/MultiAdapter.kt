package com.biswa1045.akash

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList

class MultiAdapter(context: Context, apps: ArrayList<check>) :
RecyclerView.Adapter<MultiAdapter.MultiViewHolder>() {
    private val context: Context
    private  var apps: ArrayList<check>
    fun setBillList(apps: ArrayList<check>) {
        this.apps = ArrayList()
        this.apps = apps
        notifyDataSetChanged()
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): MultiViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_cat, viewGroup, false)
        return MultiViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull multiViewHolder: MultiViewHolder, position: Int) {

        multiViewHolder.bind(apps[position])
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    inner class MultiViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val price: TextView
        val stock: TextView
        val img: ImageView
        val card: CardView
        fun bind(app: check) {
            if(app.isChecked){
                card.background.setTint(Color.GREEN)
            }else{
                card.background.setTint(Color.WHITE)
            }
            name.text=app.productname
            price.text=app.productprice+"/-"
            stock.text=app.productstuck+" pcs"

            Glide.with(context).load(app.img)
                .apply(RequestOptions().centerCrop())
                .into(img)
            itemView.setOnClickListener {
                val v= !app.isChecked

                if(v){
                    card.background.setTint(Color.GREEN)
                }else{
                    card.background.setTint(Color.WHITE)
                }
                app.isChecked = v
            }
        }

        init {
            name = itemView.findViewById(R.id.name_product)
            price = itemView.findViewById(R.id.price_product)
            stock = itemView.findViewById(R.id.stock_product)
            img = itemView.findViewById(R.id.img_product)
            card = itemView.findViewById(R.id.cat_card)
        }
    }

    val all: ArrayList<check>
    get() = apps
    val selected: ArrayList<check>
    get() {
        val selected: ArrayList<check> = ArrayList()
        for (i in 0 until apps.size) {
            if (apps[i].isChecked) {
                selected.add(apps[i])
            }
        }
        return selected
    }

    init {
        this.context = context
        this.apps = apps
    }
}
package com.ramiyon.soulmath.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ramiyon.soulmath.R
import com.ramiyon.soulmath.base.BaseRecyclerViewAdapter
import com.ramiyon.soulmath.databinding.ItemListMaterialDashboardBinding
import com.ramiyon.soulmath.domain.model.material.Material
import com.ramiyon.soulmath.presentation.diff_callback.MaterialDiffUtil
import com.ramiyon.soulmath.presentation.ui.material.video.MaterialVideoPlayerActivity
import com.ramiyon.soulmath.util.Constanta.ARG_MATERIAL_ID
import com.ramiyon.soulmath.util.Constanta.ARG_MODULE_TITLE

class MaterialAdapter: BaseRecyclerViewAdapter<ItemListMaterialDashboardBinding, Material>() {
    
    var mapData = mapOf<String, Any>()
    
    override fun inflateViewBinding(parent: ViewGroup): ItemListMaterialDashboardBinding {
        return ItemListMaterialDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    @SuppressLint("SetTextI18n")
    override val binder: (Material, ItemListMaterialDashboardBinding) -> Unit = { item, binding ->
        binding.apply {
            Glide.with(itemView!!.context).load(item.materialImage).into(ivMaterial)
            tvMaterialSubModule.text = "Lihat Video ${item.subModuleTitle}"
            Glide.with(itemView!!.context)
                .load(if(position?.mod(2) == 0) R.drawable.ic_material_dashboard_path_left_to_right
                else R.drawable.ic_material_dashboard_path_right_to_left)
                .into(ivMaterialPath)

            if(position?.plus(1) == size) {
                ivMaterialPath.visibility = View.INVISIBLE
            }

            itemView?.setOnClickListener {
                val moduleTitle = mapData[ARG_MODULE_TITLE] as String
                val intent = Intent(itemView?.context, MaterialVideoPlayerActivity::class.java)
                intent.putExtra(ARG_MATERIAL_ID, item.materialId)
                intent.putExtra(ARG_MODULE_TITLE, moduleTitle)
                itemView?.context?.startActivity(intent)
            }
        }
    }

    override val diffUtilBuilder: (List<Material>, List<Material>) -> DiffUtil.Callback = {old, new ->
        MaterialDiffUtil(old, new)
    }

}
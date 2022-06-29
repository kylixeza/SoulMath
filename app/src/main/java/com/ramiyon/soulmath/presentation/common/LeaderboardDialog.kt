package com.ramiyon.soulmath.presentation.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ramiyon.soulmath.R
import com.ramiyon.soulmath.databinding.DialogRankBinding
import com.ramiyon.soulmath.databinding.FragmentLeaderboardBinding
import com.ramiyon.soulmath.domain.model.Student

@SuppressLint("InflateParams")
fun Context.buildLeaderboardDialog(
    layoutInflater: LayoutInflater,
    rank: Int,
    data: Student
) {
    val materialBuilder = MaterialAlertDialogBuilder(this).create()
    val binding = DialogRankBinding.inflate(layoutInflater)

    binding.apply {
        Glide.with(this@buildLeaderboardDialog)
            .load(data.avatar)
            .into(binding.ivProfile)
        tvXp.text = "${data.xp} XP"
        tvDescRank.text = getString(R.string.leaderboard_dialog_description, rank)
        btnOk.setOnClickListener { materialBuilder.dismiss() }
        ivClose.setOnClickListener { materialBuilder.dismiss() }
        materialBuilder.setView(binding.root)
        materialBuilder.show()
    }
}
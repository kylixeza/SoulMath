package com.ramiyon.soulmath.data.source.dummy

import com.ramiyon.soulmath.domain.model.material.MaterialLearningPurpose
import com.ramiyon.soulmath.domain.model.material.MaterialOnBoard

object MaterialOnBoardContent {
    private fun getMaterialOnBoardContent() = listOf(
        MaterialOnBoard(materialId = "MATERIAL001", page = 1, gif = "", description = "Tahukah kamu materi apa yang akan kita pelajari kali ini?"),
        MaterialOnBoard(materialId = "MATERIAL001", page = 2, gif = "0", description = "Hari ini, kita akan belajar mengenal angka mulai dari 1-10. Yuk, simak lebih lanjut materinya"),
        MaterialOnBoard(materialId = "MATERIAL001", page = 3, upperImage = "", lowerImage = "", description = "Di atas ini adalah contoh dari angka yang akan kita pelajari hari ini!"),

        MaterialOnBoard(materialId = "MATERIAL002", page = 1, gif = "", description = "Tahukah kamu materi apa yang akan kita pelajari kali ini?"),
        MaterialOnBoard(materialId = "MATERIAL002", page = 2, gif = "0", description = "Hari ini, kita akan belajar mengenal angka mulai dari 11-100. Yuk, simak lebih lanjut materinya"),
        MaterialOnBoard(materialId = "MATERIAL002", page = 3, upperImage = "", lowerImage = "", description = "Di atas ini adalah contoh dari angka yang akan kita pelajari hari ini!"),

        MaterialOnBoard(materialId = "MATERIAL003", page = 1, gif = "", description = "Tahukah kamu materi apa yang akan kita pelajari kali ini?"),
        MaterialOnBoard(materialId = "MATERIAL003", page = 2, gif = "0", description = "Hari ini, kita akan belajar mengenal angka mulai dari 101-999. Yuk, simak lebih lanjut materinya"),
        MaterialOnBoard(materialId = "MATERIAL003", page = 3, upperImage = "", lowerImage = "", description = "Di atas ini adalah contoh dari angka yang akan kita pelajari hari ini!"),
    )

    fun getMaterialOnBoardContentById(materialId: String, page: Int) =
        getMaterialOnBoardContent().first {
            it.materialId == materialId && it.page == page
        }

    private fun getMaterialLearningPurpose() = listOf(
        MaterialLearningPurpose("MATERIAL001", "Pengenalan Angka", listOf(
            "Melalui media video siswa mampu mengenali angka dengan tepat.",
            "Melalui media video siswa mampu mempraktikan jumlah angka dengan tepat.",
            "Setelah berlatih latihan soal, siswa dapat menyebutkan angka dengan lancar.",
            "Dengan mengerjakan latihan soal, siswa mampu menuliskan angka dengan tepat."
        )),

        MaterialLearningPurpose("MATERIAL002", "Pengenalan Angka", listOf(
            "Melalui media video siswa mampu mengenali angka dengan tepat.",
            "Melalui media video siswa mampu mempraktikan jumlah angka dengan tepat.",
            "Setelah berlatih latihan soal, siswa dapat menyebutkan angka dengan lancar.",
            "Dengan mengerjakan latihan soal, siswa mampu menuliskan angka dengan tepat."
        )),

        MaterialLearningPurpose("MATERIAL003", "Pengenalan Angka", listOf(
            "Melalui media video siswa mampu mengenali angka dengan tepat.",
            "Melalui media video siswa mampu mempraktikan jumlah angka dengan tepat.",
            "Setelah berlatih latihan soal, siswa dapat menyebutkan angka dengan lancar.",
            "Dengan mengerjakan latihan soal, siswa mampu menuliskan angka dengan tepat."
        )),
    )
    fun getMaterialLearningPurposeById(materialId: String) = getMaterialLearningPurpose().first {
        it.materialId == materialId
    }
}
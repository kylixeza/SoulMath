package com.ramiyon.soulmath.presentation.validator

import androidx.viewbinding.ViewBinding

interface ConstraintValidator<VB: ViewBinding> {

    suspend fun VB.validate()

}
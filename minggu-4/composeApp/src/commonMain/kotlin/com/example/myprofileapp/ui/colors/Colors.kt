package com.example.myprofileapp.ui.colors

import androidx.compose.ui.graphics.Color
import com.example.myprofileapp.ui.colors.style.colorscheme.dark.CatppuccinMocha
import com.example.myprofileapp.ui.colors.style.colorscheme.dark.GruvBoxDark
import com.example.myprofileapp.ui.colors.style.colorscheme.light.CatppuccinLatte
import com.example.myprofileapp.ui.colors.style.colorscheme.light.GruvBoxLight

data class Colors(
    val backgroundMain: Color,
    val backgroundTopBar: Color,
    val backgroundCard: Color,

    val textPrimary: Color,
    val textSecondary: Color,

    val borderUnfocused: Color,

    val accentPrimary: Color,
    val accentSecondary: Color,
    val link: Color,

    val success: Color,
    val error: Color
)

data class Theme(
    val name: String,
    val light: Colors,
    val dark: Colors
)

object Themes {

    val Catppuccin = Theme(
        name = "Catppuccin",

        light = Colors(
            backgroundMain = CatppuccinLatte.base,
            backgroundTopBar = CatppuccinLatte.crust,
            backgroundCard = CatppuccinLatte.mantle,

            textPrimary = CatppuccinLatte.text,
            textSecondary = CatppuccinLatte.subtext0,

            borderUnfocused = CatppuccinLatte.overlay0,

            accentPrimary = CatppuccinLatte.mauve,
            accentSecondary = CatppuccinLatte.blue,
            link = CatppuccinLatte.sapphire,

            success = CatppuccinLatte.green,
            error = CatppuccinLatte.red
        ),

        dark = Colors(
            backgroundMain = CatppuccinMocha.base,
            backgroundTopBar = CatppuccinMocha.crust,
            backgroundCard = CatppuccinMocha.mantle,

            textPrimary = CatppuccinMocha.text,
            textSecondary = CatppuccinMocha.subtext0,

            borderUnfocused = CatppuccinMocha.overlay0,

            accentPrimary = CatppuccinMocha.mauve,
            accentSecondary = CatppuccinMocha.blue,
            link = CatppuccinMocha.sapphire,

            success = CatppuccinMocha.green,
            error = CatppuccinMocha.red
        )
    )

    val GruvBox = Theme(
        name = "GruvBox",

        light = Colors(
            backgroundMain = GruvBoxLight.bg0,
            backgroundTopBar = GruvBoxLight.bgHard,
            backgroundCard = GruvBoxLight.bgSoft,

            textPrimary = GruvBoxLight.fg1,
            textSecondary = GruvBoxLight.fg4,

            borderUnfocused = GruvBoxLight.bg4,

            accentPrimary = GruvBoxLight.orangeStrong,
            accentSecondary = GruvBoxLight.blueStrong,
            link = GruvBoxLight.aquaStrong,

            success = GruvBoxLight.greenStrong,
            error = GruvBoxLight.redStrong
        ),

        dark = Colors(
            backgroundMain = GruvBoxDark.bg0,
            backgroundTopBar = GruvBoxDark.bgHard,
            backgroundCard = GruvBoxDark.bgSoft,

            textPrimary = GruvBoxDark.fg1,
            textSecondary = GruvBoxDark.fg4,

            borderUnfocused = GruvBoxDark.bg3,

            accentPrimary = GruvBoxDark.orangeStrong,
            accentSecondary = GruvBoxDark.blueStrong,
            link = GruvBoxDark.aquaStrong,

            success = GruvBoxDark.greenStrong,
            error = GruvBoxDark.redStrong
        )
    )
}
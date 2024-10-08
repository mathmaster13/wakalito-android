package io.github.mathmaster13.wakalito

import io.github.mathmaster13.wakalito.Key.*

// The UCSUR for a key is "\uDB87$surr".

const val UCSUR_HIGH = '\uDB87'

enum class Key(internal val buttonID: Int, internal val surr: Char) {
    // order of enum entries SHOULD NOT be relied upon except that special keys are last
    OP_BR(R.id.bracket_open, '\uDC86'), CL_BR(R.id.bracket_pini, '\uDC87'),
    LA(R.id.la, '\uDC89'), CIRCLE(R.id.circle, '\uDC88'),
    UP(R.id.up, '\uDC83'), DOWN(R.id.down, '\uDC82'), POKI(R.id.poki, '\uDC85'),
    BOX(R.id.box, '\uDC8E'), EMITTERS(R.id.emitters, '\uDC8D'),
    PONA(R.id.pona, '\uDC94'), IKE(R.id.ike, '\uDC93'),
    HOR(R.id.hor, '\uDC9A'), VERT(R.id.vert, '\uDC99'), LUKA(R.id.luka, '\uDC90'),

    DOT(R.id.dot, '\uDC8B'), COLON(R.id.colon, '\uDC8C'), COMMA(R.id.comma, '\uDC9C');

    // DEBUG ONLY
    override fun toString(): String = when(this) {
        OP_BR -> "["
        CL_BR -> "]"
        LA -> ")"
        CIRCLE -> "O"
        UP -> "^"
        DOWN -> "v"
        POKI -> "U"
        BOX -> "#"
        EMITTERS -> "*"
        PONA -> "u"
        IKE -> "n"
        HOR -> "-"
        VERT -> "|"
        LUKA -> "h"
        DOT -> "."
        COLON -> ":"
        COMMA -> ","
    }
}

// ready for a gigantic map? TODO add code for yupekosi because why not
// This map is sponsored by jan Tepo's list and CTRL+F/CTRL+R.
// I hate that Kotlin has no array literals outside of annotations.

// FIXME This may become Map<String, String> using UCSUR to save space, idk
val sequences: Map<ArrayList<Key>, String> = buildMap {
    put(arrayListOf(COLON, COLON), "-")
    put(arrayListOf(COMMA, COMMA), "\"")
    put(arrayListOf(OP_BR, CL_BR), "/sp")
    put(arrayListOf(DOT), ".")
    put(arrayListOf(HOR), "__") // TODO do we keep this one? seems like a linja sike relic
    put(arrayListOf(DOT, DOT, DOT), "...")
    put(arrayListOf(VERT, BOX, DOWN), "misonaala")
    put(arrayListOf(COLON, LA), ":)")
    put(arrayListOf(COLON, CL_BR), ":)")
    put(arrayListOf(COLON, PONA), ":)")
    put(arrayListOf(COLON, OP_BR), ":(")
    put(arrayListOf(COLON, IKE), ":(")
    put(arrayListOf(COLON, HOR), ":|")
    put(arrayListOf(COLON, VERT), ":|")
    put(arrayListOf(COLON, DOWN), ":v")
    put(arrayListOf(VERT, CIRCLE, DOT), "a")
    put(arrayListOf(VERT, DOT, DOT), "a")
    put(arrayListOf(VERT, DOT, VERT, DOT), "aaa")
    put(arrayListOf(DOT, DOT, HOR, HOR, HOR, CIRCLE), "akesi")
    put(arrayListOf(CIRCLE, HOR, HOR, HOR, DOT, DOT), "akesi")
    put(arrayListOf(DOWN, UP), "ala")
    put(arrayListOf(VERT, LA, HOR), "alasa")
    put(arrayListOf(VERT, LA, HOR, UP), "alasa")
    put(arrayListOf(LA, VERT, HOR), "alasa")
    put(arrayListOf(LA, VERT, HOR, UP), "alasa")
    put(arrayListOf(CIRCLE, LA), "ale")
    put(arrayListOf(DOT, POKI, DOT), "anpa")
    put(arrayListOf(UP, DOWN), "ante")
    put(arrayListOf(DOWN, VERT), "anu")
    put(arrayListOf(HOR, UP, HOR), "awen")
    put(arrayListOf(UP, UP), "e")
    put(arrayListOf(VERT, HOR, VERT), "en")
    put(arrayListOf(PONA, VERT, IKE), "esun")
    put(arrayListOf(CIRCLE), "ijo")
    put(arrayListOf(IKE), "ike")
    put(arrayListOf(BOX, VERT), "ilo")
    put(arrayListOf(VERT, BOX), "ilo")
    put(arrayListOf(POKI, DOT, DOT), "insa")
    put(arrayListOf(DOT, DOT, POKI, DOT, DOT), "insa")
    put(arrayListOf(LUKA, LUKA, LUKA), "jaki")
    put(arrayListOf(CIRCLE, UP), "jan")
    put(arrayListOf(CIRCLE, BOX, UP, HOR), "jelo")
    put(arrayListOf(CIRCLE, PONA, HOR), "jo")
    put(arrayListOf(UP, CIRCLE), "kala")
    put(arrayListOf(PONA, HOR, EMITTERS), "kalama")
    put(arrayListOf(HOR, HOR, UP), "kama")
    put(arrayListOf(CIRCLE, CIRCLE, VERT), "kasi")
    put(arrayListOf(VERT, CIRCLE, CIRCLE), "kasi")
    put(arrayListOf(CIRCLE, VERT, CIRCLE), "kasi")
    put(arrayListOf(VERT, VERT, DOWN), "ken")
    put(arrayListOf(BOX, VERT, LUKA), "kepeken")
    put(arrayListOf(VERT, BOX, LUKA), "kepeken")
    put(arrayListOf(LUKA, VERT, BOX), "kepeken")
    put(arrayListOf(LUKA, BOX, VERT), "kepeken")
    put(arrayListOf(BOX, LUKA, VERT), "kepeken")
    put(arrayListOf(IKE, IKE, PONA, VERT), "kili")
    put(arrayListOf(IKE, PONA, IKE, VERT), "kili")
    put(arrayListOf(VERT, DOWN, UP, VERT), "kin")
    put(arrayListOf(VERT, DOWN, UP), "kin")
    put(arrayListOf(DOWN, UP, HOR), "kiwen")
    put(arrayListOf(DOWN, VERT, VERT, HOR), "kiwen")
    put(arrayListOf(IKE, CIRCLE, PONA), "ko")
    put(arrayListOf(PONA, CIRCLE, IKE), "ko")
    put(arrayListOf(DOWN, UP, DOWN, UP), "kon")
    put(arrayListOf(LA, LA, LA, LA), "kon")
    put(arrayListOf(UP, DOWN, UP, DOWN), "kon")
    put(arrayListOf(UP, HOR), "kule")
    put(arrayListOf(UP, HOR, HOR, HOR), "kule")
    put(arrayListOf(CIRCLE, CIRCLE, CIRCLE), "kulupu")
    put(arrayListOf(LA, DOT), "kute")
    put(arrayListOf(DOT, LA), "kute")
    put(arrayListOf(LA), "la")
    put(arrayListOf(HOR, HOR, CIRCLE), "lape")
    put(arrayListOf(CIRCLE, CIRCLE, VERT, UP, HOR), "laso")
    put(arrayListOf(VERT, CIRCLE, CIRCLE, UP, HOR), "laso")
    put(arrayListOf(VERT, CIRCLE, CIRCLE, HOR, UP), "laso")
    put(arrayListOf(CIRCLE, CIRCLE, VERT, HOR, UP), "laso")
    put(arrayListOf(UP, HOR, VERT, CIRCLE, CIRCLE), "laso")
    put(arrayListOf(UP, HOR, CIRCLE, VERT, CIRCLE), "laso")
    put(arrayListOf(CIRCLE, HOR), "lawa")
    put(arrayListOf(BOX, VERT, VERT, VERT), "len")
    put(arrayListOf(VERT, VERT, VERT, BOX), "len")
    put(arrayListOf(DOWN, HOR, UP), "lete")
    put(arrayListOf(UP, DOWN, HOR), "lete")
    put(arrayListOf(UP), "li")
    put(arrayListOf(DOWN), "lili")
    put(arrayListOf(PONA, IKE), "linja")
    put(arrayListOf(IKE, PONA), "linja")
    put(arrayListOf(BOX), "lipu")
    put(arrayListOf(PONA, HOR, UP, HOR), "loje")
    put(arrayListOf(HOR, PONA, UP, HOR), "loje")
    put(arrayListOf(UP, HOR, PONA, HOR), "loje")
    put(arrayListOf(HOR, UP, PONA, HOR), "loje")
    put(arrayListOf(UP, HOR, HOR, PONA), "loje")
    put(arrayListOf(HOR, UP, HOR, PONA), "loje")
    put(arrayListOf(DOT, HOR), "lon")
    put(arrayListOf(HOR, DOT), "lon")
    put(arrayListOf(LUKA), "luka")
    put(arrayListOf(CIRCLE, DOT), "lukin")
    put(arrayListOf(DOWN, LA, DOT), "oko")
    put(arrayListOf(PONA, VERT, VERT), "lupa")
    put(arrayListOf(VERT, PONA, VERT), "lupa")
    put(arrayListOf(VERT, VERT, PONA), "lupa")
    put(arrayListOf(CIRCLE, HOR, VERT), "ma")
    put(arrayListOf(DOT, CIRCLE), "mama")
    put(arrayListOf(CIRCLE, PONA), "mani")
    put(arrayListOf(HOR, CIRCLE, HOR), "mani")
    put(arrayListOf(IKE, CIRCLE), "meli")
    put(arrayListOf(CIRCLE, IKE), "meli")
    put(arrayListOf(VERT, CIRCLE), "mi")
    put(arrayListOf(UP, CIRCLE, UP), "mije")
    put(arrayListOf(PONA, HOR, LUKA), "moku")
    put(arrayListOf(HOR, PONA, LUKA), "moku")
    put(arrayListOf(LUKA, HOR, PONA), "moku")
    put(arrayListOf(LUKA, PONA, HOR), "moku")
    put(arrayListOf(CIRCLE, DOWN, UP, DOWN, UP), "moli")
    put(arrayListOf(CIRCLE, UP, DOWN, UP, DOWN), "moli")
    put(arrayListOf(CIRCLE, DOWN, UP), "moli")
    put(arrayListOf(DOT, HOR, VERT, HOR), "monsi")
    put(arrayListOf(DOT, OP_BR), "monsi")
    put(arrayListOf(CIRCLE, CIRCLE, DOT, CIRCLE), "mu")
    put(arrayListOf(CIRCLE, DOT, CIRCLE, CIRCLE), "mu")
    put(arrayListOf(CIRCLE, CIRCLE, CIRCLE, DOT), "mu")
    put(arrayListOf(LA, LA), "mun")
    put(arrayListOf(CIRCLE, PONA, CIRCLE), "musi")
    put(arrayListOf(VERT, VERT, VERT), "mute")
    put(arrayListOf(BOX, DOT), "namako")
    put(arrayListOf(DOT, BOX), "namako")
    put(arrayListOf(VERT, VERT, HOR, HOR), "nanpa")
    put(arrayListOf(HOR, HOR, VERT, VERT), "nanpa")
    put(arrayListOf(PONA, IKE, PONA), "nasa")
    put(arrayListOf(IKE, PONA, IKE), "nasa")
    put(arrayListOf(VERT, UP), "nasin")
    put(arrayListOf(UP, VERT), "nasin")
    put(arrayListOf(IKE, VERT, VERT), "nena")
    put(arrayListOf(VERT, VERT, IKE), "nena")
    put(arrayListOf(VERT, IKE, VERT), "nena")
    put(arrayListOf(VERT, DOWN), "ni")
    put(arrayListOf(VERT, HOR, VERT, HOR), "nimi")
    put(arrayListOf(HOR, VERT, HOR, VERT), "nimi")
    put(arrayListOf(VERT, LA, VERT), "noka")
    put(arrayListOf(VERT, LA, HOR, VERT), "noka")
    put(arrayListOf(VERT, DOT), "o")
    put(arrayListOf(IKE, IKE, DOWN, IKE, IKE, DOWN), "olin")
    put(arrayListOf(DOWN, IKE, IKE, DOWN, IKE, IKE), "olin")
    put(arrayListOf(HOR, CIRCLE), "ona")
    put(arrayListOf(POKI, HOR), "open")
    put(arrayListOf(HOR, POKI), "open")
    put(arrayListOf(BOX, DOWN, UP), "pakala")
    put(arrayListOf(BOX, UP, DOWN), "pakala")
    put(arrayListOf(CIRCLE, LUKA), "pali")
    put(arrayListOf(LUKA, CIRCLE), "pali")
    put(arrayListOf(IKE, VERT, VERT, PONA), "palisa")
    put(arrayListOf(PONA, VERT, VERT, IKE), "palisa")
    put(arrayListOf(IKE, VERT, PONA, VERT), "palisa")
    put(arrayListOf(PONA, VERT, IKE, VERT), "palisa")
    put(arrayListOf(VERT, IKE, VERT, PONA), "palisa")
    put(arrayListOf(VERT, PONA, VERT, IKE), "palisa")
    put(arrayListOf(DOWN, DOWN, DOWN), "pan")
    put(arrayListOf(LUKA, EMITTERS), "pana")
    put(arrayListOf(EMITTERS, LUKA), "pana")
    put(arrayListOf(VERT, HOR), "pi")
    put(arrayListOf(IKE, IKE, DOWN), "pilin")
    put(arrayListOf(DOWN, IKE, IKE), "pilin")
    put(arrayListOf(IKE, IKE, PONA), "pilin")
    put(arrayListOf(UP, HOR, DOWN, UP), "pimeja")
    put(arrayListOf(DOWN, UP, UP, HOR), "pimeja")
    put(arrayListOf(HOR, VERT, HOR), "pini")
    put(arrayListOf(DOT, DOT, VERT, HOR, HOR, HOR), "pipi")
    put(arrayListOf(VERT, HOR, HOR, HOR, DOT, DOT), "pipi")
    put(arrayListOf(HOR, HOR, HOR, VERT, DOT, DOT), "pipi")
    put(arrayListOf(POKI, DOT), "poka")
    put(arrayListOf(POKI), "poki")
    put(arrayListOf(PONA), "pona")
    put(arrayListOf(BOX, CIRCLE, EMITTERS, PONA), "pu")
    put(arrayListOf(HOR, HOR), "sama")
    put(arrayListOf(DOT, EMITTERS), "seli")
    put(arrayListOf(EMITTERS, DOT), "seli")
    put(arrayListOf(VERT, HOR, VERT, VERT, VERT), "selo")
    put(arrayListOf(HOR, VERT, VERT, VERT, VERT), "selo")
    put(arrayListOf(HOR, HOR, VERT, HOR, HOR), "selo")
    put(arrayListOf(LUKA, DOT), "seme")
    put(arrayListOf(DOT, LUKA), "seme")
    put(arrayListOf(PONA, PONA, VERT), "sewi")
    put(arrayListOf(VERT, PONA, PONA, VERT), "sewi")
    put(arrayListOf(VERT, HOR, VERT, VERT), "sijelo")
    put(arrayListOf(HOR, VERT, VERT, VERT), "sijelo")
    put(arrayListOf(CIRCLE, CIRCLE), "sike")
    put(arrayListOf(EMITTERS), "sin")
    put(arrayListOf(CIRCLE, VERT), "sina")
    put(arrayListOf(HOR, VERT, HOR, DOT), "sinpin")
    put(arrayListOf(CL_BR, DOT), "sinpin")
    put(arrayListOf(BOX, DOT, DOT, DOT), "sitelen")
    put(arrayListOf(BOX, DOT, DOT), "sitelen")
    put(arrayListOf(BOX, EMITTERS), "sona")
    put(arrayListOf(EMITTERS, BOX), "sona")
    put(arrayListOf(HOR, LA, DOT, DOT, VERT, VERT, VERT, VERT), "soweli")
    put(arrayListOf(LA, DOT, DOT, VERT, VERT), "soweli")
    put(arrayListOf(LA, VERT, VERT, VERT, VERT, DOT, DOT), "soweli")
    put(arrayListOf(LA, DOT, DOT, VERT, VERT, VERT, VERT), "soweli")
    put(arrayListOf(LA, VERT, VERT, DOT, DOT), "soweli")
    put(arrayListOf(DOWN, DOWN), "suli")
    put(arrayListOf(VERT, DOWN, VERT), "suli")
    put(arrayListOf(CIRCLE, BOX), "suno")
    put(arrayListOf(HOR, VERT, VERT), "supa")
    put(arrayListOf(VERT, VERT, HOR), "supa")
    put(arrayListOf(UP, DOT, UP), "suwi")
    put(arrayListOf(UP, UP, DOT), "suwi")
    put(arrayListOf(DOT, UP, UP), "suwi")
    put(arrayListOf(DOWN, IKE), "tan")
    put(arrayListOf(IKE, DOWN), "tan")
    put(arrayListOf(HOR, VERT), "taso")
    put(arrayListOf(UP, HOR, HOR), "tawa")
    put(arrayListOf(IKE, PONA, IKE, PONA), "telo")
    put(arrayListOf(PONA, IKE, PONA, IKE), "telo")
    put(arrayListOf(CIRCLE, VERT, HOR), "tenpo")
    put(arrayListOf(CIRCLE, EMITTERS), "toki")
    put(arrayListOf(UP, POKI), "tomo")
    put(arrayListOf(POKI, UP), "tomo")
    put(arrayListOf(VERT, VERT), "tu")
    put(arrayListOf(IKE, IKE, DOWN, CIRCLE), "unpa")
    put(arrayListOf(IKE, IKE, PONA, CIRCLE), "unpa")
    put(arrayListOf(PONA, HOR, DOT), "uta")
    put(arrayListOf(HOR, PONA, DOT), "uta")
    put(arrayListOf(DOWN, UP, DOWN), "utala")
    put(arrayListOf(DOWN, DOWN, UP, DOWN), "utala")
    put(arrayListOf(UP, HOR, DOWN), "walo")
    put(arrayListOf(HOR, UP, DOWN), "walo")
    put(arrayListOf(VERT), "wan")
    put(arrayListOf(UP, VERT, DOT, DOT), "waso")
    put(arrayListOf(DOT, DOT, UP, VERT), "waso")
    put(arrayListOf(VERT, CIRCLE, VERT), "wawa")
    put(arrayListOf(EMITTERS, VERT), "weka")
    put(arrayListOf(PONA, PONA), "wile")
    put(arrayListOf(CIRCLE, HOR, HOR, VERT), "tonsi")
    put(arrayListOf(CIRCLE, VERT, HOR, HOR), "tonsi")
    put(arrayListOf(CIRCLE, HOR, VERT, HOR), "tonsi")
    put(arrayListOf(UP, UP, UP, UP), "monsuta")
    put(arrayListOf(DOWN, DOWN, DOWN, DOWN), "monsuta")
    put(arrayListOf(CIRCLE, VERT, VERT, VERT, IKE, UP, VERT, VERT, VERT, DOT, DOT), "kijetesantakalu")
    put(arrayListOf(BOX, BOX), "leko")
    put(arrayListOf(DOT, VERT, DOT), "kipisi")
    put(arrayListOf(CIRCLE, CIRCLE, CIRCLE, UP), "apeja")
    put(arrayListOf(CIRCLE, CIRCLE, UP, CIRCLE), "apeja")
    put(arrayListOf(CIRCLE, UP, CIRCLE, CIRCLE), "apeja")
    put(arrayListOf(UP, CIRCLE, CIRCLE, CIRCLE), "apeja")
    put(arrayListOf(IKE, PONA, IKE, PONA, VERT), "jasima")
    put(arrayListOf(PONA, IKE, PONA, IKE, VERT), "jasima")
    put(arrayListOf(VERT, PONA, IKE, PONA, IKE), "jasima")
    put(arrayListOf(VERT, IKE, PONA, IKE, PONA), "jasima")
    put(arrayListOf(CIRCLE, DOWN, VERT), "oke")
    put(arrayListOf(DOWN, VERT, CIRCLE), "oke")
    put(arrayListOf(VERT, IKE), "n")
    put(arrayListOf(CIRCLE, POKI), "soko1")
    put(arrayListOf(IKE, HOR, POKI), "soko1")
    put(arrayListOf(VERT, DOT, VERT), "meso")
    put(arrayListOf(DOT, VERT, VERT), "meso")
    put(arrayListOf(VERT, VERT, UP), "epiku1")
    put(arrayListOf(UP, VERT, VERT), "epiku1")
    put(arrayListOf(CIRCLE, EMITTERS, HOR), "kokosila")
    put(arrayListOf(EMITTERS, CIRCLE, HOR), "kokosila")
    put(arrayListOf(IKE, PONA, IKE, DOT), "lanpan")
    put(arrayListOf(IKE, VERT, VERT, PONA, HOR), "misikeke")
    put(arrayListOf(PONA, VERT, VERT, IKE, HOR), "misikeke")
    put(arrayListOf(IKE, VERT, PONA, VERT, HOR), "misikeke")
    put(arrayListOf(PONA, VERT, IKE, VERT, HOR), "misikeke")
    put(arrayListOf(VERT, IKE, VERT, PONA, HOR), "misikeke")
    put(arrayListOf(VERT, PONA, VERT, IKE, HOR), "misikeke")
    put(arrayListOf(BOX, DOWN, HOR, DOT), "ku")
    put(arrayListOf(BOX, DOWN, HOR, CIRCLE, EMITTERS, PONA), "ku")
    put(arrayListOf(CIRCLE, HOR, HOR, EMITTERS), "isipin")
    put(arrayListOf(EMITTERS, CIRCLE, HOR, HOR), "isipin")
    put(arrayListOf(CIRCLE, EMITTERS, HOR, HOR), "isipin")
    put(arrayListOf(CIRCLE, HOR, VERT, UP, HOR), "kapesi")
    put(arrayListOf(CIRCLE, VERT, HOR, UP, HOR), "kapesi")
    put(arrayListOf(CIRCLE, HOR, VERT, HOR, UP), "kapesi")
    put(arrayListOf(CIRCLE, VERT, HOR, HOR, UP), "kapesi")
    put(arrayListOf(UP, HOR, CIRCLE, HOR, VERT), "kapesi")
    put(arrayListOf(UP, HOR, CIRCLE, VERT, HOR), "kapesi")
    put(arrayListOf(HOR, UP, CIRCLE, HOR, VERT), "kapesi")
    put(arrayListOf(HOR, UP, CIRCLE, VERT, HOR), "kapesi")
    put(arrayListOf(UP, UP, UP, DOWN, DOWN, DOWN), "kiki")
    put(arrayListOf(UP, UP, UP, UP, DOWN, DOWN, DOWN, DOWN), "kiki")
    put(arrayListOf(VERT, HOR, VERT, PONA, IKE, VERT, DOT, DOT, DOT), "linluwi")
    put(arrayListOf(VERT, HOR, VERT, DOT, DOT, DOT, PONA, IKE, VERT), "linluwi")
    put(arrayListOf(VERT, HOR, VERT, VERT, PONA, IKE, DOT, DOT, DOT), "linluwi")
    put(arrayListOf(VERT, HOR, VERT, VERT, DOT, DOT, DOT, PONA, IKE), "linluwi")
    put(arrayListOf(UP, HOR, CIRCLE, CIRCLE, CIRCLE), "mulapisu")
    put(arrayListOf(HOR, UP, CIRCLE, CIRCLE, CIRCLE), "mulapisu")
    put(arrayListOf(CIRCLE, CIRCLE, CIRCLE, UP, HOR), "mulapisu")
    put(arrayListOf(CIRCLE, CIRCLE, CIRCLE, HOR, UP), "mulapisu")
    put(arrayListOf(VERT, HOR, HOR), "pake")
    put(arrayListOf(HOR, HOR, VERT), "pake")
    put(arrayListOf(VERT, HOR, LA), "Pingo")
    put(arrayListOf(VERT, UP, DOWN), "powe")
    put(arrayListOf(LA, LA, UP, HOR), "unu")
    put(arrayListOf(LA, LA, HOR, UP), "unu")
    put(arrayListOf(UP, HOR, LA, LA), "unu")
    put(arrayListOf(HOR, UP, LA, LA), "unu")
    put(arrayListOf(VERT, PONA, PONA), "wa")

    // NON-iOS KEY SEQUENCES

    // Personal additions: mostly punctuation
    put(arrayListOf(COLON), ":")
    put(arrayListOf(COMMA), ",")
    put(arrayListOf(VERT, COMMA), "!")
    put(arrayListOf(IKE, DOT), "?")
    put(arrayListOf(PONA, VERT, PONA), "yupekosi") // nimi yupekosi li nimi ku suli
    put(arrayListOf(PONA, LA), "yupekosi")
    put(arrayListOf(HOR, DOWN, UP), "powe") // a MUCH more accurate powe, hor + ala
    put(arrayListOf(UP, HOR, CIRCLE, BOX), "jelo") // for symmetry with other colors
    // TODO if you have any better recommendations for () and [], let me know! These are experimental.
    put(arrayListOf(OP_BR), "(")
    put(arrayListOf(CL_BR), ")")
    put(arrayListOf(OP_BR, OP_BR), "[")
    put(arrayListOf(CL_BR, CL_BR), "]")

    // UCSUR IMPLEMENTATION COMPATIBILITY
    put(arrayListOf(CIRCLE, UP, PONA), "alelipona")
    put(arrayListOf(EMITTERS, PONA), "epiku")
    put(arrayListOf(PONA, EMITTERS), "epiku")
    put(arrayListOf(EMITTERS, HOR, HOR), "majuna")
    put(arrayListOf(HOR, EMITTERS, HOR), "majuna")
    put(arrayListOf(HOR, HOR, EMITTERS), "majuna")
    put(arrayListOf(IKE, HOR, VERT), "soko")
    put(arrayListOf(IKE, HOR, VERT, HOR), "soko")
    put(arrayListOf(IKE, HOR, HOR, VERT), "soko")
    put(arrayListOf(CIRCLE, EMITTERS, PONA), "toki-pona")
    put(arrayListOf(COLON, LA, LA), ":3")
    put(arrayListOf(COLON, PONA, PONA), ":3")
    put(arrayListOf(COLON, CL_BR, CL_BR), ":3")
    put(arrayListOf(COLON, VERT, LA), ":D")
    put(arrayListOf(COLON, HOR, PONA), ":D")
    put(arrayListOf(COLON, VERT, CL_BR), ":D")
    put(arrayListOf(COLON, VERT, LUKA), ":P")
    put(arrayListOf(COLON, HOR, LUKA), ":P")

    // NIMI SU COMPATIBILITY
    put(arrayListOf(BOX, IKE, IKE), "su") // I made the su sequences
    put(arrayListOf(BOX, IKE, LA), "su")
    put(arrayListOf(LUKA, LUKA, LUKA, LUKA), "mute2")
    put(arrayListOf(VERT, VERT, HOR, DOT), "sewi2") // supa + dot (I made these)
    put(arrayListOf(HOR, VERT, VERT, DOT), "sewi2") // supa + dot
    put(arrayListOf(VERT, HOR, VERT, DOT), "sewi2") // how i write it
    put(arrayListOf(DOT, COMMA, COMMA), "te") // after slight adjustment, we borrow the USCUR quote sequences as te/to
    put(arrayListOf(COMMA, COMMA, DOT), "to") // these could just be quote marks in su, but we have a sequence for that.
}

data class SequenceMapping(val text: String, val radicals: String)

// that's right friends, we're making a COPY of the list! :(
val displaySequences: List<SequenceMapping> = sequences.map { (seq, str) ->
    SequenceMapping(str, buildString {
        for (key in seq) append("$UCSUR_HIGH${key.surr}")
    })
}.sortedWith(compareBy({
    when (it.text) {
        "Pingo" -> "pingo" // I mean come on. it's non-iOS but...
        // manually adjust some rogue punctuation
        "[" -> ")("
        "]" -> "))"
        "!" -> "))("
        "?" -> ")))"
        else -> it.text
    }
}, { it.radicals }))

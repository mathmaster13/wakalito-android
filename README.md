# wakalito-android

**Note:** This is a direct port of [wakalito-ios](https://github.com/tbodt/wakalito-ios), so it is designed for
sitelen Lasina. [This keyboard](https://keyman.com/keyboards/sp_wakalito_ucsur) by jan Lepeka is for UCSUR.

**Note:** All files in the project are [GPLv3](./LICENSE), *except for* the [internal font](./app/src/main/res/font/compose_glyph_font.ttf), which is licensed under the [OFL](./OFL.txt).
It is a modification of [this font](https://www.kreativekorp.com/software/fonts/fairfaxponahd/) made by jan Lepeka. 

The current (unstable) build is in the usual [app build directory](./app/build/outputs/apk/debug/app-debug.apk), or you can get a release from the usual spot.

## Compatibility
Every sequence in wakalito-ios, every sequence in jan Lepeka's implementation except certain UCSUR-specific punctuation, every glyph in lipu su, yupekosi, and extra punctuation. Some glyphs may have added alternate sequences from me.

## Known Possible Issues
The keyboard is stable as far as I have tested, but it is theoretically possible for the keyboard to crash.
It is also possible that you may need to click a text box twice for the keyboard to load.
These did not happen in recent builds, but happened in older ones, so let me know if they come back.

## Credits

jan Tepo and kala pona Tonyu, for writing the code that I have the honor of butchering.

kala pona Tonyu and waso Likipi, for wakalito, and jan Lepeka for the UCSUR implementation and making fonts that are amazingly easy to modify.

jan Ke Tami, for [this page](https://sona.pona.la/wiki/User:Jan_Ke_Tami/Oz_words) on nimi su.

Waydroid, for allowing me to debug when Android Studio's emulator failed.

[toki pona keyboard](https://github.com/timeopochin/tokiponakeyboard), for giving me something to
sanity-check my code against, and for inspiring the implementation of the backspace and enter key.

[iconmonstr](https://iconmonstr.com) for the backspace and some of the enter key icons.

Enter key icons were inspired by the AOSP keyboard that Waydroid uses.

stella, for testing and helping me debug.

# wakalito-android

**Note:** This is a direct port of [wakalito-ios](https://github.com/tbodt/wakalito-ios), so it is designed for
sitelen Lasina. [This keyboard](https://keyman.com/keyboards/sp_wakalito_ucsur) by jan Lepeka is for UCSUR.

**Note:** All files in the project are [GPLv3](./LICENSE), *except for* the [internal font](./app/src/main/res/font/compose_glyph_font.ttf), which is licensed under the [OFL](./OFL.txt).
It is a modification of [this font](https://www.kreativekorp.com/software/fonts/fairfaxponahd/) made by jan Lepeka. 

Debug builds are in the usual app build directory; old builds are [here](https://mathmaster13.github.io/wakalito-android).

## Compatibility
Every sequence in wakalito-ios, every sequence in jan Lepeka's implementation except certain UCSUR-specific punctuation, every glyph in lipu su, yupekosi, and extra punctuation. Some glyphs may have added alternate sequences from me.

## Known Issues (will be fixed soon!)
- Unicode and the space/delete key do not play nicely. Specifically, I might delete half of a surrogate pair.
- Theoretically, the keyboard can crash if the editor takes way too long to get
the text around the cursor. This should not happen, but if it does, let me know.

## Unimplemented Features
- The app itself, which allows you to see a chart of all radical sequences allowed.
- App icons are intentionally not added until the app is done.
- Dark mode.

## Credits

No one on this list was involved in the code for this project.

jan Tepo and kala pona Tonyu, for writing the code that I have the honor of butchering.

kala pona Tonyu and waso Likipi, for wakalito, and jan Lepeka for the UCSUR implementation and making fonts that are amazingly easy to modify.

jan Ke Tami, for [this page](https://sona.pona.la/wiki/User:Jan_Ke_Tami/Oz_words) on nimi su.

Waydroid, for allowing me to debug when Android Studio's emulator failed.

[toki pona keyboard](https://github.com/timeopochin/tokiponakeyboard), for giving me something to
sanity-check my code against, and for inspiring the implementation of the backspace key.

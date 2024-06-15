# wakalito-android

**Note:** This is a direct port of [wakalito-ios](https://github.com/tbodt/wakalito-ios). This means it is specifically designed for
*sitelen Lasina*. sina wile kepeken nasin UCSUR la o kepeken [nasin Wakalito pi ilo Kiman](https://keyman.com/keyboards/sp_wakalito_ucsur).
ona li tan mi ala li tan jan Lepeka.

**Note:** The project is [GPLv3](./LICENSE), but the font in the resources directory is licensed under the [OFL](./OFL.txt).
It is a modification of [this font](https://www.kreativekorp.com/software/fonts/fairfaxponahd/) made by jan Lepeka. I will publish the exact modifications soon.

Get a built APK [here](https://mathmaster13.github.io/wakalito-android).

## Compatibility
This is compatible with every nimi ku suli (including yupekosi), every glyph in su, every sequence in wakalito-ios,
and every sequence in jan Lepeka's implementation except certain UCSUR-specific punctuation.

## Known Issues (will be fixed soon!)

- Unicode and the space/delete key do not play nicely, especially with surrogate pairs.
- Theoretically, the keyboard can crash if the editor takes way too long to get
the text around the cursor. This should not happen, but if it does, let me know.

## Unimplemented Features

- The app itself, which allows you to see a chart of all radical sequences allowed.
- App icons are intentionally not added until the app is done.
- Dark mode.

## Credits

jan Tepo and kala pona Tonyu, for writing the code that I have the honor of butchering.

kala pona Tonyu and waso Likipi, for wakalito, and jan Lepeka for the UCSUR implementation and making fonts that are amazingly easy to modify.

jan Ke Tami, for [this page](https://sona.pona.la/wiki/User:Jan_Ke_Tami/Oz_words) on nimi su.

Waydroid, for allowing me to debug when Android Studio's emulator failed.

[toki pona keyboard](https://github.com/timeopochin/tokiponakeyboard), for giving me something to
sanity-check my code against, and for inspiring the implementation of the backspace key.

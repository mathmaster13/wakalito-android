# wakalito-android

A work-in-progress [wakalito-ios](https://github.com/tbodt/wakalito-ios) port.

Get a built APK [here](https://mathmaster13.github.io/wakalito-android).

## Known Issues

- Keyboard may not display correctly in landscape mode or on wider screens.
- `delete_fallback()` is unimplemented.
- The box displaying the sequence you are currently typing is transparent.
- The return key sends a hard keyboard "ENTER".

## Unimplemented Features

- The app itself, which allows you to see a chart of all radical sequences allowed.
- linja sike is not included yet, so a text-based approximation is used for now.
- The keyboard color scheme is not finalized, and its layout is not very polished.
  - As such, no dark mode yet.

## Credits

jan Tepo, for writing the code that I have the honor of butchering.

kala pona Tonyu and jan Likipi, for wakalito.

Waydroid, for allowing me to debug when Android Studio's emulator failed.

[toki pona keyboard](https://github.com/timeopochin/tokiponakeyboard), for giving me something to
sanity-check my code against, and for inspiring the implementation of the backspace key.

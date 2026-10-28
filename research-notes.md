# External research notes

- Target APK is the release asset `gboard-18-5-4-993608717-release-arm64-v8a.xapk` from https://github.com/Error404rt/Gboard-alphabetical-patch/releases/tag/gboard.
- The XAPK contains `com.google.android.inputmethod.latin.apk`; apktool 3.0.3 decodes its keyboard definitions under `res/xml/`.
- Existing Morphe Gboard implementation reference: https://github.com/JZ6/Flexboard. Its README/source shows Gboard patches use `resourcePatch`, `ResourcePatchContext.get(path, true)`, and write decoded XML resources in `finalize`.
- The target Gboard APK (`18.5.4.993608717-release-arm64-v8a`) has the Russian IME in `res/xml/APKTOOL_RENAMED_0x7f17036d.xml`, with the default Russian main definition `APKTOOL_RENAMED_0x7f1708a1.xml` and standard softkey definition `APKTOOL_RENAMED_0x7f171151.xml`.
- The Russian default softkey definition explicitly contains `long_press_data` for `Е/е` including `ё/Ё`, `И/и` does not currently include `й/Й`, and `Ь/ь` maps long press to `Ъ/ъ` in the stock list. The requested patch will define the exact requested pairs: Е→Ё, И→Й, Ь→Ъ.
- The English IME is in `res/xml/APKTOOL_RENAMED_0x7f17045e.xml` (`string_id="ime_qwerty"`) and uses main definition `APKTOOL_RENAMED_0x7f1707eb.xml`. The actual non-tablet body wrapper is `APKTOOL_RENAMED_0x7f1707ec.xml` and currently includes the stock English softkeys `APKTOOL_RENAMED_0x7f170e30.xml`.
- The Russian standard key mapping is `APKTOOL_RENAMED_0x7f170ab4.xml`, mapping view slots `D01..D12`, `C01..C11`, `B01..B09` to obfuscated key ids. The base state has 30 unique letter positions because some long-press letters are not separate visible keys.
- The English stock softkeys include stable-for-this-target key ids and press data in `APKTOOL_RENAMED_0x7f170f9b.xml`; the common mapping files use the same D/C/B view slot convention.
- Official Morphe patch template docs: https://github.com/MorpheApp/morphe-patches-template/blob/main/README.md. The user's repository template README says local build is `./gradlew buildAndroid`, output `patches/build/libs/patches-*.mpp`, and semantic `feat:`/`fix:` commits trigger releases.
